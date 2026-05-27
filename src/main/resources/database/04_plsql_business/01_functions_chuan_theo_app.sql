/*
===============================================================================
FILE: 01_functions_chuan_theo_app.sql
MUC DICH:
  - Cac function PL/SQL phuc vu nghiep vu va bao cao.
  - Chi su dung bang/cot goc cua app, khong can sua Java.
===============================================================================
*/

/*
FUNCTION: FN_TINH_DIEM_TICH_LUY
LOI/NGUY CO XU LY:
  - Nhieu noi tinh diem khac cong thuc se gay lech diem khach hang.
  - Function gom cong thuc ve 1 noi: moi 10.000 VND = 1 diem.
*/
CREATE OR REPLACE FUNCTION FN_TINH_DIEM_TICH_LUY(p_tongtien IN NUMBER)
RETURN NUMBER
IS
BEGIN
    RETURN FLOOR(NVL(p_tongtien, 0) / 10000);
END;
/

/*
FUNCTION: FN_TON_KHO_SAN_PHAM
MUC DICH:
  - Tra ve ton kho hien tai cua 1 san pham.
  - Dung khi demo mo DB xem ton kho sau khi ban hang/nhap kho.
*/
CREATE OR REPLACE FUNCTION FN_TON_KHO_SAN_PHAM(p_sanpham_id IN NUMBER)
RETURN NUMBER
IS
    v_tonkho SANPHAM.tonkho%TYPE;
BEGIN
    SELECT tonkho
    INTO v_tonkho
    FROM SANPHAM
    WHERE id = p_sanpham_id;

    RETURN v_tonkho;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
END;
/

/*
FUNCTION: FN_DOANH_THU_CA
MUC DICH:
  - Tinh doanh thu hoa don HOAN_THANH cua mot ca lam.
  - Giai quyet van de bao cao khong tinh don THAT_BAI/CHO_THANH_TOAN.
*/
CREATE OR REPLACE FUNCTION FN_DOANH_THU_CA(p_calam_id IN NUMBER)
RETURN NUMBER
IS
    v_doanhthu NUMBER;
BEGIN
    SELECT NVL(SUM(tongtien), 0)
    INTO v_doanhthu
    FROM HOADON
    WHERE calam_id = p_calam_id
      AND trangthai = 'HOAN_THANH';

    RETURN v_doanhthu;
END;
/

/*
FUNCTION: FN_DOANH_THU_NGAY
MUC DICH:
  - Tinh doanh thu trong ngay dua tren hoa don HOAN_THANH.
  - Dung trong bao cao va demo performance/execution plan.
*/
CREATE OR REPLACE FUNCTION FN_DOANH_THU_NGAY(p_ngay IN DATE)
RETURN NUMBER
IS
    v_doanhthu NUMBER;
BEGIN
    SELECT NVL(SUM(tongtien), 0)
    INTO v_doanhthu
    FROM HOADON
    WHERE trangthai = 'HOAN_THANH'
      AND ngaylap >= TRUNC(p_ngay)
      AND ngaylap < TRUNC(p_ngay) + 1;

    RETURN v_doanhthu;
END;
/
