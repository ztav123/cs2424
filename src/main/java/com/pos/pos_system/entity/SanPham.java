package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SANPHAM")
@Data
public class SanPham {
    @Id
    private Integer id;

    private String ten;
    private String category;
    private Integer giaban;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "voucherid") // Tên cột khóa ngoại trong DB
    private Voucher voucher;
}
