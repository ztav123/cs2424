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
            @RequestParam(required = false, defaultValue = "ALL") String empIdStr) { // ALL = Toàn cửa hàng

        // 1. LUÔN LẤY TỔNG QUAN CHO TOÀN CỬA HÀNG (Không filter theo empId)
        Double tong = hoadonRepo.tinhTongDoanhThu(from, to, null);
        Long soDon = hoadonRepo.demSoDonHang(from, to, null);
        String spChung = hoadonRepo.findSpBanChayNhat(from, to, null);

        // 2. Xử lý logic lọc nhân viên cho Bảng
        Integer empId = null;
        if (!"ALL".equals(empIdStr)) {
            try { empId = Integer.parseInt(empIdStr); } catch (Exception ignored) {}
        }

        List<ThongKeResponse.DailyReport> chiTietNgay = new ArrayList<>();
        java.time.LocalDate curr = from.toLocalDate();
        java.time.LocalDate end = to.toLocalDate();

        // 3. Vòng lặp quét từng ngày để tạo ra các dòng Báo cáo
        while (!curr.isAfter(end)) {
            LocalDateTime startOfDay = curr.atStartOfDay();
            LocalDateTime endOfDay = curr.atTime(23, 59, 59);
            String dateStr = String.format("%02d/%02d/%04d", curr.getDayOfMonth(), curr.getMonthValue(), curr.getYear());

            // --- A. Query Doanh thu của ngày hôm đó ---
            StringBuilder sqlDt = new StringBuilder("SELECT SUM(hd.tongtien) FROM HOADON hd WHERE hd.trangthai = 'HOAN_THANH' AND hd.ngaylap >= :startOfDay AND hd.ngaylap <= :endOfDay");
            if (empId != null) sqlDt.append(" AND hd.nhanvien_id = :empId");
            
            Query queryDt = entityManager.createNativeQuery(sqlDt.toString());
            queryDt.setParameter("startOfDay", java.sql.Timestamp.valueOf(startOfDay));
            queryDt.setParameter("endOfDay", java.sql.Timestamp.valueOf(endOfDay));
            if (empId != null) queryDt.setParameter("empId", empId);
            
            Double doanhThuNgay = 0.0;
            Object resultDt = queryDt.getSingleResult();
            if (resultDt != null) doanhThuNgay = ((Number) resultDt).doubleValue();

            // --- B. Query SP Bán chạy nhất của ngày hôm đó ---
            String spBanChayNgay = "Không có";
            if (doanhThuNgay > 0) {
                StringBuilder sqlSp = new StringBuilder(
                    "SELECT sp.ten FROM CTHD ct " +
                    "JOIN HOADON hd ON ct.hoadon_id = hd.id " +
                    "JOIN SANPHAM sp ON ct.sanpham_id = sp.id " +
                    "WHERE hd.trangthai = 'HOAN_THANH' AND hd.ngaylap >= :startOfDay AND hd.ngaylap <= :endOfDay"
                );
                if (empId != null) sqlSp.append(" AND hd.nhanvien_id = :empId");
                sqlSp.append(" GROUP BY sp.ten ORDER BY SUM(ct.soluong) DESC");
                
                Query querySp = entityManager.createNativeQuery(sqlSp.toString());
                querySp.setParameter("startOfDay", java.sql.Timestamp.valueOf(startOfDay));
                querySp.setParameter("endOfDay", java.sql.Timestamp.valueOf(endOfDay));
                if (empId != null) querySp.setParameter("empId", empId);
                
                // setMaxResults(1) đảm bảo an toàn trên mọi loại Database (MySQL, SQL Server, Oracle)
                querySp.setMaxResults(1); 
                List<?> resultSp = querySp.getResultList();
                if (!resultSp.isEmpty() && resultSp.get(0) != null) {
                    spBanChayNgay = resultSp.get(0).toString();
                }
            }

            // --- C. Đóng gói dòng và đẩy vào mảng ---
            chiTietNgay.add(new ThongKeResponse.DailyReport(dateStr, doanhThuNgay, spBanChayNgay));
            
            curr = curr.plusDays(1); // Sang ngày tiếp theo
        }

        return new ThongKeResponse(tong, soDon, spChung != null ? spChung : "Chưa có", chiTietNgay);
    }
}