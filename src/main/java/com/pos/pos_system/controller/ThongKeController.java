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
            @RequestParam(required = false) Integer nhanVienId) {

        // 1. Tính tổng và SP bán chạy từ Repository (An toàn 100%)
        Double tong = hoadonRepo.tinhTongDoanhThu(from, to, nhanVienId);
        Long soDon = hoadonRepo.demSoDonHang(from, to, nhanVienId);
        String spChung = hoadonRepo.findSpBanChayNhat(from, to, nhanVienId);
        if (spChung == null) spChung = "Chưa có dữ liệu";

        // 2. Native Query cho Biểu đồ (Đã tối giản, không lo lỗi ORA-00979)
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TO_CHAR(hd.ngaylap, 'DD/MM') as ngay, SUM(hd.tongtien) as doanhthu ");
        sql.append("FROM HOADON hd WHERE hd.ngaylap BETWEEN :from AND :to ");
        
        if (nhanVienId != null) {
            sql.append("AND hd.nhanvien_id = :nvId ");
        }
        
        sql.append("GROUP BY TO_CHAR(hd.ngaylap, 'DD/MM') ORDER BY TO_CHAR(hd.ngaylap, 'DD/MM')");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("from", from);
        query.setParameter("to", to);
        if (nhanVienId != null) query.setParameter("nvId", nhanVienId);

        List<Object[]> results = query.getResultList();
        List<ThongKeResponse.BieuDoData> bieuDo = new ArrayList<>();

        for (Object[] obj : results) {
            String ngay = obj[0] != null ? (String) obj[0] : "";
            Double doanhThu = obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0;
            // Gắn SP bán chạy chung vào từng ngày để tooltip không bị trống
            bieuDo.add(new ThongKeResponse.BieuDoData(ngay, doanhThu, spChung));
        }

        return new ThongKeResponse(tong, soDon, spChung, bieuDo);
    }
}