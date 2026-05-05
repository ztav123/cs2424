package com.pos.pos_system.controller;

import com.pos.pos_system.entity.KetCaLog;
import com.pos.pos_system.repository.KetCaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ketca")
@CrossOrigin("*")
public class KetCaController {

    @Autowired private KetCaLogRepository ketCaLogRepo;

    // API 1: Lưu bảng kê đếm tiền vào database
    @PostMapping("/save-log")
    public ResponseEntity<?> saveLog(@RequestBody Map<String, Integer> payload) {
        Integer nhanVienId = payload.get("nhanVienId");
        
        // Tìm log nháp vừa được AuthService tạo ra ở bước trên
        KetCaLog log = ketCaLogRepo.findTopByNhanVienIdOrderByThoigianDesc(nhanVienId);
        if (log == null) return ResponseEntity.badRequest().body("Không tìm thấy ca làm việc cần chốt!");

        // Cập nhật số tiền nhân viên đếm được vào log
        log.setTienmat(payload.get("tienmat"));
        log.setTongtien(log.getTiennganhang() + payload.get("tienmat"));
        
        ketCaLogRepo.save(log);
        return ResponseEntity.ok("Cập nhật bảng kê thành công!");
    }

    // API 2: Lấy danh sách kết ca trong ngày hôm nay để vẽ lên UI
    @GetMapping("/logs-today")
    public List<KetCaLog> getLogsToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return ketCaLogRepo.findByThoigianBetween(startOfDay, endOfDay);
    }
}