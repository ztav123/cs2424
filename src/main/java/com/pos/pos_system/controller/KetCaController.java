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
    // Sử dụng thẳng DTO PayLoadTienMat ở đây
    public ResponseEntity<?> saveLog(@RequestBody PayLoadTienMat payload) {
        try {
            Integer nhanVienId = payload.getNhanVienId(); 
            Double cashCounted = payload.getTienmat();
            Boolean quanly = payload.getQuanly();

            if (quanly) {
                NhanVien nv = nhanVienRepo.findById(nhanVienId).orElse(null);
                if (nv == null) {
                    return ResponseEntity.badRequest().body("Không tìm thấy nhân viên/quản lý!");
                }
                
                CaLam newShift = new CaLam();
                newShift.setNhanVien(nv);
                // Tạo ca mới thì set thời gian BẮT ĐẦU nhé
                newShift.setBatdau(LocalDateTime.now()); 
                newShift.setKetthuc(LocalDateTime.now()); 
                newShift.setTienmatKetca(cashCounted); 
                
                caLamRepo.save(newShift); // Lệnh này sẽ tạo ra log INSERT
                return ResponseEntity.ok("Tạo ca mới thành công!");
            } else {
                // Logic cập nhật ca cũ
                CaLam shift = caLamRepo.findTopByNhanVienIdOrderByBatdauDesc(nhanVienId);
                if (shift == null) {
                    return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy ca làm gần nhất!");
                }

                shift.setKetthuc(LocalDateTime.now()); // Set thời gian kết thúc
                shift.setTienmatKetca(cashCounted);
                Double sysBank = shift.getTienNganhang() != null ? shift.getTienNganhang() : 0.0;
                shift.setTongtienThucte(sysBank + cashCounted);
                
                caLamRepo.save(shift); // Lệnh này sẽ tạo ra log UPDATE
                return ResponseEntity.ok("Cập nhật kết ca thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi Server: " + e.getMessage());
        }
    }

    @GetMapping("/logs-today")
    public List<CaLam> getLogsToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return caLamRepo.findByBatdauBetweenAndKetthucIsNotNullOrderByIdAsc(start, end);
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