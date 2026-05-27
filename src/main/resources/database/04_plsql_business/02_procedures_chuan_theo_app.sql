/*
===============================================================================
FILE: 02_procedures_chuan_theo_app.sql
MUC DICH:
  - Procedure PL/SQL xu ly cac luong nghiep vu quan trong cua POS.
  - Dung dung 7 bang/cot goc cua app, khong them cot vao schema goc.
===============================================================================
*/

/*
PROCEDURE: PROC_CAP_NHAT_TRANG_THAI_HD
LOI/NGUY CO XU LY:
  - Race condition: VNPay return, job huy don, quan ly cap nhat hoa don cung luc.
  - Neu doc trang thai roi update bang 2 buoc rieng, transaction ghi sau co the de len transaction truoc.
GIAI PHAP:
  - Conditional update: UPDATE ... WHERE id = ? AND trangthai = trangthai_cu.
  - Neu SQL%ROWCOUNT = 0 nghia la hoa don da duoc giao dich khac xu ly.
*/
CREATE OR REPLACE PROCEDURE PROC_CAP_NHAT_TRANG_THAI_HD (
    p_hoadon_id IN HOADON.id%TYPE,
    p_trangthai_cu IN HOADON.trangthai%TYPE,
    p_trangthai_moi IN HOADON.trangthai%TYPE,
    p_so_dong_cap_nhat OUT NUMBER
)
AS
BEGIN
    UPDATE HOADON
    SET trangthai = p_trangthai_moi
    WHERE id = p_hoadon_id
      AND trangthai = p_trangthai_cu;

    p_so_dong_cap_nhat := SQL%ROWCOUNT;
END;
/

/*
PROCEDURE: PROC_NHAP_KHO
LOI/NGUY CO XU LY:
  - Nhap kho truc tiep vao SANPHAM tu nhieu noi se mat lich su.
GIAI PHAP:
  - Chi insert vao LICH_SU_KHO.
  - Trigger TRG_LICH_SU_KHO_BEFORE_INSERT se khoa SANPHAM, tinh tonkho_sau va cap nhat ton kho.
*/
CREATE OR REPLACE PROCEDURE PROC_NHAP_KHO (
    p_sanpham_id IN SANPHAM.id%TYPE,
    p_so_luong IN NUMBER,
    p_nguoi_thuc_hien IN NHANVIEN.id%TYPE,
    p_ghi_chu IN VARCHAR2 DEFAULT NULL
)
AS
BEGIN
    INSERT INTO LICH_SU_KHO(sanpham_id, loai_phieu, so_luong, nguoi_thuc_hien, ghi_chu)
    VALUES (p_sanpham_id, 'NHAP_KHO', p_so_luong, p_nguoi_thuc_hien, NVL(p_ghi_chu, 'Nhap kho bang procedure'));
END;
/

/*
PROCEDURE: PROC_DIEU_CHINH_KHO
LOI/NGUY CO XU LY:
  - Quan ly dieu chinh kho bang so duong/am, neu khong qua DB trigger co the gay ton kho am.
GIAI PHAP:
  - Ghi bien dong vao LICH_SU_KHO loai DIEU_CHINH_TON.
  - Trigger kiem tra ton kho sau dieu chinh khong am.
*/
CREATE OR REPLACE PROCEDURE PROC_DIEU_CHINH_KHO (
    p_sanpham_id IN SANPHAM.id%TYPE,
    p_so_luong_bien_dong IN NUMBER,
    p_nguoi_thuc_hien IN NHANVIEN.id%TYPE,
    p_ghi_chu IN VARCHAR2 DEFAULT NULL
)
AS
BEGIN
    INSERT INTO LICH_SU_KHO(sanpham_id, loai_phieu, so_luong, nguoi_thuc_hien, ghi_chu)
    VALUES (p_sanpham_id, 'DIEU_CHINH_TON', p_so_luong_bien_dong, p_nguoi_thuc_hien, NVL(p_ghi_chu, 'Dieu chinh ton kho bang procedure'));
END;
/

/*
PROCEDURE: PROC_KIEM_KE_KHO
LOI/NGUY CO XU LY:
  - Upload Excel/kiem ke la gan lai ton kho thuc te, khac voi nhap kho cong don.
GIAI PHAP:
  - Loai phieu KIEM_KE: trigger se set tonkho_sau = p_tonkho_thucte.
*/
CREATE OR REPLACE PROCEDURE PROC_KIEM_KE_KHO (
    p_sanpham_id IN SANPHAM.id%TYPE,
    p_tonkho_thucte IN NUMBER,
    p_nguoi_thuc_hien IN NHANVIEN.id%TYPE,
    p_ghi_chu IN VARCHAR2 DEFAULT NULL
)
AS
BEGIN
    INSERT INTO LICH_SU_KHO(sanpham_id, loai_phieu, so_luong, nguoi_thuc_hien, ghi_chu)
    VALUES (p_sanpham_id, 'KIEM_KE', p_tonkho_thucte, p_nguoi_thuc_hien, NVL(p_ghi_chu, 'Kiem ke ton kho bang procedure'));
END;
/

