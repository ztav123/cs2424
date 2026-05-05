package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "KETCA_LOG")
@Data
public class KetCaLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanVien;

    private LocalDateTime thoigian;
    private Integer tiencannop;
    private Integer tienmat;
    private Integer tiennganhang;
    private Integer tongtien;
}