/*
===============================================================================
FILE: 01_indexes_theo_schema_goc.sql
MUC DICH:
  - Bo sung index toi uu truy van dua tren cac cot goc cua app.
  - Khong them cot moi, khong doi khoa ngoai.
===============================================================================
*/

/*
Index phuc vu thong ke hoa don theo trang thai va ngay lap.
Dung cho dashboard doanh thu, loc hoa don HOAN_THANH.
*/
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_hd_trangthai_ngay_app ON HOADON(trangthai, ngaylap)';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

/*
Index phuc vu thong ke theo nhan vien + trang thai + ngay.
*/
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_hd_nv_tt_ngay_app ON HOADON(nhanvien_id, trangthai, ngaylap)';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

/*
Index phuc vu thong ke san pham ban chay tu CTHD.
*/
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_cthd_sp_app ON CTHD(sanpham_id)';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

/*
Index phuc vu tim ca dang mo cua nhan vien trong trigger gan hoa don.
*/
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_calam_nv_ketthuc_app ON CALAM(nhanvien_id, ketthuc)';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

/*
Index phuc vu loc san pham dang ban theo is_deleted/category.
*/
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_sp_deleted_category_app ON SANPHAM(is_deleted, category)';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -955 THEN RAISE; END IF;
END;
/