/*
PROCEDURE: PROC_KET_CA
LOI/NGUY CO XU LY:
  - Phantom read/lệch doanh thu ca: POS tao hoa don moi trong luc quan ly ket ca.
GIAI PHAP KHONG THEM CALAM.trangthai:
  - Khoa dong CALAM dang mo bang SELECT ... FOR UPDATE.
  - Cap nhat ketthuc truoc trong cung transaction.
  - Trigger TRG_HOADON_BEFORE_INSERT cung khoa dong CALAM, nen hoa don moi phai cho ket ca xong.
  - Sau khi commit, ca da co ketthuc, hoa don moi khong duoc gan vao ca cu.
*/
CREATE OR REPLACE PROCEDURE PROC_KET_CA (
    p_calam_id IN CALAM.id%TYPE,
    p_tienmat_ketca IN NUMBER,
    p_ghichu IN VARCHAR2 DEFAULT NULL
)
AS
    v_dummy NUMBER;
    v_tien_mat NUMBER := 0;
    v_tien_nganhang NUMBER := 0;
    v_tong_doanhthu NUMBER := 0;
BEGIN
    SELECT id
    INTO v_dummy
    FROM CALAM
    WHERE id = p_calam_id
      AND ketthuc IS NULL
    FOR UPDATE WAIT 3;

    UPDATE CALAM
    SET ketthuc = SYSTIMESTAMP
    WHERE id = p_calam_id;

    SELECT
        NVL(SUM(CASE WHEN UPPER(phuongthuc_tt) LIKE '%TIỀN MẶT%' OR UPPER(phuongthuc_tt) LIKE '%TIEN MAT%' THEN tongtien ELSE 0 END), 0),
        NVL(SUM(CASE WHEN NOT (UPPER(phuongthuc_tt) LIKE '%TIỀN MẶT%' OR UPPER(phuongthuc_tt) LIKE '%TIEN MAT%') THEN tongtien ELSE 0 END), 0),
        NVL(SUM(tongtien), 0)
    INTO v_tien_mat, v_tien_nganhang, v_tong_doanhthu
    FROM HOADON
    WHERE calam_id = p_calam_id
      AND trangthai = 'HOAN_THANH';

    UPDATE CALAM
    SET tien_mat = v_tien_mat,
        tien_nganhang = v_tien_nganhang,
        tong_doanhthu = v_tong_doanhthu,
        tienmat_ketca = p_tienmat_ketca,
        tongtien_thucte = p_tienmat_ketca + v_tien_nganhang,
        ghichu = p_ghichu
    WHERE id = p_calam_id;
END;
/

/*
PROCEDURE: PROC_THANH_TOAN_1_SP
MUC DICH:
  - Mo phong nghiep vu thanh toan o tang DBMS cho bao cao mon HQTCSDL.
  - App Java van chay nhu cu; procedure nay dung de giai thich neu thầy hỏi DB co the xu ly nghiep vu ra sao.
LOI/NGUY CO XU LY:
  - Lost update ton kho: khoa SANPHAM bang FOR UPDATE.
  - Ban vuot ton kho: kiem tra ton kho truoc khi insert hoa don/cthd.
  - Hoa don khong co ca: trigger HOADON_BEFORE_INSERT tu gan ca.
*/
CREATE OR REPLACE PROCEDURE PROC_THANH_TOAN_1_SP (
    p_hoadon_id IN HOADON.id%TYPE,
    p_nhanvien_id IN NHANVIEN.id%TYPE,
    p_khachhang_id IN KHACHHANG.id%TYPE,
    p_sanpham_id IN SANPHAM.id%TYPE,
    p_soluong IN NUMBER,
    p_phuongthuc_tt IN HOADON.phuongthuc_tt%TYPE
)
AS
    v_tonkho SANPHAM.tonkho%TYPE;
    v_giaban SANPHAM.giaban%TYPE;
    v_tongtien NUMBER;
    v_trangthai HOADON.trangthai%TYPE;
BEGIN
    SELECT tonkho, giaban
    INTO v_tonkho, v_giaban
    FROM SANPHAM
    WHERE id = p_sanpham_id
      AND NVL(is_deleted, 0) = 0
    FOR UPDATE WAIT 3;

    IF p_soluong <= 0 THEN
        RAISE_APPLICATION_ERROR(-20100, 'So luong thanh toan phai lon hon 0.');
    END IF;

    IF v_tonkho < p_soluong THEN
        RAISE_APPLICATION_ERROR(-20101, 'Khong du ton kho de thanh toan.');
    END IF;

    v_tongtien := v_giaban * p_soluong;

    IF p_phuongthuc_tt IN ('Ngân hàng', 'VNPay') THEN
        v_trangthai := 'CHO_THANH_TOAN';
    ELSE
        v_trangthai := 'HOAN_THANH';
    END IF;

    INSERT INTO HOADON(id, nhanvien_id, khachhang_id, ngaylap, tongtien, giamgia, phuongthuc_tt, trangthai)
    VALUES (p_hoadon_id, p_nhanvien_id, p_khachhang_id, SYSTIMESTAMP, v_tongtien, 0, p_phuongthuc_tt, v_trangthai);

    INSERT INTO CTHD(hoadon_id, sanpham_id, soluong, dongia)
    VALUES (p_hoadon_id, p_sanpham_id, p_soluong, v_giaban);
END;
/
