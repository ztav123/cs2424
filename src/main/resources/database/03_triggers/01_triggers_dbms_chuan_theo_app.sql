/*
===============================================================================
FILE: 01_triggers_dbms_chuan_theo_app.sql
MUC DICH:
  - Dinh nghia lai trigger theo dung 7 bang/cot goc cua app POS.
  - Khong dung cot tu che nhu ma_nhanvien, ngaycapnhat, CALAM.trangthai.
  - Moi trigger deu co comment: loi/nguy co nao xay ra va trigger xu ly ra sao.
===============================================================================
*/

/*
TRIGGER: TRG_LICH_SU_KHO_BEFORE_INSERT
LOI/NGUY CO XU LY:
  1. Lost Update ton kho:
     - Nhieu POS/quan ly cung cap nhat 1 san pham.
     - Xu ly bang SELECT ... FOR UPDATE tren dong SANPHAM.
  2. Ban vuot ton kho:
     - Khi xuat kho, trigger kiem tra ton kho hien tai - so_luong.
     - Neu am, raise error va rollback transaction.
  3. Du lieu kho khong sach:
     - Code Java co cac loai phieu: NULL, XUAT_BAN, NHAP_HUY_DON, NHAP_EXCEL.
     - Trigger tu chuan hoa NULL thanh DIEU_CHINH_TON hoac XUAT_BAN tuy vai tro.
  4. Upload Excel:
     - Java dang set SANPHAM.tonkho truoc, sau do insert LICH_SU_KHO loai NHAP_EXCEL.
     - Neu trigger cong tiep se sai ton kho.
     - Ban nay xu ly NHAP_EXCEL nhu KIEM_KE: tonkho_sau = so_luong, khong cong don.

LUU Y:
  - Trigger nay khong tu tao san pham moi khi sanpham_id sai, vi lam vay se sinh du lieu rac.
  - Neu sanpham_id khong ton tai, trigger bao loi de backend/nguoi dung sua du lieu.
*/
CREATE OR REPLACE TRIGGER TRG_LICH_SU_KHO_BEFORE_INSERT
BEFORE INSERT ON LICH_SU_KHO
FOR EACH ROW
DECLARE
    v_tonkho_hientai NUMBER := 0;
    v_vaitro NHANVIEN.vaitro%TYPE;
    v_loai_phieu VARCHAR2(50);
BEGIN
    SELECT tonkho
    INTO v_tonkho_hientai
    FROM SANPHAM
    WHERE id = :NEW.sanpham_id
    FOR UPDATE WAIT 3;

    IF :NEW.nguoi_thuc_hien IS NOT NULL THEN
        BEGIN
            SELECT vaitro
            INTO v_vaitro
            FROM NHANVIEN
            WHERE id = :NEW.nguoi_thuc_hien;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                v_vaitro := 'NHANVIEN';
        END;
    ELSE
        v_vaitro := 'NHANVIEN';
    END IF;

    IF :NEW.loai_phieu IS NULL THEN
        IF v_vaitro = 'ADMIN' THEN
            :NEW.loai_phieu := 'DIEU_CHINH_TON';
            :NEW.ghi_chu := NVL(:NEW.ghi_chu, 'Quan ly dieu chinh ton kho thu cong');
        ELSE
            :NEW.loai_phieu := 'XUAT_BAN';
            :NEW.ghi_chu := NVL(:NEW.ghi_chu, 'Xuat ban tu POS');
        END IF;
    END IF;

    v_loai_phieu := UPPER(TRIM(:NEW.loai_phieu));

    IF v_loai_phieu = 'XUAT_BAN' THEN
        IF :NEW.so_luong <= 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'So luong xuat ban phai lon hon 0.');
        END IF;

        IF v_tonkho_hientai - :NEW.so_luong < 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'Khong du ton kho cho san pham ID ' || :NEW.sanpham_id);
        END IF;

        :NEW.tonkho_sau := v_tonkho_hientai - :NEW.so_luong;

    ELSIF v_loai_phieu IN ('NHAP_KHO', 'NHAP_HUY_DON') THEN
        IF :NEW.so_luong <= 0 THEN
            RAISE_APPLICATION_ERROR(-20004, 'So luong nhap/hoan kho phai lon hon 0.');
        END IF;

        :NEW.tonkho_sau := v_tonkho_hientai + :NEW.so_luong;

    ELSIF v_loai_phieu IN ('KIEM_KE', 'NHAP_EXCEL') THEN
        IF :NEW.so_luong < 0 THEN
            RAISE_APPLICATION_ERROR(-20005, 'Ton kho kiem ke/Excel khong duoc am.');
        END IF;

        :NEW.tonkho_sau := :NEW.so_luong;

    ELSIF v_loai_phieu = 'DIEU_CHINH_TON' THEN
        IF :NEW.so_luong = 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'So luong dieu chinh phai khac 0.');
        END IF;

        IF v_tonkho_hientai + :NEW.so_luong < 0 THEN
            RAISE_APPLICATION_ERROR(-20007, 'Dieu chinh lam ton kho am cho san pham ID ' || :NEW.sanpham_id);
        END IF;

        :NEW.tonkho_sau := v_tonkho_hientai + :NEW.so_luong;

    ELSE
        RAISE_APPLICATION_ERROR(-20008, 'Loai phieu kho khong hop le: ' || :NEW.loai_phieu);
    END IF;

    UPDATE SANPHAM
    SET tonkho = :NEW.tonkho_sau
    WHERE id = :NEW.sanpham_id;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20009, 'San pham ID ' || :NEW.sanpham_id || ' khong ton tai.');
