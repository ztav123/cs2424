/*
===============================================================================
FILE: 01_select_sau_khi_chay_app.sql
MUC DICH:
  - Chi dung SELECT de quan sat du lieu sau khi demo app Java.
  - Khong INSERT/UPDATE thu cong.
===============================================================================
*/

-- 1. Hoa don moi nhat phat sinh tu app.
SELECT id, nhanvien_id, khachhang_id, calam_id, ngaylap, tongtien, giamgia, phuongthuc_tt, trangthai
FROM HOADON
ORDER BY ngaylap DESC;

-- 2. Chi tiet hoa don moi nhat.
SELECT ct.hoadon_id, ct.sanpham_id, sp.ten, ct.soluong, ct.dongia, ct.soluong * ct.dongia AS thanh_tien
FROM CTHD ct
JOIN SANPHAM sp ON sp.id = ct.sanpham_id
ORDER BY ct.hoadon_id DESC, ct.sanpham_id;

-- 3. Ton kho hien tai sau khi ban/nhap/hoan kho.
SELECT id, ten, category, giaban, tonkho, is_deleted
FROM SANPHAM
ORDER BY id;

-- 4. Lich su bien dong kho do trigger/procedure ghi nhan.
SELECT id, sanpham_id, loai_phieu, so_luong, tonkho_sau, nguoi_thuc_hien, ngay_tao, ghi_chu
FROM LICH_SU_KHO
ORDER BY ngay_tao DESC;

-- 5. Ca lam va doanh thu ca.
SELECT id, nhanvien_id, may_pos, batdau, ketthuc, tienmat_bandau, tienmat_ketca,
       tien_mat, tien_nganhang, tong_doanhthu, tongtien_thucte, ghichu
FROM CALAM
ORDER BY id DESC;

-- 6. Khach hang va diem tich luy.
SELECT id, sdt, hoten, diemtichluy, ngaytao
FROM KHACHHANG
ORDER BY id DESC;

-- 7. Audit log neu da chay file recovery/audit.
SELECT *
FROM AUDIT_LOG
ORDER BY thoi_gian DESC;

-- 8. Recovery log mo phong neu da chay file recovery/audit.
SELECT *
FROM DBMS_RECOVERY_LOG
ORDER BY thoi_gian DESC;
