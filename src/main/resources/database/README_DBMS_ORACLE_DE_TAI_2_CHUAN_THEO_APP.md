# README DBMS ORACLE - ĐỀ TÀI 2 - BẢN CHUẨN THEO DATABASE GỐC CỦA APP

## 1. Nguyên tắc của folder này

Folder `database/` này được tái cấu trúc theo đúng yêu cầu đồ án **Đề tài 2: Xây dựng hệ thống thực tế và xử lý giao tác phức tạp**.

Nguyên tắc quan trọng nhất:

- **Không chế thêm cột vào 7 bảng gốc của app.**
- **Không đổi tên bảng, tên cột, kiểu khóa chính hoặc khóa ngoại gốc.**
- **Không thêm khóa ngoại mới vào các bảng nghiệp vụ gốc nếu code Java không dùng.**
- **Chỉ bổ sung object DBMS phụ trợ ở bên ngoài như AUDIT_LOG hoặc DBMS_RECOVERY_LOG để phục vụ báo cáo kiểm toán/phục hồi.**
- **Các trigger/procedure/function/cursor chỉ dùng các cột đang có trong schema gốc.**

Schema gốc của app gồm đúng 7 bảng:

1. `NHANVIEN`
2. `KHACHHANG`
3. `SANPHAM`
4. `LICH_SU_KHO`
5. `CALAM`
6. `HOADON`
7. `CTHD`

## 2. Những lỗi ở bản trước đã sửa

Bản trước có một số điểm không khớp với database gốc của app, ví dụ:

- Có thêm `NHANVIEN.ma_nhanvien`.
- Có thêm `NHANVIEN.ngaytao`.
- Có thêm `SANPHAM.ngaycapnhat`.
- Có thêm `CALAM.trangthai`.
- Có thêm khóa ngoại `LICH_SU_KHO.nguoi_thuc_hien -> NHANVIEN.id`.
- Có đổi `HOADON.id` từ `VARCHAR2(20)` thành `VARCHAR2(30)`.
- Có cho `HOADON.calam_id` nullable trong schema tự viết.
- Có thêm trạng thái hóa đơn `HUY` dù UI/code Java chỉ dùng `CHO_THANH_TOAN`, `HOAN_THANH`, `THAT_BAI`.

Các điểm này đã được loại bỏ khỏi bản chuẩn này.

## 3. Cấu trúc folder

```text
database/
├── 00_original_from_app/
│   ├── schema_goc_cua_app.sql
│   └── data_goc_cua_app.sql
│
├── 01_schema_seed/
│   ├── 01_schema_chuan_theo_app.sql
│   └── 02_data_chuan_theo_app.sql
│
├── 02_constraints/
│   └── 01_constraints_bo_sung_khong_doi_schema.sql
│
├── 03_triggers/
│   └── 01_triggers_dbms_chuan_theo_app.sql
│
├── 04_plsql_business/
│   ├── 01_functions_chuan_theo_app.sql
│   ├── 02_procedures_chuan_theo_app.sql
│   └── 03_cursor_reports_chuan_theo_app.sql
│
├── 05_transaction_concurrency/
│   ├── 01_giai_thich_concurrency_theo_de_tai_2.sql
│   └── 02_script_quan_sat_an_toan.sql
│
├── 06_recovery/
│   └── 01_recovery_audit_log_mo_phong.sql
│
├── 07_performance/
│   ├── 01_indexes_theo_schema_goc.sql
│   └── 02_execution_plan_queries.sql
│
├── 08_app_observation_queries/
│   ├── 01_select_sau_khi_chay_app.sql
│   └── 02_select_dbms_objects.sql
│
└── 09_install/
    └── 00_run_order.txt
```

## 4. Cách dùng khi báo cáo

Luồng báo cáo nên dùng:

1. Chạy app Java POS như bình thường.
2. Thực hiện nghiệp vụ trên app: mở ca, bán hàng, thanh toán, cập nhật kho, kết ca.
3. Mở Oracle DB.
4. Chạy các câu `SELECT` trong `08_app_observation_queries/01_select_sau_khi_chay_app.sql` để quan sát dữ liệu đã phát sinh.
5. Mở các file trigger/procedure/function/cursor để giải thích RDBMS xử lý gì phía sau.

Không cần chạy các câu `INSERT` thủ công để demo nghiệp vụ, vì dữ liệu nghiệp vụ nên phát sinh từ app.

