package com.pos.pos_system.repository;

import com.pos.pos_system.entity.KetCaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KetCaLogRepository extends JpaRepository<KetCaLog, Integer> {
    // Truy vấn tất cả các lần kết ca trong một khoảng thời gian (Từ đầu ngày đến cuối ngày)
    List<KetCaLog> findByThoigianBetween(LocalDateTime start, LocalDateTime end);
    KetCaLog findTopByNhanVienIdOrderByThoigianDesc(Integer nhanVienId);
}