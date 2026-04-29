package com.pos.pos_system.repository;

import com.pos.pos_system.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    
    // Spring Boot tự động hỗ trợ viết hàm tìm kiếm các giao dịch thanh toán theo ID hóa đơn
    List<ThanhToan> findByHoaDonId(Integer hoadonid);
}