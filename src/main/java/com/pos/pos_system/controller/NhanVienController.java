package com.pos.pos_system.controller;

import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin("*")
public class NhanVienController {

    @Autowired
    private NhanVienRepository repo;

    @GetMapping
    public List<NhanVien> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public NhanVien create(@RequestBody NhanVien nv) {
        // Mặc định nhân viên mới là Đang làm (1), MK mặc định là 123456 (Nên băm MK sau này)
        if (nv.getTrangthai() == null) nv.setTrangthai(1);
        if (nv.getMkdangnhap() == null) nv.setMkdangnhap("123456"); 
        return repo.save(nv);
    }

    @PutMapping("/{id}")
    public NhanVien update(@PathVariable Integer id, @RequestBody NhanVien nvDetails) {
        NhanVien nv = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy NV"));
        nv.setHoten(nvDetails.getHoten());
        nv.setSdt(nvDetails.getSdt());
        nv.setVaitro(nvDetails.getVaitro());
        nv.setTrangthai(nvDetails.getTrangthai()); // Cập nhật trạng thái nghỉ việc ở đây
        return repo.save(nv);
    }
}