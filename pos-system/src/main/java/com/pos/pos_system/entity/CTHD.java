package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CTHD")
@IdClass(CTHDid.class) // Kết nối với class định danh ở trên
@Data
public class CTHD {
    @Id
    private Integer hoadonid;

    @Id
    private Integer sanphamid;

    private Integer soluong;
    private Integer dongia;
}