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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            
            // Trích xuất an toàn giá trị quanly đề phòng bị null
            Boolean quanly = payload.get("quanly") != null ? (Boolean) payload.get("quanly") : false;
            
            if (!quanly) {
                // =========================================================================
                // LUỒNG 1: THU NGÂN KẾT CA (CẬP NHẬT CA HIỆN TẠI)
                // =========================================================================
                CaLam shift = caLamRepo.findTopByNhanVienIdOrderByBatdauDesc(nhanVienId);
                if (shift == null) {
                    return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy ca làm gần nhất!");
                }
                
                // Gọi Procedure PROC_KET_CA cập nhật tiền và đóng ca làm việc an toàn
                caLamRepo.thucHienKetCa(shift.getId(), cashCounted, "Kết ca từ App");
                return ResponseEntity.ok("Cập nhật kết ca thành công!");
                
            } else {
                // =========================================================================
                // LUỒNG 2: QUẢN LÝ MỞ CA MỚI (CHỖ THIẾU KHIẾN HÀM BÁO LỖI)
                // =========================================================================
                NhanVien nv = nhanVienRepo.findById(nhanVienId).orElse(null);
                if (nv == null) {
                    return ResponseEntity.badRequest().body("Không tìm thấy nhân viên/quản lý!");
                }
                
                CaLam newShift = new CaLam();
                newShift.setNhanVien(nv);
                newShift.setBatdau(LocalDateTime.now());
                newShift.setTienmatKetca(cashCounted); // Ca mới chưa có tiền kết ca
                newShift.setMayPos(mayPos);
                
                // Giá trị ban đầu truyền vào là 0.0 hoặc null, Trigger TRG_CALAM_TIENMAT_BANDAU 
                // dưới DB sẽ tự động lấy tiền kết ca của ca trước gán vào ca này.
                newShift.setTienmatBandau(0.0); 
                newShift.setKetthuc(LocalDateTime.now());
                
                caLamRepo.save(newShift);
                return ResponseEntity.ok("Tạo ca mới thành công!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi Server: " + e.getMessage());
        }
    }

    @GetMapping("/logs-today")
    public List<CaLam> getLogsToday(@RequestParam(defaultValue = "1") Integer mayPos) {
            
        java.time.LocalDateTime start = java.time.LocalDate.now().atStartOfDay();
        java.time.LocalDateTime end = java.time.LocalDate.now().atTime(java.time.LocalTime.MAX);
        
        List<CaLam> finishedShifts = caLamRepo.findByMayPosAndBatdauBetweenAndKetthucIsNotNullOrderByIdAsc(mayPos, start, end);
        List<CaLam> allShifts = new ArrayList<>(finishedShifts);

        // ĐÃ SỬA: Lấy duy nhất 1 đối tượng ca đang diễn ra mới nhất
        CaLam activeShift = caLamRepo.findTopByMayPosAndKetthucIsNullOrderByIdDesc(mayPos);
        if (activeShift != null) {
            // Kiểm tra tránh trùng lặp nếu danh sách trên bộ nhớ đệm đã chứa
            if (!allShifts.contains(activeShift)) {
                allShifts.add(activeShift);
            }
        }
        
        return allShifts;
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