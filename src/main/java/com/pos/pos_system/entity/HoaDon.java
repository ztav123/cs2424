package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOADON")
@Data
public class HoaDon {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "khachhang_sdt")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "nhanvienid")
    private NhanVien nhanVien;

    @Column(name = "ngaylap")
    private LocalDateTime ngayLap;

    private String trangthai;
    private Integer tongtien;
}