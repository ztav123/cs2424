package com.pos.pos_system.controller;

import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.entity.KhachHang;
import com.pos.pos_system.entity.Kho;
import com.pos.pos_system.repository.SanPhamRepository;
import com.pos.pos_system.repository.KhachHangRepository;
import com.pos.pos_system.repository.KhoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@CrossOrigin("*")
public class StoreController {
    @Autowired
    private KhoRepository khoRepo;
    
    @Autowired
    private SanPhamRepository sanPhamRepo;
    
    @Autowired
    private KhachHangRepository khachHangRepo;

    // Lấy danh sách sản phẩm
    @GetMapping("/sanpham")
    public List<SanPham> getAllProducts() {
        return sanPhamRepo.findAll();
    }
    @GetMapping("/kho")
    public List<Kho> getAllKho() {
        return khoRepo.findAll();
    }
    // Tạo khách hàng mới
    @PostMapping("/khachhang")
    public ResponseEntity<?> createKhachHang(@RequestBody KhachHang khachHang) {
        // Đặt mặc định ngày lập là hôm nay và điểm = 0
        khachHang.setNgayLap(java.time.LocalDate.now());
        khachHang.setDiem(0);
        
        KhachHang saved = khachHangRepo.save(khachHang);
        return ResponseEntity.ok(saved);
    }

    // Tìm khách hàng qua SĐT
    @GetMapping("/khachhang/{sdt}")
    public ResponseEntity<?> getKhachHang(@PathVariable String sdt) {
        KhachHang kh = khachHangRepo.findById(sdt).orElse(null);
        if (kh != null) return ResponseEntity.ok(kh);
        return ResponseEntity.notFound().build();
    }
}