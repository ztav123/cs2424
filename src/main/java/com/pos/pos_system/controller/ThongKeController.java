package com.pos.pos_system.controller;

import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thongke")
@CrossOrigin("*")
public class ThongKeController {

    @Autowired
    private HoaDonRepository hoadonRepo;

    @GetMapping("/doanhthu")
    public Map<String, Object> getStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer nhanVienId) {

        List<HoaDon> dsHoaDon;
        
        // Nếu Quản lý có chọn 1 nhân viên cụ thể
        if (nhanVienId != null && nhanVienId > 0) {
            dsHoaDon = hoadonRepo.findByNhanVienIdAndNgaylapBetween(nhanVienId, from, to);
        } else {
            // Nếu Quản lý chọn "Tất cả nhân viên"
            dsHoaDon = hoadonRepo.findByNgaylapBetween(from, to);
        }

        // Tính tổng doanh thu và số lượng đơn hàng
        double tongDoanhThu = 0;
        for (HoaDon hd : dsHoaDon) {
            if (hd.getTongtien() != null) {
                tongDoanhThu += hd.getTongtien();
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("tongDoanhThu", tongDoanhThu);
        response.put("soDonHang", dsHoaDon.size());
        
        return response;
    }
}