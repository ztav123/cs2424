package com.pos.pos_system.controller;

import com.pos.pos_system.dto.ThongKeResponse;
import com.pos.pos_system.repository.HoaDonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/thongke")
@CrossOrigin("*")
public class ThongKeController {

    @Autowired
    private HoaDonRepository hoadonRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/doanhthu")
    public ThongKeResponse getStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "STORE") String mode, // "STORE" hoặc "ALL_EMP"
            @RequestParam(required = false) List<Integer> empIds) { // Danh sách ID nhân viên (Nếu chọn tùy chỉnh)

        // 1. Tính Tổng (Giữ nguyên logic cũ của bạn)
        Double tong = hoadonRepo.tinhTongDoanhThu(from, to, null);
        Long soDon = hoadonRepo.demSoDonHang(from, to, null);
        String spChung = hoadonRepo.findSpBanChayNhat(from, to, null);

        // 2. Lấy dữ liệu thô từ Database
        StringBuilder sql = new StringBuilder();
        if (mode.equals("STORE")) {
            // Chế độ cửa hàng: Chỉ lấy theo ngày
            sql.append("SELECT TO_CHAR(hd.ngaylap, 'DD/MM') as ngay, 'Toàn cửa hàng' as ten, SUM(hd.tongtien) as doanhthu ");
            sql.append("FROM HOADON hd WHERE hd.trangthai = 'HOAN_THANH' AND hd.ngaylap >= :from AND hd.ngaylap <= :to ");
            sql.append("GROUP BY TO_CHAR(hd.ngaylap, 'DD/MM') ORDER BY TO_CHAR(hd.ngaylap, 'DD/MM')");
        } else {
            // Chế độ tất cả nhân viên: Lấy theo ngày VÀ theo nhân viên
            sql.append("SELECT TO_CHAR(hd.ngaylap, 'DD/MM') as ngay, (SELECT nv.hoten FROM NHANVIEN nv WHERE nv.id = hd.nhanvien_id) as ten, SUM(hd.tongtien) as doanhthu ");
            sql.append("FROM HOADON hd WHERE hd.trangthai = 'HOAN_THANH' AND hd.ngaylap >= :from AND hd.ngaylap <= :to ");
            
            // Nếu có lọc riêng danh sách nhân viên
            if (empIds != null && !empIds.isEmpty()) {
                sql.append("AND hd.nhanvien_id IN (:empIds) ");
            }
            sql.append("GROUP BY TO_CHAR(hd.ngaylap, 'DD/MM'), hd.nhanvien_id ORDER BY TO_CHAR(hd.ngaylap, 'DD/MM')");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("from", java.sql.Timestamp.valueOf(from));
        query.setParameter("to", java.sql.Timestamp.valueOf(to));
        if (mode.equals("ALL_EMP") && empIds != null && !empIds.isEmpty()) {
            query.setParameter("empIds", empIds);
        }

        List<Object[]> results = query.getResultList();

        // 3. TẠO MẢNG LABELS (TRỤC X) - Lấp đầy các ngày
        List<String> labels = new ArrayList<>();
        java.time.LocalDate curr = from.toLocalDate();
        java.time.LocalDate end = to.toLocalDate();
        while (!curr.isAfter(end)) {
            String dayStr = String.format("%02d/%02d", curr.getDayOfMonth(), curr.getMonthValue());
            labels.add(dayStr);
            curr = curr.plusDays(1);
        }

        // 4. PHÂN BỔ DỮ LIỆU VÀO DATASET
        java.util.Map<String, List<Double>> datasetMap = new java.util.LinkedHashMap<>();
        for (Object[] row : results) {
            String ngay = (String) row[0];
            String tenNV = row[1] != null ? (String) row[1] : "Không xác định";
            Double tien = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;

            // Nếu nhân viên này chưa có trong Map, tạo mảng 0.0 cho tất cả các ngày
            datasetMap.putIfAbsent(tenNV, new ArrayList<>(java.util.Collections.nCopies(labels.size(), 0.0)));
            
            // Cắm tiền vào đúng index của ngày
            int dayIndex = labels.indexOf(ngay);
            if (dayIndex != -1) {
                datasetMap.get(tenNV).set(dayIndex, tien);
            }
        }

        // Chuyển Map thành List Dataset
        List<ThongKeResponse.ChartDataset> datasets = new ArrayList<>();
        for (java.util.Map.Entry<String, List<Double>> entry : datasetMap.entrySet()) {
            datasets.add(new ThongKeResponse.ChartDataset(entry.getKey(), entry.getValue()));
        }

        return new ThongKeResponse(tong, soDon, spChung != null ? spChung : "Chưa có", labels, datasets);
    }
}