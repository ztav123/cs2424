package com.pos.pos_system.service;

import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Báo cho Spring biết đây là lớp chứa logic nghiệp vụ, Spring sẽ tự động tạo một phiên bản (Bean) của lớp này.
public class SanPhamService {

    @Autowired // Tự động "tiêm" (inject) SanPhamRepository vào đây để sử dụng mà không cần dùng từ khóa 'new'.
    private SanPhamRepository sanPhamRepository;

    // Hàm lấy toàn bộ danh sách sản phẩm để hiển thị lên máy POS
    public List<SanPham> layTatCaSanPham() {
        return sanPhamRepository.findAll();
    }

    // Hàm tìm sản phẩm theo ID (khi thu ngân quét mã vạch)
    public SanPham timSanPhamTheoId(Integer id) {
        // Dùng orElse(null) để nếu không tìm thấy ID đó trong database, hệ thống sẽ trả về null thay vì báo lỗi.
        return sanPhamRepository.findById(id).orElse(null); 
    }
}