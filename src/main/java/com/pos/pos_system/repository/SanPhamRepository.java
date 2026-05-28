package com.pos.pos_system.repository;

import com.pos.pos_system.entity.SanPham;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM SanPham s WHERE s.id IN :ids")
    List<SanPham> findByIdsForUpdate(@Param("ids") List<Integer> ids);
    List<SanPham> findByCategory(String category);
    @Transactional(readOnly = true)
    @Query("SELECT s FROM SanPham s WHERE s.isDeleted = :isDeleted order by s.id asc")
     List<SanPham> findByIsDeletedOrderByidAsc(@Param("isDeleted") Integer isDeleted);
}