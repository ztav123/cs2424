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
    @JoinColumn(name = "khachhang_id", referencedColumnName = "id")
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
    
    @OneToMany(mappedBy = "hoadon", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CTHD> chiTiet;
}