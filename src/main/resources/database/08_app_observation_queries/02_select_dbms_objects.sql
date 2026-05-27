/*
===============================================================================
FILE: 02_select_dbms_objects.sql
MUC DICH:
  - Kiem tra cac object DBMS da duoc tao trong Oracle.
===============================================================================
*/

-- Kiem tra trigger.
SELECT trigger_name, table_name, status
FROM user_triggers
WHERE trigger_name IN (
    'TRG_LICH_SU_KHO_BEFORE_INSERT',
    'TRG_CALAM_TIENMAT_BANDAU',
    'TRG_HOADON_BEFORE_INSERT',
    'TRG_CTHD_AFTER_INSERT',
    'TRG_HOADON_AFTER_INSERT',
    'TRG_HOADON_AFTER_UPDATE',
    'TRG_AUDIT_HOADON_STATUS',
    'TRG_AUDIT_SANPHAM_UPDATE',
    'TRG_AUDIT_LICHSUKHO_INSERT'
)
ORDER BY trigger_name;

-- Kiem tra procedure/function.
SELECT object_name, object_type, status
FROM user_objects
WHERE object_type IN ('PROCEDURE', 'FUNCTION')
  AND object_name LIKE 'PROC_%' OR object_name LIKE 'FN_%'
ORDER BY object_type, object_name;

-- Kiem tra constraint bo sung.
SELECT constraint_name, table_name, constraint_type, status
FROM user_constraints
WHERE constraint_name LIKE 'CHK_%'
ORDER BY table_name, constraint_name;

-- Kiem tra index bo sung.
SELECT index_name, table_name, uniqueness
FROM user_indexes
WHERE index_name LIKE '%_APP'
ORDER BY table_name, index_name;
