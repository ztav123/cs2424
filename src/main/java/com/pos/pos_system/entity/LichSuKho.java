package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "LICH_SU_KHO")
@Data
public class LichSuKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sanpham_id")
    private Integer sanphamId;

    @Column(name = "loai_phieu")
    private String loaiPhieu;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "tonkho_sau")
    private Integer tonkhoSau;

    @Column(name = "nguoi_thuc_hien")
    private Integer nguoiThucHien;

    @Column(name = "ngay_tao", insertable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ghi_chu")
    private String ghiChu;
}