END;
/

/*
TRIGGER: TRG_CALAM_TIENMAT_BANDAU
LOI/NGUY CO XU LY:
  - Khi mo ca moi, nguoi dung co the khong nhap tien mat ban dau.
  - Trigger tu lay tienmat_ketca cua ca truoc tren cung may_pos.
  - Giu dung logic goc cua app, khong them cot moi.
*/
CREATE OR REPLACE TRIGGER TRG_CALAM_TIENMAT_BANDAU
BEFORE INSERT ON CALAM
FOR EACH ROW
DECLARE
    v_tien_ketca_truoc NUMBER := 0;
BEGIN
    IF :NEW.tienmat_bandau = 0 OR :NEW.tienmat_bandau IS NULL THEN
        BEGIN
            SELECT tienmat_ketca
            INTO v_tien_ketca_truoc
            FROM CALAM
            WHERE may_pos = NVL(:NEW.may_pos, 1)
              AND ketthuc IS NOT NULL
            ORDER BY id DESC
            FETCH FIRST 1 ROWS ONLY;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                v_tien_ketca_truoc := 0;
        END;

        :NEW.tienmat_bandau := NVL(v_tien_ketca_truoc, 0);
    END IF;
END;
/

/*
TRIGGER: TRG_HOADON_BEFORE_INSERT
LOI/NGUY CO XU LY:
  1. Hoa don khong co ca lam:
     - Java entity HOADON khong gan calam_id truc tiep.
     - Trigger tu tim ca dang mo cua nhan vien bang dieu kien ketthuc IS NULL.
  2. Phantom/Race condition khi ket ca:
     - Trigger dung SELECT ... FOR UPDATE WAIT 3 de khoa dong CALAM dang mo.
     - Neu procedure ket ca dang giu lock, hoa don moi se phai cho.
     - Sau khi ket ca commit, dong ca khong con ketthuc IS NULL, hoa don se khong gan vao ca cu nua.
*/
CREATE OR REPLACE TRIGGER TRG_HOADON_BEFORE_INSERT
BEFORE INSERT ON HOADON
FOR EACH ROW
DECLARE
    v_calam_id CALAM.id%TYPE;
