package com.pos.pos_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CTHD")
@IdClass(CTHDid.class) 
@Data
public class CTHD {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "hoadon_id", referencedColumnName = "id")
    @JsonIgnore
    private HoaDon hoadon;

    @Id
    @ManyToOne
    @JoinColumn(name = "sanpham_id", referencedColumnName = "id")
    private SanPham sanPham;

    @Column(name = "soluong")
    private Integer soLuong;

    @Column(name = "dongia")
    private Double donGia; // Chuyển từ Integer sang Double để đồng bộ kiểu số với cột tongtien của HoaDon
}