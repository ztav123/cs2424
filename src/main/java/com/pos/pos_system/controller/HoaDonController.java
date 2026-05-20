package com.pos.pos_system.controller;

import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin("*")
public class HoaDonController {

    @Autowired
    private HoaDonRepository hoadonRepo;

    // Thay thế hàm getAll() cũ bằng hàm phân trang này
    @GetMapping
    public ResponseEntity<Page<HoaDon>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        // Sắp xếp hóa đơn mới nhất lên đầu trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngaylap").descending());
        Page<HoaDon> hoaDonPage = hoadonRepo.findAll(pageable);
        
        return ResponseEntity.ok(hoaDonPage);
    }

    @GetMapping("/{id}")
    public HoaDon getById(@PathVariable String id) {
        return hoadonRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
    }
}