BEGIN
    IF :NEW.calam_id IS NULL THEN
        -- BƯỚC 1: Tìm ID ca làm đang mở mới nhất (Dùng MAX thay vì ORDER BY để tránh lỗi ORA-02014)
        SELECT MAX(id)
        INTO v_calam_id
        FROM CALAM
        WHERE nhanvien_id = :NEW.nhanvien_id
          AND ketthuc IS NULL;

        -- Kiểm tra nếu biến v_calam_id trả về rỗng (nhân viên chưa mở ca)
        IF v_calam_id IS NULL THEN
            RAISE_APPLICATION_ERROR(-20010, 'Nhân viên chưa có ca làm đang mở. Không thể tạo hóa đơn.');
        END IF;

        -- BƯỚC 2: Thực hiện Lock độc quyền đúng 1 dòng Ca Làm đó thông qua Primary Key
        SELECT id
        INTO v_calam_id
        FROM CALAM
        WHERE id = v_calam_id
        FOR UPDATE WAIT 3;

        :NEW.calam_id := v_calam_id;
    END IF;
END;
/

/*
TRIGGER: TRG_CTHD_AFTER_INSERT
LOI/NGUY CO XU LY:
  - Khi co chi tiet hoa don, kho phai duoc tru tu dong.
  - Trigger khong tru kho truc tiep ma insert vao LICH_SU_KHO.
  - TRG_LICH_SU_KHO_BEFORE_INSERT moi la trung tam kiem soat ton kho, lock va chong am.
*/
CREATE OR REPLACE TRIGGER TRG_CTHD_AFTER_INSERT
AFTER INSERT ON CTHD
FOR EACH ROW
DECLARE
    v_trangthai HOADON.trangthai%TYPE;
    v_nhanvien_id HOADON.nhanvien_id%TYPE;
BEGIN
    SELECT trangthai, nhanvien_id
    INTO v_trangthai, v_nhanvien_id
    FROM HOADON
    WHERE id = :NEW.hoadon_id;

    IF v_trangthai IN ('HOAN_THANH', 'CHO_THANH_TOAN') THEN
        INSERT INTO LICH_SU_KHO (sanpham_id, loai_phieu, so_luong, nguoi_thuc_hien, ghi_chu)
        VALUES (:NEW.sanpham_id, 'XUAT_BAN', :NEW.soluong, v_nhanvien_id, 'Ban hoa don: ' || :NEW.hoadon_id);
    END IF;
END;
/

/*
TRIGGER: TRG_HOADON_AFTER_INSERT
LOI/NGUY CO XU LY:
  - Hoa don tien mat tao ra voi trangthai HOAN_THANH can cap nhat ngay doanh thu ca va diem khach hang.
  - Trigger dung UPDATE atomic:
      diemtichluy = diemtichluy + diem
      tong_doanhthu = tong_doanhthu + tongtien
  - Cach cap nhat cong don truc tiep trong DB giup giam lost update.
*/
CREATE OR REPLACE TRIGGER TRG_HOADON_AFTER_INSERT
AFTER INSERT ON HOADON
FOR EACH ROW
BEGIN
    IF :NEW.trangthai = 'HOAN_THANH' THEN
        IF :NEW.khachhang_id IS NOT NULL THEN
            UPDATE KHACHHANG
            SET diemtichluy = diemtichluy + FLOOR(:NEW.tongtien / 10000)
            WHERE id = :NEW.khachhang_id;
        END IF;

        IF UPPER(:NEW.phuongthuc_tt) LIKE '%TIỀN MẶT%' OR UPPER(:NEW.phuongthuc_tt) LIKE '%TIEN MAT%' THEN
            UPDATE CALAM
            SET tong_doanhthu = NVL(tong_doanhthu, 0) + :NEW.tongtien,
                tien_mat = NVL(tien_mat, 0) + :NEW.tongtien
            WHERE id = :NEW.calam_id;
        ELSE
            UPDATE CALAM
            SET tong_doanhthu = NVL(tong_doanhthu, 0) + :NEW.tongtien,
                tien_nganhang = NVL(tien_nganhang, 0) + :NEW.tongtien
            WHERE id = :NEW.calam_id;
        END IF;
    END IF;
