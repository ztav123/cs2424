package com.pos.pos_system.repository;

import com.pos.pos_system.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    
    Optional<KhachHang> findBySdt(String sdt);
}