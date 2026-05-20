package com.pos.pos_system.repository;

import com.pos.pos_system.entity.CaLam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
@Repository
public interface CaLamRepository extends JpaRepository<CaLam, Integer> {
    // Thêm 2 hàm này vào:
    List<CaLam> findByNhanVienIdAndKetthucIsNull(Integer nhanVienId);
    List<CaLam> findByBatdauBetweenAndKetthucIsNotNullOrderByIdAsc(LocalDateTime start, LocalDateTime end);
    CaLam findTopByNhanVienIdOrderByBatdauDesc(Integer nhanVienId);
    CaLam findTopByOrderByBatdauDesc();
}