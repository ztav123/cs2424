package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "HOADON")
@Data
public class HoaDon {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "khach_sdt") 
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "nhanvien_id") 
    private NhanVien nhanVien;

    @Column(name = "ngaylap")
    private LocalDateTime ngaylap;

    @Column(name = "tongtien")
    private Long tongtien;

    @Column(name = "giamgia")
    private Double giamgia;

    @Column(name = "phuongthuc_tt")
    private String phuongthucTt;

    @Column(name = "trangthai")
    private String trangthai;

    // =========================================================================
    // BỔ SUNG QUAN TRỌNG: Để gọi được hd.getChiTiet() xuất món ăn ra file PDF
    // =========================================================================
    @OneToMany(mappedBy = "hoadon", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CTHD> chiTiet;
}