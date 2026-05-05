package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "KHACHHANG")
@Data
public class KhachHang {
    @Id
    private String sdt; // Khóa chính là số điện thoại

    private String ten;

    @Column(name = "ngaylap")
    private LocalDate ngayLap;

    private Integer diem;
}