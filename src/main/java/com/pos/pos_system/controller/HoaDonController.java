package com.pos.pos_system.controller;

import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin("*")
public class HoaDonController {

    @Autowired
    private HoaDonRepository hoadonRepo;

    // Lấy toàn bộ danh sách hóa đơn để hiển thị ở bảng tiện ích
    @GetMapping
    public List<HoaDon> getAll() {
        return hoadonRepo.findAll();
    }

    // Lấy chi tiết một hóa đơn theo ID
    @GetMapping("/{id}")
    public HoaDon getById(@PathVariable String id) {
        return hoadonRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
    }
}