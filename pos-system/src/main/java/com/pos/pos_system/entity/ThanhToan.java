package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "THANHTOAN") // Viết hoa để khớp chuẩn với Database
@Data
public class ThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khai báo ID tự động tăng
    private Integer id;

    // Khóa ngoại liên kết với bảng HOADON
    @ManyToOne
    @JoinColumn(name = "hoadonid")
    private HoaDon hoaDon;

    private String phuongthuc;

    @Column(name = "ngaythanhtoan")
    private LocalDateTime ngayThanhToan;

    private Integer sotien;
}