## 5. Mapping yêu cầu thầy với folder

Theo yêu cầu Đề tài 2, sản phẩm cần có:

- Mô tả bài toán, luồng nghiệp vụ, thực thể quản lý.
- Thiết kế CSDL: ERD, PK, FK, CHECK, trigger.
- PL/SQL: Procedure, Function xử lý nghiệp vụ quan trọng.
- Transaction/concurrency: Dirty Read, Non-repeatable Read, Phantom Read, Lost Update, Deadlock.
- Recovery: Undo/Redo log, Checkpoint.
- Performance: Execution Plan, Response time, Throughput.
- UI demo nếu có.

Folder này đáp ứng như sau:

| Yêu cầu | File tương ứng |
|---|---|
| Schema + dữ liệu mẫu | `01_schema_seed/` |
| Constraint | `02_constraints/` |
| Trigger | `03_triggers/` |
| Procedure/Function/Cursor | `04_plsql_business/` |
| Concurrency | `05_transaction_concurrency/` |
| Recovery mô phỏng | `06_recovery/` |
| Index + Execution Plan | `07_performance/` |
| Kiểm tra sau khi chạy app | `08_app_observation_queries/` |

## 6. Các object DBMS chính để giải thích

### Trigger

- `TRG_LICH_SU_KHO_BEFORE_INSERT`: kiểm soát tồn kho, chống tồn kho âm, xử lý nhập/xuất/kiểm kê/Excel.
- `TRG_CALAM_TIENMAT_BANDAU`: tự lấy tiền mặt đầu ca từ ca gần nhất của cùng máy POS.
- `TRG_HOADON_BEFORE_INSERT`: tự gán hóa đơn vào ca đang mở và khóa dòng ca để giảm xung đột kết ca.
- `TRG_CTHD_AFTER_INSERT`: khi thêm chi tiết hóa đơn thì tự ghi biến động xuất kho.
- `TRG_HOADON_AFTER_INSERT`: cộng doanh thu ca và điểm khách hàng khi hóa đơn hoàn thành ngay lúc tạo.
- `TRG_HOADON_AFTER_UPDATE`: hoàn kho/cộng doanh thu/cộng điểm khi hóa đơn đổi trạng thái.
- `TRG_AUDIT_HOADON_STATUS`, `TRG_AUDIT_SANPHAM_UPDATE`, `TRG_AUDIT_LICHSUKHO_INSERT`: ghi log kiểm toán.

### Procedure

- `PROC_CAP_NHAT_TRANG_THAI_HD`: cập nhật trạng thái hóa đơn theo kiểu conditional update, chống race condition.
- `PROC_NHAP_KHO`: nhập kho qua `LICH_SU_KHO`.
- `PROC_DIEU_CHINH_KHO`: điều chỉnh kho tăng/giảm qua `LICH_SU_KHO`.
- `PROC_KIEM_KE_KHO`: gán tồn kho theo số kiểm kê thực tế.
- `PROC_KET_CA`: khóa ca, chốt thời điểm kết ca, tính doanh thu ca trong một transaction.
- `PROC_THANH_TOAN_1_SP`: procedure mô phỏng nghiệp vụ thanh toán một sản phẩm ở tầng DBMS.

### Function

- `FN_TINH_DIEM_TICH_LUY`: tính điểm khách hàng từ tổng tiền.
- `FN_TON_KHO_SAN_PHAM`: trả về tồn kho hiện tại của sản phẩm.
- `FN_DOANH_THU_CA`: tính doanh thu hóa đơn hoàn thành trong một ca.
- `FN_DOANH_THU_NGAY`: tính doanh thu hoàn thành theo ngày.

### Cursor

- `PROC_BAO_CAO_DOANH_THU_CA`: dùng cursor duyệt doanh thu từng ca.
- `PROC_BAO_CAO_SAN_PHAM_BAN_CHAY`: dùng cursor duyệt sản phẩm bán chạy.

## 7. Lưu ý quan trọng

Các file trong `02_constraints`, `03_triggers`, `04_plsql_business`, `06_recovery`, `07_performance` là phần DBMS nâng cao. Khi dùng với DB app đang chạy, nên chạy trên schema test trước. Nếu đã có trigger cùng tên, file trigger sẽ `CREATE OR REPLACE`, tức là thay trigger cũ bằng trigger đã comment rõ hơn và xử lý chặt hơn nhưng vẫn dùng đúng bảng/cột gốc.
