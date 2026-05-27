/*
===============================================================================
FILE: 02_script_quan_sat_an_toan.sql
MUC DICH:
  - Cac cau lenh an toan de quan sat lock/transaction sau khi chay app.
  - Khong tao loi co chu dich cho thay xem.
===============================================================================
*/

-- 1. Quan sat hoa don dang cho thanh toan, hoan thanh, that bai.
SELECT trangthai, COUNT(*) AS so_luong, NVL(SUM(tongtien), 0) AS tong_tien
FROM HOADON
GROUP BY trangthai
ORDER BY trangthai;

-- 2. Quan sat ton kho va lich su kho gan nhat.
SELECT id, ten, tonkho, giaban, is_deleted
FROM SANPHAM
ORDER BY id;

SELECT id, sanpham_id, loai_phieu, so_luong, tonkho_sau, nguoi_thuc_hien, ngay_tao, ghi_chu
FROM LICH_SU_KHO
ORDER BY ngay_tao DESC;

-- 3. Quan sat doanh thu theo ca.
SELECT c.id,
       c.nhanvien_id,
       c.may_pos,
       c.batdau,
       c.ketthuc,
       c.tien_mat,
       c.tien_nganhang,
       c.tong_doanhthu,
       FN_DOANH_THU_CA(c.id) AS doanh_thu_tinh_lai_tu_hoadon
FROM CALAM c
ORDER BY c.id DESC;

-- 4. Goi procedure bao cao cursor neu can thuyet minh cursor.
SET SERVEROUTPUT ON;
BEGIN
    PROC_BAO_CAO_DOANH_THU_CA;
END;
/
