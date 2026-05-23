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
import java.util.Map;

@RestController
@RequestMapping("/api/ketca")
@CrossOrigin("*")
public class KetCaController {
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private PdfService4KetCa PdfService4KetCa;

    @PostMapping("/save-log")
    public ResponseEntity<?> saveLog(@RequestBody Map<String, Object> payload) {
        // 1. Trích xuất an toàn thuộc tính mayPos từ JSON Frontend gửi lên
        Integer mayPos = 1;
        if (payload.containsKey("mayPos") && payload.get("mayPos") != null) {
            mayPos = Integer.parseInt(payload.get("mayPos").toString());
        }
        
        try {
            // Sửa lỗi ép kiểu ngầm tiềm ẩn cho nhanVienId
            Integer nhanVienId = payload.get("nhanVienId") != null ? ((Number) payload.get("nhanVienId")).intValue() : null;
            if (nhanVienId == null) {
                return ResponseEntity.badRequest().body("Thiếu ID nhân viên/quản lý!");
            }

            Double cashCounted = 0.0;
            if (payload.get("tienmat") != null) {
                cashCounted = ((Number) payload.get("tienmat")).doubleValue();
            }
            
            Boolean quanly = (Boolean) payload.get("quanly");
            
            if (quanly) {
                NhanVien nv = nhanVienRepo.findById(nhanVienId).orElse(null);
                if (nv == null) {
                    return ResponseEntity.badRequest().body("Không tìm thấy nhân viên/quản lý!");
                }
                
                CaLam newShift = new CaLam();
                newShift.setNhanVien(nv);
                newShift.setBatdau(LocalDateTime.now()); 
                newShift.setKetthuc(LocalDateTime.now()); 
                newShift.setTienmatKetca(cashCounted); 
                
                // ĐÃ SỬA: Gán giá trị máy POS vào đây để Oracle không báo lỗi NULL ở luồng INSERT
                newShift.setMayPos(mayPos); 
                
                caLamRepo.save(newShift); 
                return ResponseEntity.ok("Tạo ca mới thành công!");
            } else {
                // Logic cập nhật ca cũ
                CaLam shift = caLamRepo.findTopByNhanVienIdOrderByBatdauDesc(nhanVienId);
                if (shift == null) {
                    return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy ca làm gần nhất!");
                }

                shift.setKetthuc(LocalDateTime.now()); 
                shift.setTienmatKetca(cashCounted);
                
                // ĐÃ SỬA: Đồng bộ cập nhật hoặc giữ vững số máy POS ở luồng UPDATE
                shift.setMayPos(mayPos);
                
                Double sysBank = shift.getTienNganhang() != null ? shift.getTienNganhang() : 0.0;
                shift.setTongtienThucte(sysBank + cashCounted);
                
                caLamRepo.save(shift); 
                return ResponseEntity.ok("Cập nhật kết ca thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi Server: " + e.getMessage());
        }
    }

    @GetMapping("/logs-today")
    public List<CaLam> getLogsToday(@RequestParam(defaultValue = "1") Integer mayPos) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        
        // Truyền tham số mayPos động nhận từ Frontend gửi lên
        return caLamRepo.findByMayPosAndBatdauBetweenAndKetthucIsNotNullOrderByIdAsc(mayPos, start, end);
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