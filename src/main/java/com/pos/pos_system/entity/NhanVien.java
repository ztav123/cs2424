package com.pos.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NHANVIEN")
@Data
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private String hoten;
    private String sdt;
    private String mkdangnhap;

    // Thêm các cột quản lý phân quyền mới
    private String vaitro; 
    private Integer trangthai;
    
    // ĐÃ XÓA: diachi, ngayVaoLam (để hệ thống tinh gọn)
}