package com.pos.pos_system.controller;

import com.pos.pos_system.entity.LichSuKho;
import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.LichSuKhoRepository;
import com.pos.pos_system.repository.SanPhamRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/lichsukho")
@CrossOrigin("*")
public class LichSuKhoController {

    @Autowired private LichSuKhoRepository lichSuKhoRepo;
    @Autowired private SanPhamRepository sanPhamRepo;

    // API 1: ĐIỀU CHỈNH TỒN KHO THỦ CÔNG TỪ MODAL GIAO DIỆN
    @PostMapping("/dieu-chinh")
    public ResponseEntity<?> dieuChinhKho(@RequestBody Map<String, Object> payload) {
        try {
            Integer spId = Integer.parseInt(payload.get("sanphamId").toString());
            Integer soLuongBienDong = Integer.parseInt(payload.get("soLuong").toString());
            Integer adminId = Integer.parseInt(payload.get("nguoiThucHien").toString());

            if (soLuongBienDong == 0) return ResponseEntity.badRequest().body("Số lượng điều chỉnh phải khác 0");

            // Chỉ cần Insert vào LichSuKho, Trigger dưới Oracle sẽ lo việc UPDATE bảng SANPHAM
            LichSuKho lsk = new LichSuKho();
            lsk.setSanphamId(spId);
            lsk.setSoLuong(soLuongBienDong);
            lsk.setNguoiThucHien(adminId);
            // Để loại phiếu Null để Trigger tự detect Admin -> "NHAP_KHO" theo ý tưởng của bạn
            
            lichSuKhoRepo.save(lsk);
            return ResponseEntity.ok(Map.of("message", "Cập nhật tồn kho thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // API 2: UPLOAD EXCEL (TẠO MỚI & CẬP NHẬT TỒN KHO ĐỒNG LOẠT)
    // =========================================================================
    // API UPLOAD EXCEL - PHIÊN BẢN CHỐNG SAI ĐỊNH DẠNG Ô (FIX LỖI ORA / CELL STRING)
    // =========================================================================
    @PostMapping("/upload-excel")
    @Transactional
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("quanlyId") Integer quanlyId) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            int countNew = 0, countUpdate = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 1. Đọc cột ID an toàn (Chấp nhận cả định dạng số hoặc chuỗi chữ)
                Cell idCell = row.getCell(0);
                Integer id = null;
                boolean isIdBlank = true;
                
                if (idCell != null && idCell.getCellType() != CellType.BLANK) {
                    isIdBlank = false;
                    if (idCell.getCellType() == CellType.NUMERIC) {
                        id = (int) idCell.getNumericCellValue();
                    } else if (idCell.getCellType() == CellType.STRING) {
                        String idStr = idCell.getStringCellValue().trim();
                        if (!idStr.isEmpty()) {
                            id = Integer.parseInt(idStr);
                        } else {
                            isIdBlank = true;
                        }
                    }
                }

                // 2. Đọc cột Tên (1) và Danh mục (2) an toàn
                String ten = row.getCell(1) != null ? row.getCell(1).getStringCellValue().trim() : "SP Không Tên";
                String category = row.getCell(2) != null ? row.getCell(2).getStringCellValue().trim() : "Khác";

                // 3. ĐÃ KHẮC PHỤC CHÍ MẠNG: Đọc cột Giá Bán (3) chấp nhận cả ô Text lẫn ô Number
                Cell giaCell = row.getCell(3);
                int giaBan = 0;
                if (giaCell != null) {
                    if (giaCell.getCellType() == CellType.NUMERIC) {
                        giaBan = (int) giaCell.getNumericCellValue();
                    } else if (giaCell.getCellType() == CellType.STRING) {
                        // Nếu lỡ để định dạng Text, tự động dọn sạch rác (khoảng trắng, chữ đ) rồi ép kiểu
                        String giaStr = giaCell.getStringCellValue().replaceAll("[^0-9]", "").trim();
                        giaBan = giaStr.isEmpty() ? 0 : Integer.parseInt(giaStr);
                    }
                }

                // 4. ĐÃ KHẮC PHỤC CHÍ MẠNG: Đọc cột Tồn Kho (4) chấp nhận cả ô Text lẫn ô Number
                Cell tonKhoCell = row.getCell(4);
                int tonKhoExcel = 0;
                if (tonKhoCell != null) {
                    if (tonKhoCell.getCellType() == CellType.NUMERIC) {
                        tonKhoExcel = (int) tonKhoCell.getNumericCellValue();
                    } else if (tonKhoCell.getCellType() == CellType.STRING) {
                        // Tự động bóc tách số từ chuỗi chữ
                        String tkStr = tonKhoCell.getStringCellValue().replaceAll("[^0-9\\-]", "").trim(); // Giữ lại dấu trừ nếu có trừ kho
                        tonKhoExcel = tkStr.isEmpty() ? 0 : Integer.parseInt(tkStr);
                    }
                }

                // =============================================================
                // TIẾN HÀNH ĐIỀU HƯỚNG LƯU TRỮ XUỐNG DATABASE
                // =============================================================
                if (isIdBlank || id == null) {
                    // TRƯỜNG HỢP 1: TẠO MỚI SẢN PHẨM
                    SanPham spMoi = new SanPham();
                    spMoi.setTen(ten);
                    spMoi.setCategory(category);
                    spMoi.setGiaban(giaBan);
                    spMoi.setTonkho(tonKhoExcel);
                    sanPhamRepo.save(spMoi);
                    countNew++;
                } else {
                    // TRƯỜNG HỢP 2: CẬP NHẬT SẢN PHẨM CÓ SẴN
                    SanPham spCu = sanPhamRepo.findById(id).orElse(null);
                    if (spCu != null) {
                        spCu.setTen(ten);
                        spCu.setCategory(category);
                        spCu.setGiaban(giaBan);
                        spCu.setTonkho(tonKhoExcel); 
                        sanPhamRepo.save(spCu);

                        // Ghi log lịch sử kho
                        LichSuKho lsk = new LichSuKho();
                        lsk.setSanphamId(id);
                        lsk.setLoaiPhieu("NHAP_EXCEL");
                        lsk.setSoLuong(tonKhoExcel); 
                        lsk.setTonkhoSau(tonKhoExcel);
                        lsk.setNguoiThucHien(quanlyId);
                        lsk.setGhiChu("Đồng bộ kho từ file Excel");
                        lichSuKhoRepo.save(lsk);
                        countUpdate++;
                    }
                }
            }
            return ResponseEntity.ok(Map.of("message", "Đồng bộ thành công! Tạo mới: " + countNew + " SP. Cập nhật kho: " + countUpdate + " SP."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi xử lý file dữ liệu: " + e.getMessage()));
        }
    }
}