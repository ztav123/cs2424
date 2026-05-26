package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SANPHAM")
@Data
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ten;
    private String category;
    private Integer giaban;

    

    private Integer tonkho;
    public Integer getTonkho() {
        return this.tonkho != null ? this.tonkho : 0;
    }

    public void setTonkho(Integer tonkho) {
        this.tonkho = tonkho;
    }

    private String imageurl;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
