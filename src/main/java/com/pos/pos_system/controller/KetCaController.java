package com.pos.pos_system.controller;

import com.pos.pos_system.dto.PayLoadTienMat;
import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.repository.CaLamRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import com.pos.pos_system.service.PdfService4KetCa;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/ketca")
@CrossOrigin("*")
public class KetCaController {
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private PdfService4KetCa PdfService4KetCa;

    @PostMapping("/save-log")
    public ResponseEntity<?> saveLog(@RequestBody PayLoadTienMat payload) {
        try {
            Integer nhanVienId = payload.getNhanVienId(); 
            Double cashCounted = payload.getTienmat();

            // Tìm ca làm việc đang mở (chưa có giờ kết thúc) của nhân viên này để xử lý
            CaLam shift = caLamRepo.findTopByNhanVienIdOrderByBatdauDesc(nhanVienId);
            
            // Dự phòng trường hợp không tìm thấy theo ID cụ thể, quét ca gần nhất hệ thống
            if (shift == null) {
                shift = caLamRepo.findTopByOrderByBatdauDesc();
            }

            if (shift == null) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy ca làm việc cần chốt!");
            }

            // XỬ LÝ THỜI GIAN: Nếu frontend có truyền mốc thời gian sang, ép kiểu về LocalDateTime
            if (payload.getKetThucTime() != null && !payload.getKetThucTime().isEmpty()) {
                java.time.ZonedDateTime zdt = java.time.ZonedDateTime.parse(payload.getKetThucTime());
                shift.setKetthuc(zdt.toLocalDateTime());
            } else {
                shift.setKetthuc(LocalDateTime.now());
            }

            shift.setTienmatKetca(cashCounted);
            Double sysBank = shift.getTienNganhang() != null ? shift.getTienNganhang() : 0.0;
            shift.setTongtienThucte(sysBank + cashCounted);
            
            caLamRepo.save(shift); // Thực thi lệnh UPDATE hoàn tất ca xuống Oracle
            return ResponseEntity.ok("Cập nhật kết ca thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/logs-today")
    public List<CaLam> getLogsToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return caLamRepo.findByBatdauBetweenOrderByIdAsc(start, end);
    }

    @GetMapping("/export-pdf/{id}")
    public void exportToPDF(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=BaoCao_" + id + ".pdf");

        CaLam shift = caLamRepo.findById(id).orElse(null);
        if (shift != null) {
            PdfService4KetCa.exportShiftReport(response, shift);
        }
    }
}