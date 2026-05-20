package com.pos.pos_system.repository;

import com.pos.pos_system.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    // Lọc theo cả nhân viên và khoảng thời gian
    List<HoaDon> findByNhanVienIdAndNgaylapBetween(Integer nhanVienId, LocalDateTime start, LocalDateTime end);
    
    // Lọc CHỈ theo khoảng thời gian (Dành cho chức năng "Tất cả nhân viên")
    List<HoaDon> findByNgaylapBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(h.tongtien), 0) FROM HoaDon h WHERE h.ngaylap BETWEEN :from AND :to AND (:nvId IS NULL OR h.nhanVien.id = :nvId)")
    Double tinhTongDoanhThu(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("nvId") Integer nvId);

    // Đếm số đơn hàng trực tiếp dưới DB
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.ngaylap BETWEEN :from AND :to AND (:nvId IS NULL OR h.nhanVien.id = :nvId)")
    Long demSoDonHang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("nvId") Integer nvId);
    // Tìm sản phẩm bán chạy nhất bằng Query thuần túy (Tuyệt đối an toàn cho Oracle 12c+)
    @Query(value = "SELECT sp.ten FROM CTHD ct JOIN SANPHAM sp ON ct.sanpham_id = sp.id " +
                   "JOIN HOADON hd ON ct.hoadon_id = hd.id " +
                   "WHERE hd.ngaylap BETWEEN :from AND :to AND (:nvId IS NULL OR hd.nhanvien_id = :nvId) " +
                   "GROUP BY sp.ten ORDER BY SUM(ct.soluong) DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    String findSpBanChayNhat(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("nvId") Integer nvId);
    // ... code cũ
    // Tìm các hóa đơn có trạng thái X và được lập trước một mốc thời gian Y
    List<HoaDon> findByTrangthaiAndNgaylapBefore(String trangthai, java.time.LocalDateTime time);
}