END;
/

/*
TRIGGER: TRG_HOADON_AFTER_UPDATE
LOI/NGUY CO XU LY:
  1. Don VNPay that bai/qua han:
     - Trang thai CHO_THANH_TOAN -> THAT_BAI.
     - Can hoan kho cho cac CTHD da tru kho truoc do.
  2. Don CHO_THANH_TOAN thanh cong:
     - Trang thai CHO_THANH_TOAN -> HOAN_THANH.
     - Can cong doanh thu ca va diem khach hang.
  3. Quan ly doi hoa don da hoan thanh sang THAT_BAI:
     - Can tru lai doanh thu, tru diem va hoan kho.
*/
CREATE OR REPLACE TRIGGER TRG_HOADON_AFTER_UPDATE
AFTER UPDATE OF trangthai ON HOADON
FOR EACH ROW
BEGIN
    IF :OLD.trangthai IN ('HOAN_THANH', 'CHO_THANH_TOAN') AND :NEW.trangthai = 'THAT_BAI' THEN
        FOR r IN (SELECT sanpham_id, soluong FROM CTHD WHERE hoadon_id = :NEW.id) LOOP
            INSERT INTO LICH_SU_KHO (sanpham_id, loai_phieu, so_luong, nguoi_thuc_hien, ghi_chu)
            VALUES (r.sanpham_id, 'NHAP_HUY_DON', r.soluong, :NEW.nhanvien_id, 'Hoan kho do hoa don that bai: ' || :NEW.id);
        END LOOP;
    END IF;

    IF :OLD.trangthai <> 'HOAN_THANH' AND :NEW.trangthai = 'HOAN_THANH' THEN
        IF :NEW.khachhang_id IS NOT NULL THEN
            UPDATE KHACHHANG
            SET diemtichluy = diemtichluy + FLOOR(:NEW.tongtien / 10000)
            WHERE id = :NEW.khachhang_id;
        END IF;

        IF UPPER(:NEW.phuongthuc_tt) LIKE '%TIỀN MẶT%' OR UPPER(:NEW.phuongthuc_tt) LIKE '%TIEN MAT%' THEN
            UPDATE CALAM
            SET tong_doanhthu = NVL(tong_doanhthu, 0) + :NEW.tongtien,
                tien_mat = NVL(tien_mat, 0) + :NEW.tongtien
            WHERE id = :NEW.calam_id;
        ELSE
            UPDATE CALAM
            SET tong_doanhthu = NVL(tong_doanhthu, 0) + :NEW.tongtien,
                tien_nganhang = NVL(tien_nganhang, 0) + :NEW.tongtien
            WHERE id = :NEW.calam_id;
        END IF;
    END IF;

    IF :OLD.trangthai = 'HOAN_THANH' AND :NEW.trangthai <> 'HOAN_THANH' THEN
        IF :OLD.khachhang_id IS NOT NULL THEN
            UPDATE KHACHHANG
            SET diemtichluy = GREATEST(0, diemtichluy - FLOOR(:OLD.tongtien / 10000))
            WHERE id = :OLD.khachhang_id;
        END IF;

        IF UPPER(:OLD.phuongthuc_tt) LIKE '%TIỀN MẶT%' OR UPPER(:OLD.phuongthuc_tt) LIKE '%TIEN MAT%' THEN
            UPDATE CALAM
            SET tong_doanhthu = GREATEST(0, NVL(tong_doanhthu, 0) - :OLD.tongtien),
                tien_mat = GREATEST(0, NVL(tien_mat, 0) - :OLD.tongtien)
            WHERE id = :OLD.calam_id;
        ELSE
            UPDATE CALAM
            SET tong_doanhthu = GREATEST(0, NVL(tong_doanhthu, 0) - :OLD.tongtien),
                tien_nganhang = GREATEST(0, NVL(tien_nganhang, 0) - :OLD.tongtien)
            WHERE id = :OLD.calam_id;
        END IF;
    END IF;
END;
/
