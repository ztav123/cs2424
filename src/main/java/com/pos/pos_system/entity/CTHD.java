package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CTHD")
@IdClass(CTHDid.class) 
@Data
public class CTHD {
    
    @Id
    @Column(name = "hoadon_id")
    private String hoadonid;

    @Id
    @Column(name = "sanpham_id") // BỔ SUNG DÒNG NÀY
    private Integer sanphamid;

    private Integer soluong;
    private Integer dongia;
}