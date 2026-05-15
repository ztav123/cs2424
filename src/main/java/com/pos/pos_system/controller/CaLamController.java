package com.pos.pos_system.controller;

import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.repository.CaLamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calam")
@CrossOrigin("*")
public class CaLamController {

    @Autowired
    private CaLamRepository repo;

    @GetMapping
    public List<CaLam> getAll() {
        return repo.findAll();
    }

    // Nhân viên bắt đầu ca
    @PostMapping
    public CaLam create(@RequestBody CaLam caLam) {
        caLam.setBatdau(java.time.LocalDateTime.now());
        return repo.save(caLam);
    }

    // Kết ca (Cập nhật tiền, thời gian kết thúc)
    @PutMapping("/{id}")
    public CaLam endShift(@PathVariable Integer id, @RequestBody CaLam details) {
        CaLam caLam = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Ca"));
        caLam.setKetthuc(java.time.LocalDateTime.now());
        caLam.setTienmatKetca(details.getTienmatKetca());
        caLam.setTienNganhang(details.getTienNganhang());
        caLam.setTongDoanhthu(details.getTongDoanhthu());
        caLam.setTongtienThucte(details.getTongtienThucte());
        caLam.setGhichu(details.getGhichu());
        return repo.save(caLam);
    }
}