package com.pos.pos_system.repository;

import com.pos.pos_system.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    // Lọc theo cả nhân viên và khoảng thời gian
    List<HoaDon> findByNhanVienIdAndNgaylapBetween(Integer nhanVienId, LocalDateTime start, LocalDateTime end);
    
    // Lọc CHỈ theo khoảng thời gian (Dành cho chức năng "Tất cả nhân viên")
    List<HoaDon> findByNgaylapBetween(LocalDateTime start, LocalDateTime end);
}