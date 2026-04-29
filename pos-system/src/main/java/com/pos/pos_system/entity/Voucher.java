package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "VOUCHER")
@Data
public class Voucher {
    
    @Id
    private Integer id;

    @Column(name = "ngaybatdau")
    private LocalDate ngayBatDau;

    @Column(name = "ngayketthuc")
    private LocalDate ngayKetThuc;

    @Column(name = "giatrigiam")
    private Integer giaTriGiam;

    private String dieukien;
}