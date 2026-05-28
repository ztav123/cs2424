package com.pos.pos_system.repository;

import com.pos.pos_system.entity.CaLam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;
@Repository
public interface CaLamRepository extends JpaRepository<CaLam, Integer> {
    // Lấy ca đang mở mới nhất duy nhất của máy POS
    CaLam findTopByMayPosAndKetthucIsNullOrderByIdDesc(Integer mayPos);
    @Procedure(procedureName = "PROC_KET_CA")
    void thucHienKetCa(@Param("p_calam_id") Integer calamId, 
                       @Param("p_tienmat_ketca") Double tienMat, 
                       @Param("p_ghichu") String ghiChu);
    List<CaLam> findByNhanVienIdAndKetthucIsNull(Integer nhanVienId);
    List<CaLam> findByMayPosAndBatdauBetweenAndKetthucIsNotNullOrderByIdAsc(Integer mayPos, LocalDateTime start, LocalDateTime end);
    CaLam findTopByNhanVienIdOrderByBatdauDesc(Integer nhanVienId);
    CaLam findTopByOrderByBatdauDesc();
}