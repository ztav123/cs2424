/*
===============================================================================
FILE: 02_execution_plan_queries.sql
MUC DICH:
  - Demo Execution Plan cho yeu cau toi uu hieu nang.
  - Chay sau khi da tao index trong 01_indexes_theo_schema_goc.sql.
===============================================================================
*/

-- 1. Execution plan truy van doanh thu hoa don hoan thanh theo ngay.
EXPLAIN PLAN FOR
SELECT NVL(SUM(tongtien), 0) AS doanh_thu
FROM HOADON
WHERE trangthai = 'HOAN_THANH'
  AND ngaylap >= TRUNC(SYSDATE)
  AND ngaylap < TRUNC(SYSDATE) + 1;

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

-- 2. Execution plan truy van san pham ban chay.
EXPLAIN PLAN FOR
SELECT sp.id, sp.ten, SUM(ct.soluong) AS tong_sl
FROM CTHD ct
JOIN SANPHAM sp ON sp.id = ct.sanpham_id
JOIN HOADON hd ON hd.id = ct.hoadon_id
WHERE hd.trangthai = 'HOAN_THANH'
GROUP BY sp.id, sp.ten
ORDER BY tong_sl DESC;

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);
