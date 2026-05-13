package com.pos.pos_system.controller;

import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.repository.CaLamRepository;
import com.pos.pos_system.service.PdfService4KetCa; // Thêm import này
import jakarta.servlet.http.HttpServletResponse; // Thêm import này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ketca")
@CrossOrigin("*")
public class KetCaController {

    @Autowired private CaLamRepository caLamRepo;
    @Autowired private PdfService4KetCa PdfService4KetCa; // Thêm dòng này

    @PostMapping("/save-log")
    public ResponseEntity<?> saveLog(@RequestBody Map<String, Double> payload) {
        Integer nhanVienId = payload.get("nhanVienId").intValue(); 
        Double cashCounted = payload.get("tienmat");

        CaLam shift = caLamRepo.findTopByNhanVienIdOrderByBatdauDesc(nhanVienId);
        if (shift == null) return ResponseEntity.badRequest().body("Lỗi tìm ca!");

        shift.setTienmatKetca(cashCounted);
        Double sysBank = shift.getTienNganhang() != null ? shift.getTienNganhang() : 0.0;
        shift.setTongtienThucte(sysBank + cashCounted);
        
        caLamRepo.save(shift);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @GetMapping("/logs-today")
    public List<CaLam> getLogsToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return caLamRepo.findByBatdauBetweenOrderByIdAsc(start, end);
    }

    // --- ĐÂY LÀ HÀM CÒN THIẾU KHIẾN XUẤT PDF KHÔNG HOẠT ĐỘNG ---
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