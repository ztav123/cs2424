package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CALAM")
@Data
public class CaLam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanVien;

    @Column(name = "may_pos")
    private Integer mayPos;

    private LocalDateTime batdau;
    private LocalDateTime ketthuc;

    @Column(name = "tienmat_bandau")
    private Double tienmatBandau;

    @Column(name = "tienmat_ketca")
    private Double tienmatKetca;

    @Column(name = "tien_nganhang")
    private Double tienNganhang;

    @Column(name = "tien_mat")
    private Double tienMat;

    @Column(name = "tong_doanhthu")
    private Double tongDoanhthu;

    @Column(name = "tongtien_thucte") // Cột mới thêm
    private Double tongtienThucte;

    private String ghichu;
}