package com.pos.pos_system.repository;

import com.pos.pos_system.entity.Kho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoRepository extends JpaRepository<Kho, Integer> {
    
    /**
     * Hàm này được Spring Boot tự động dịch thành câu lệnh SQL:
     * SELECT * FROM KHO WHERE sanphamid = ?
     * * Rất quan trọng cho logic thanh toán: Giúp tìm ra đúng dòng tồn kho của 1 sản phẩm cụ thể 
     * để tiến hành trừ số lượng (tonkho = tonkho - soluong) khi khách mua hàng.
     */
    Kho findBySanphamid(Integer sanphamid);
    
}