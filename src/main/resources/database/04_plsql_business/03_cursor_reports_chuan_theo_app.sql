/*
===============================================================================
FILE: 03_cursor_reports_chuan_theo_app.sql
MUC DICH:
  - Cursor PL/SQL phuc vu bao cao doanh thu va san pham ban chay.
  - Chi dung cot goc cua app.
===============================================================================
*/

SET SERVEROUTPUT ON;

/*
PROCEDURE: PROC_BAO_CAO_DOANH_THU_CA
CURSOR: c_ca
LOI/NGUY CO XU LY:
  - Bao cao doanh thu ca khong nen tinh hoa don THAT_BAI/CHO_THANH_TOAN.
  - Cursor duyet tung ca va tinh doanh thu dua tren HOADON.trangthai = HOAN_THANH.
*/
CREATE OR REPLACE PROCEDURE PROC_BAO_CAO_DOANH_THU_CA
AS
    CURSOR c_ca IS
        SELECT c.id,
               c.nhanvien_id,
               c.may_pos,
               c.batdau,
               c.ketthuc,
               FN_DOANH_THU_CA(c.id) AS doanh_thu_thuc_te
        FROM CALAM c
        ORDER BY c.id DESC;
BEGIN
    DBMS_OUTPUT.PUT_LINE('BAO CAO DOANH THU THEO CA');

    FOR r IN c_ca LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Ca ' || r.id ||
            ' | NV=' || r.nhanvien_id ||
            ' | POS=' || r.may_pos ||
            ' | Bat dau=' || TO_CHAR(r.batdau, 'YYYY-MM-DD HH24:MI:SS') ||
            ' | Ket thuc=' || NVL(TO_CHAR(r.ketthuc, 'YYYY-MM-DD HH24:MI:SS'), 'DANG MO') ||
            ' | Doanh thu=' || r.doanh_thu_thuc_te
        );
    END LOOP;
END;
/

/*
PROCEDURE: PROC_BAO_CAO_SAN_PHAM_BAN_CHAY
CURSOR: c_sp
LOI/NGUY CO XU LY:
  - Bao cao san pham ban chay khong duoc tinh don THAT_BAI.
  - Cursor chi lay hoa don HOAN_THANH trong khoang ngay.
*/
CREATE OR REPLACE PROCEDURE PROC_BAO_CAO_SAN_PHAM_BAN_CHAY(
    p_tu_ngay IN DATE,
    p_den_ngay IN DATE
)
AS
    CURSOR c_sp IS
        SELECT sp.id,
               sp.ten,
               SUM(ct.soluong) AS tong_sl,
               SUM(ct.soluong * ct.dongia) AS tong_tien
        FROM CTHD ct
        JOIN SANPHAM sp ON sp.id = ct.sanpham_id
        JOIN HOADON hd ON hd.id = ct.hoadon_id
        WHERE hd.trangthai = 'HOAN_THANH'
          AND hd.ngaylap >= p_tu_ngay
          AND hd.ngaylap < p_den_ngay + 1
        GROUP BY sp.id, sp.ten
        ORDER BY tong_sl DESC;
BEGIN
    DBMS_OUTPUT.PUT_LINE('BAO CAO SAN PHAM BAN CHAY');

    FOR r IN c_sp LOOP
        DBMS_OUTPUT.PUT_LINE(
            'SP ' || r.id || ' - ' || r.ten ||
            ' | SL=' || r.tong_sl ||
            ' | Tien=' || r.tong_tien
        );
    END LOOP;
END;
/
