package com.pos.pos_system.repository;

import com.pos.pos_system.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    // Thêm hàm này vào:
    List<HoaDon> findByNhanVienIdAndNgaylapBetween(Integer nhanVienId, LocalDateTime start, LocalDateTime end);
}