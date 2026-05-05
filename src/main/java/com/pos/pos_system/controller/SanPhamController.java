package com.pos.pos_system.controller;

import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Đánh dấu lớp này là API Controller, mọi dữ liệu trả về sẽ tự động chuyển thành chuỗi JSON.
@RequestMapping("/api/sanpham") // Khai báo đường dẫn gốc cho mọi API trong lớp này.
@CrossOrigin("*") // Rất quan trọng: Cho phép file HTML chạy ở localhost gọi được API này mà không bị lỗi bảo mật trình duyệt (CORS).
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    // Khi Frontend gọi GET http://localhost:8080/api/sanpham, hàm này sẽ chạy
    @GetMapping
    public List<SanPham> getDanhSachSanPham() {
        return sanPhamService.layTatCaSanPham();
    }

    // Khi Frontend gọi GET http://localhost:8080/api/sanpham/123 (quét mã vạch 123), hàm này sẽ chạy
    @GetMapping("/{id}")
    public SanPham getChiTietSanPham(@PathVariable Integer id) {
        return sanPhamService.timSanPhamTheoId(id);
    }
}