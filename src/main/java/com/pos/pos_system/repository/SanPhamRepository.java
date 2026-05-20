package com.pos.pos_system.repository;

import com.pos.pos_system.entity.SanPham;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    
    // 1. Áp dụng khóa bi quan (Khóa ghi)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // 2. Chống treo Database: Nếu chờ quá 3000ms (3 giây) mà không lấy được Lock thì ném lỗi
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM SanPham s WHERE s.id IN :ids")
    List<SanPham> findByIdsForUpdate(@Param("ids") List<Integer> ids);
    List<SanPham> findByCategory(String category);
}