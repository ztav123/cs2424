package com.pos.pos_system.repository;
import com.pos.pos_system.entity.QuanLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuanLyRepository extends JpaRepository<QuanLy, Integer> {
}