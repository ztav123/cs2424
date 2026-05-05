package com.pos.pos_system.repository;

import com.pos.pos_system.entity.CaLam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CaLamRepository extends JpaRepository<CaLam, Integer> {
    // Spring Data JPA sẽ tự động dịch tên hàm này thành:
    // SELECT * FROM CALAM WHERE nhanvien_id = ? AND tgianketthuc IS NULL
    List<CaLam> findByNhanVienIdAndTgianKetThucIsNull(Integer nhanVienId);
}