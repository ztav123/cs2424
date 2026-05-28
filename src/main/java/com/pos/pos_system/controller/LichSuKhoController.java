package com.pos.pos_system.controller;

import com.pos.pos_system.entity.LichSuKho;
import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.LichSuKhoRepository;
import com.pos.pos_system.repository.SanPhamRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lichsukho")
@CrossOrigin("*")
public class LichSuKhoController {

    @Autowired private LichSuKhoRepository lichSuKhoRepo;
    @Autowired private SanPhamRepository sanPhamRepo;

    // API 1: ĐIỀU CHỈNH TỒN KHO THỦ CÔNG 
    @PostMapping("/dieu-chinh")
    public ResponseEntity<?> dieuChinhKho(@RequestBody Map<String, Object> payload) {
        try {
            Integer spId = Integer.parseInt(payload.get("sanphamId").toString());
            Integer soLuongBienDong = Integer.parseInt(payload.get("soLuong").toString());
            Integer adminId = Integer.parseInt(payload.get("nguoiThucHien").toString());

            if (soLuongBienDong == 0) return ResponseEntity.badRequest().body("Số lượng điều chỉnh phải khác 0");

            LichSuKho lsk = new LichSuKho();
            lsk.setSanphamId(spId);
            lsk.setSoLuong(soLuongBienDong);
            lsk.setNguoiThucHien(adminId);
            lichSuKhoRepo.save(lsk);
            return ResponseEntity.ok(Map.of("message", "Cập nhật tồn kho thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // API 2: XEM TRƯỚC (PREVIEW) EXCEL - TÁCH DỮ LIỆU ĐỂ FRONTEND QUYẾT ĐỊNH
    @PostMapping("/preview-excel")
    public ResponseEntity<?> previewExcel(@RequestParam("file") MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Map<String, Object>> previewList = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 1. Kiểm tra ID an toàn
                Cell idCell = row.getCell(0);
                Integer id = null;
                boolean isNew = true; // Mặc định là sản phẩm mới
                
                if (idCell != null && idCell.getCellType() != CellType.BLANK) {
                    if (idCell.getCellType() == CellType.NUMERIC) {
                        id = (int) idCell.getNumericCellValue();
                    } else if (idCell.getCellType() == CellType.STRING) {
                        String idStr = idCell.getStringCellValue().trim();
                        if (!idStr.isEmpty()) id = Integer.parseInt(idStr);
                    }
                    
                    // Nếu lấy được ID từ Excel, kiểm tra xem DB có SP này chưa
                    if (id != null && sanPhamRepo.existsById(id)) {
                        isNew = false;
                    }
                }

                // 2. Đọc các cột còn lại an toàn
                String ten = row.getCell(1) != null ? row.getCell(1).getStringCellValue().trim() : "";
                String category = row.getCell(2) != null ? row.getCell(2).getStringCellValue().trim() : "";

                Cell giaCell = row.getCell(3);
                int giaBan = 0;
                if (giaCell != null) {
                    if (giaCell.getCellType() == CellType.NUMERIC) giaBan = (int) giaCell.getNumericCellValue();
                    else if (giaCell.getCellType() == CellType.STRING) {
                        String giaStr = giaCell.getStringCellValue().replaceAll("[^0-9]", "").trim();
                        giaBan = giaStr.isEmpty() ? 0 : Integer.parseInt(giaStr);
                    }
                }

                Cell tonKhoCell = row.getCell(4);
                int tonKhoExcel = 0;
                if (tonKhoCell != null) {
                    if (tonKhoCell.getCellType() == CellType.NUMERIC) tonKhoExcel = (int) tonKhoCell.getNumericCellValue();
                    else if (tonKhoCell.getCellType() == CellType.STRING) {
                        String tkStr = tonKhoCell.getStringCellValue().replaceAll("[^0-9\\-]", "").trim(); 
                        tonKhoExcel = tkStr.isEmpty() ? 0 : Integer.parseInt(tkStr);
                    }
                }

                // 3. Đóng gói dòng thành Map
                Map<String, Object> rowData = new HashMap<>();
                rowData.put("id", id);
                rowData.put("ten", ten);
                rowData.put("category", category);
                rowData.put("giaban", giaBan);
                rowData.put("tonkho", tonKhoExcel);
                rowData.put("isNew", isNew);
                
                previewList.add(rowData);
            }
            return ResponseEntity.ok(previewList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi định dạng file: " + e.getMessage()));
        }
    }
}