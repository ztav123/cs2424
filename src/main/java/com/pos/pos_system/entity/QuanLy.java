package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "QUANLY")
@Data
public class QuanLy {
    @Id
    private Integer id; 
    private String hoten;
    private String mkdangnhap;
}