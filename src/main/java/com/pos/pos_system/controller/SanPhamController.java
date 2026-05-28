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

    // API mới dành riêng cho trang bán hàng (orderPage.html)
    @GetMapping("/active")
    public List<SanPham> getAllActive() {
        return sanPhamRepo.findByIsDeletedOrderByidAsc(0);
    }

    // Giữ nguyên API cũ cho trang Quản lý (TienIchQuanLy.html)
    @GetMapping
    public List<SanPham> getAll() {
        return sanPhamRepo.findAll();
    }

    @PutMapping("/{id}")
    public SanPham update(@PathVariable Integer id, @RequestBody Map<String, Object> spDetails) {
        SanPham sp = sanPhamRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SP"));
        
        sp.setTen(spDetails.get("ten").toString());
        sp.setCategory(spDetails.get("category").toString());
        sp.setGiaban(Integer.parseInt(spDetails.get("giaban").toString()));
        sp.setTonkho(Integer.parseInt(spDetails.get("tonkho").toString()));
        
        // Cập nhật thuộc tính trạng thái kinh doanh an toàn
        if (spDetails.containsKey("is_deleted") && spDetails.get("is_deleted") != null) {
            sp.setIsDeleted(Integer.parseInt(spDetails.get("is_deleted").toString()));
        } else {
            sp.setIsDeleted(0); // Mặc định là 0 nếu trống
        }
        
        return sanPhamRepo.save(sp);
    }
}