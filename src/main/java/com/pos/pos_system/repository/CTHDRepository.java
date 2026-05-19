package com.pos.pos_system.repository;

import com.pos.pos_system.entity.CTHD;
import com.pos.pos_system.entity.CTHDid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CTHDRepository extends JpaRepository<CTHD, CTHDid> {
    List<CTHD> findByHoadonid(String hoadonid);
}