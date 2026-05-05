package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CALAM")
@Data
public class CaLam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Khóa ngoại liên kết với bảng NHANVIEN
    @ManyToOne
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanVien;

    private LocalDate ngaylam;

    @Column(name = "tgianbatdau")
    private LocalTime tgianBatDau;

    @Column(name = "tgianketthuc")
    private LocalTime tgianKetThuc;
}