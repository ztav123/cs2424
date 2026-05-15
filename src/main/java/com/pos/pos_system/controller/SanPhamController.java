package com.pos.pos_system.controller;

import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sanpham")
@CrossOrigin("*") 
public class SanPhamController {

    @Autowired
    private SanPhamRepository repo;

    // Lấy toàn bộ sản phẩm
    @GetMapping
    public List<SanPham> getAll() {
        return repo.findAll();
    }

    // Thêm sản phẩm mới
    @PostMapping
    public SanPham create(@RequestBody SanPham sp) {
        return repo.save(sp);
    }

    // Cập nhật hoặc Xóa mềm (Set tồn kho = 0)
    @PutMapping("/{id}")
    public SanPham update(@PathVariable Integer id, @RequestBody SanPham spDetails) {
        SanPham sp = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy SP"));
        sp.setTen(spDetails.getTen());
        sp.setCategory(spDetails.getCategory());
        sp.setGiaban(spDetails.getGiaban());
        sp.setTonkho(spDetails.getTonkho());
        return repo.save(sp);
    }
}