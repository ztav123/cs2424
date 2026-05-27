/*
===============================================================================
FILE: 01_giai_thich_concurrency_theo_de_tai_2.sql
MUC DICH:
  - Ghi chu giai thich cac van de giao tac theo yeu cau De tai 2.
  - File nay khong co muc tieu tao loi cho thay xem.
  - Khi bao cao, app chay thanh cong; file nay dung de chi ra RDBMS da xu ly nguy co nao.
===============================================================================

1. DIRTY READ
-------------------------------------------------------------------------------
Oracle mac dinh khong cho doc du lieu chua COMMIT cua transaction khac.
Code Java thanh toan dung READ_COMMITTED, nen dirty read gan nhu khong xay ra.

2. LOST UPDATE TON KHO
-------------------------------------------------------------------------------
Nguy co:
  - Hai may POS cung ban mot san pham.
  - Neu cung doc tonkho cu va cung tru, ton kho se sai.
Xu ly:
  - TRG_LICH_SU_KHO_BEFORE_INSERT dung SELECT ... FOR UPDATE tren SANPHAM.
  - PROC_THANH_TOAN_1_SP cung dung SELECT ... FOR UPDATE.

3. DEADLOCK
-------------------------------------------------------------------------------
Nguy co:
  - Transaction A khoa san pham 1 roi doi san pham 2.
  - Transaction B khoa san pham 2 roi doi san pham 1.
Xu ly:
  - Trong code Java CheckoutService da sort productIds tang dan truoc khi lock.
  - Trong DBMS, khi viet procedure xu ly nhieu san pham nen khoa theo ORDER BY id.
  - Cac trigger/procedure trong folder nay chu yeu xu ly tung san pham moi lan bien dong kho.

4. PHANTOM READ KHI KET CA
-------------------------------------------------------------------------------
Nguy co:
  - Quan ly ket ca trong khi POS khac tao hoa don moi vao ca do.
  - Hoa don moi co the bi gan vao ca cu nhung khong nam trong tong ket ca.
Xu ly khong them cot moi:
  - PROC_KET_CA khoa dong CALAM bang SELECT ... FOR UPDATE.
  - TRG_HOADON_BEFORE_INSERT cung SELECT ... FOR UPDATE dong CALAM dang mo.
  - Hoa don moi phai cho ket ca xong; sau commit, ketthuc != NULL nen khong gan vao ca cu.

5. RACE CONDITION TRANG THAI HOA DON
-------------------------------------------------------------------------------
Nguy co:
  - Job huy don qua han cap nhat CHO_THANH_TOAN -> THAT_BAI.
  - VNPay return cap nhat CHO_THANH_TOAN -> HOAN_THANH.
Xu ly:
  - PROC_CAP_NHAT_TRANG_THAI_HD dung conditional update:
      UPDATE HOADON
      SET trangthai = moi
      WHERE id = ? AND trangthai = cu;
  - Neu SQL%ROWCOUNT = 0, hoa don da bi transaction khac xu ly.

6. NON-REPEATABLE READ
-------------------------------------------------------------------------------
Nguy co:
  - Bao cao doc mot hoa don, transaction khac doi trangthai, doc lai thay gia tri khac.
Xu ly:
  - Bao cao doanh thu chi doc hoa don HOAN_THANH.
  - Neu can nhat quan tuyet doi, bao cao co the chay theo mot moc thoi gian hoac dung transaction read only.

7. KET LUAN
-------------------------------------------------------------------------------
Trong demo, khong can tao loi truc tiep. Chi can chay app thanh cong, sau do mo DB de chi:
  - Bang HOADON/CTHD co du lieu.
  - SANPHAM.tonkho da thay doi.
  - LICH_SU_KHO ghi lai bien dong.
  - CALAM cap nhat doanh thu.
  - AUDIT_LOG/DBMS_RECOVERY_LOG ghi vet neu da cai phan audit/recovery.
*/
