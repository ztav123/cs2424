package com.pos.pos_system.repository;

import com.pos.pos_system.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    
    // Spring Boot tự động hỗ trợ viết hàm tìm kiếm các giao dịch thanh toán theo ID hóa đơn
    List<ThanhToan> findByHoaDonId(Integer hoadonid);
    @Query("SELECT t FROM ThanhToan t WHERE t.hoaDon.nhanVien.id = :nvId AND t.ngayThanhToan >= :start AND t.ngayThanhToan <= :end")
    List<ThanhToan> findByNhanVienAndTime(@Param("nvId") Integer nvId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}