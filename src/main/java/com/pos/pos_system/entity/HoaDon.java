package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    private LocalDateTime ngaylap;
    private Double tongtien;
    private Double giamgia;

    @Column(name = "phuongthuc_tt")
    private String phuongthucTt;
    private String trangthai;
}