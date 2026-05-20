package com.pos.pos_system.repository;

import com.pos.pos_system.entity.CTHD;
import com.pos.pos_system.entity.CTHDid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CTHDRepository extends JpaRepository<CTHD, CTHDid> {
    // Sửa thành tên này để JPA tự hiểu: Lấy CTHD dựa vào trường 'id' bên trong thực thể 'hoadon'
    List<CTHD> findByHoadon_Id(String hoadonId);
}