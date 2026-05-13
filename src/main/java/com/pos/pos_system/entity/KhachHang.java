package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "KHACHHANG")
@Data
public class KhachHang {
    @Id
    private String sdt; 

    @Column(name = "hoten") // Đổi ten -> hoten
    private String hoten;

    @Column(name = "diemtichluy") // Đổi diem -> diemtichluy
    private Integer diemtichluy;

    private LocalDate ngaytao; // Đổi ngayLap -> ngaytao
}