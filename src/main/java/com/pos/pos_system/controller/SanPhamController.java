package com.pos.pos_system.controller;

import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;   

@RestController
@RequestMapping("/api/sanpham")
@CrossOrigin("*") 
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepo;

    // API dành riêng cho trang bán hàng
    @GetMapping("/active")
    public List<SanPham> getAllActive() {
        return sanPhamRepo.findByIsDeletedOrderByidAsc(0);
    }

    // Lấy toàn bộ danh sách cho trang Quản lý
    @GetMapping
    public List<SanPham> getAll() {
        return sanPhamRepo.findAll();
    }

    // LUỒNG 1: TẠO MỚI (Thêm thẳng vào bảng SANPHAM, set tồn kho ban đầu)
    @PostMapping
    public SanPham create(@RequestBody Map<String, Object> payload) {
        SanPham sp = new SanPham();
        sp.setTen(payload.get("ten").toString());
        sp.setCategory(payload.get("category").toString());
        sp.setGiaban(Integer.parseInt(payload.get("giaban").toString()));
        
        if (payload.containsKey("tonkho") && payload.get("tonkho") != null && !payload.get("tonkho").toString().isEmpty()) {
            sp.setTonkho(Integer.parseInt(payload.get("tonkho").toString()));
        } else {
            sp.setTonkho(0);
        }
        
        sp.setIsDeleted(0); // Mặc định kinh doanh
        return sanPhamRepo.save(sp);
    }

    // LUỒNG 2: CẬP NHẬT (Chỉ cập nhật thông tin, KHÔNG đụng chạm tồn kho)
    @PutMapping("/{id}")
    public SanPham update(@PathVariable Integer id, @RequestBody Map<String, Object> spDetails) {
        SanPham sp = sanPhamRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SP"));
        
        sp.setTen(spDetails.get("ten").toString());
        sp.setCategory(spDetails.get("category").toString());
        sp.setGiaban(Integer.parseInt(spDetails.get("giaban").toString()));
        
        // BỎ QUA việc setTonkho ở đây. Tồn kho sẽ do LICH_SU_KHO và Trigger dưới Database xử lý
        
        // Cập nhật trạng thái kinh doanh an toàn
        if (spDetails.containsKey("is_deleted") && spDetails.get("is_deleted") != null) {
            sp.setIsDeleted(Integer.parseInt(spDetails.get("is_deleted").toString()));
        } else {
            sp.setIsDeleted(0);
        }
        
        return sanPhamRepo.save(sp);
    }
}