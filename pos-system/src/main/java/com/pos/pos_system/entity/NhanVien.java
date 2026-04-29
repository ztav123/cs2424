package com.pos.pos_system.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "NHANVIEN")
@Data
public class NhanVien {
    @Id
    private Integer id; // Khóa chính bạn tự nhập (VD: 24521487)

    private String hoten;
    private String sdt;
    private String diachi;

    @Column(name = "ngayvaolam")
    private LocalDate ngayVaoLam;

    private String mkdangnhap;
}