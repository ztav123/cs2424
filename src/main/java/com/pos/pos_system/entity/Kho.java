package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "KHO")
@Data
public class Kho {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Cột này lưu ID của sản phẩm, tương ứng với khóa ngoại fk_kho_sp trong cơ sở dữ liệu
    @Column(name = "sanphamid", unique = true)
    private Integer sanphamid;

    // Số lượng tồn kho hiện tại của sản phẩm đó
    private Integer tonkho;
}