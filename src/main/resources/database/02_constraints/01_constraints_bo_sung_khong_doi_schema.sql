/*
===============================================================================
FILE: 01_constraints_bo_sung_khong_doi_schema.sql
MUC DICH:
  - Bo sung rang buoc du lieu sach tren CAC COT DA CO SAN trong database goc.
  - Khong them cot moi.
  - Khong them khoa ngoai moi vao cac bang nghiep vu goc.
  - Cac constraint nay dung de the hien vai tro RDBMS la lop chan du lieu ban.

LUU Y:
  - Constraint ton kho >= 0 da co trong schema goc voi ten chk_tonkho_positive.
  - File nay chi bo sung cac constraint con thieu.
  - Neu constraint da ton tai, block se bo qua loi trung ten constraint.
===============================================================================
*/

-- Ham noi bo: moi block ALTER TABLE deu bat ORA-02264 neu constraint da ton tai.

/*
NHANVIEN.trangthai:
  - Loi co the xay ra: UI/backend gui trang thai khac 0/1.
  - RDBMS xu ly: CHECK chi cho phep 0 = khoa, 1 = dang hoat dong.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE NHANVIEN ADD CONSTRAINT chk_nv_trangthai_01 CHECK (trangthai IN (0, 1))';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
NHANVIEN.vaitro:
  - Loi co the xay ra: them vai tro rac lam sai phan quyen.
  - Cac gia tri dang dung trong app: ADMIN, NHANVIEN.
*/
BEGIN
    EXECUTE IMMEDIATE q'[ALTER TABLE NHANVIEN ADD CONSTRAINT chk_nv_vaitro_app CHECK (vaitro IN ('ADMIN', 'NHANVIEN'))]';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
KHACHHANG.diemtichluy:
  - Loi co the xay ra: diem bi tru am khi huy hoa don hoac cap nhat sai.
  - RDBMS chan diem am.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE KHACHHANG ADD CONSTRAINT chk_kh_diem_khong_am CHECK (diemtichluy >= 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
SANPHAM.giaban va SANPHAM.is_deleted:
  - Loi co the xay ra: gia ban am hoac co xoa mem khac 0/1.
  - RDBMS dam bao du lieu san pham hop le.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE SANPHAM ADD CONSTRAINT chk_sp_giaban_khong_am CHECK (giaban >= 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE SANPHAM ADD CONSTRAINT chk_sp_is_deleted_01 CHECK (is_deleted IN (0, 1))';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
LICH_SU_KHO.so_luong:
  - Code Java dieu chinh kho thu cong cho phep so_luong am hoac duong, mien khac 0.
  - Vi vay KHONG dung CHECK so_luong > 0, vi se pha chuc nang giam kho thu cong.
  - RDBMS chi chan so_luong = 0.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE LICH_SU_KHO ADD CONSTRAINT chk_lsk_soluong_khac_0 CHECK (so_luong <> 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
LICH_SU_KHO.loai_phieu:
  - Loi co the xay ra: loai phieu rac lam trigger xu ly sai ton kho.
  - Cho phep NULL vi code Java dieu-chinh kho dang de loai_phieu NULL de trigger tu gan.
  - Bo sung NHAP_EXCEL vi code Java upload Excel dang dung gia tri nay.
*/
BEGIN
    EXECUTE IMMEDIATE q'[ALTER TABLE LICH_SU_KHO ADD CONSTRAINT chk_lsk_loai_phieu_app CHECK (
        loai_phieu IS NULL OR loai_phieu IN (
            'NHAP_KHO', 'XUAT_BAN', 'NHAP_HUY_DON', 'KIEM_KE', 'DIEU_CHINH_TON', 'NHAP_EXCEL'
        )
    )]';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
CALAM cac cot tien:
  - Loi co the xay ra: tien dau ca, tien ket ca, doanh thu bi am.
  - RDBMS chan gia tri tai chinh am.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE CALAM ADD CONSTRAINT chk_calam_tien_khong_am CHECK (
        tienmat_bandau >= 0 AND tienmat_ketca >= 0 AND tien_nganhang >= 0 AND
        tien_mat >= 0 AND tong_doanhthu >= 0 AND tongtien_thucte >= 0
    )';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
HOADON.trangthai:
  - Code Java/UI hien dung dung 3 trang thai:
    CHO_THANH_TOAN, HOAN_THANH, THAT_BAI.
  - Khong them HUY vi schema/code goc khong dung.
*/
BEGIN
    EXECUTE IMMEDIATE q'[ALTER TABLE HOADON ADD CONSTRAINT chk_hd_trangthai_app CHECK (
        trangthai IN ('CHO_THANH_TOAN', 'HOAN_THANH', 'THAT_BAI')
    )]';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
HOADON tong tien va giam gia:
  - Chan tong tien am, giam gia am.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE HOADON ADD CONSTRAINT chk_hd_tien_khong_am CHECK (tongtien >= 0 AND giamgia >= 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
HOADON.phuongthuc_tt:
  - Code Java/frontend co the gui: Tiền mặt, Ngân hàng, VNPay.
  - Khong ep ve TIEN_MAT/NGAN_HANG vi se khong khop app.
*/
BEGIN
    EXECUTE IMMEDIATE q'[ALTER TABLE HOADON ADD CONSTRAINT chk_hd_phuongthuc_app CHECK (
        phuongthuc_tt IN ('Tiền mặt', 'Ngân hàng', 'VNPay')
    )]';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

/*
CTHD:
  - So luong ban phai > 0.
  - Don gia tai thoi diem ban khong duoc am.
*/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE CTHD ADD CONSTRAINT chk_cthd_soluong_duong CHECK (soluong > 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE CTHD ADD CONSTRAINT chk_cthd_dongia_khong_am CHECK (dongia >= 0)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2264 THEN RAISE; END IF;
END;
/
