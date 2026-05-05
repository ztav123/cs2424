-- Nên thêm DROP TABLE để khi chạy lại script không bị lỗi tồn tại bảng
drop table if exists KETCA_LOG, THANHTOAN, CTHD, KHO, HOADON, KHACHHANG, SANPHAM, VOUCHER, CALAM, NHANVIEN;

create table NHANVIEN (
    id int primary key,
    hoten varchar(50),
    sdt varchar(15), 
    diachi varchar(100), 
    ngayvaolam date, 
    mkdangnhap varchar(255) -- Để dài hơn để sau này nếu dùng BCrypt mã hóa mật khẩu sẽ không bị thiếu chỗ
);

create table CALAM (
    id int primary key auto_increment, -- Nên để tự tăng cho tiện
    nhanvien_id int, 
    ngaylam date, -- Quan trọng: Cần có ngày làm
    tgianbatdau time, 
    tgianketthuc time
);

create table VOUCHER (
    id int primary key, 
    ngaybatdau date, 
    ngayketthuc date, 
    giatrigiam int, 
    dieukien varchar(100)
);

create table SANPHAM (
    id int primary key, 
    ten varchar(100), 
    category varchar(30), -- Thêm để lọc Đồ ăn/Nước uống trên UI
    voucherid int, 
    giaban int,
    image_url varchar(255) -- Thêm để lưu đường dẫn ảnh sản phẩm hiện lên UI
);

create table KHO (
    id int primary key auto_increment, 
    sanphamid int unique, -- Mỗi sản phẩm chỉ có 1 dòng trong kho
    tonkho int
);

create table KHACHHANG (
    sdt varchar(15) primary key, -- Đổi int thành varchar
    ten varchar(50), 
    ngaylap date, 
    diem int default 0
);

create table HOADON (
    id int primary key, 
    khachhang_sdt varchar(15), -- Đổi thành sdt để khớp foreign key
    nhanvienid int, 
    ngaylap timestamp, -- Đổi thành datetime để lưu cả giờ phút giây
    trangthai varchar(20), 
    tongtien int
); 

create table CTHD (
    hoadonid int, 
    sanphamid int, 
    soluong int, 
    dongia int,
    primary key(hoadonid, sanphamid)
); 

create table THANHTOAN (
    id int primary key auto_increment, 
    hoadonid int, 
    phuongthuc varchar(20), -- Ví dụ: 'Tien mat', 'Chuyen khoan'
    ngaythanhtoan timestamp, 
    sotien int
);
create table KETCA_LOG (
    id int primary key auto_increment,
    nhanvien_id int,
    thoigian timestamp,
    tiencannop int, -- THÊM CỘT NÀY: Số tiền hệ thống ghi nhận
    tienmat int,
    tiennganhang int,
    tongtien int,
    foreign key(nhanvien_id) references NHANVIEN(id)
);

-- Khóa ngoại (Giữ nguyên logic của bạn nhưng chuẩn hóa tên cột)
alter table CALAM add constraint fk_cl_nv foreign key(nhanvien_id) references NHANVIEN(id);
alter table SANPHAM add constraint fk_sp_vch foreign key(voucherid) references VOUCHER(id); 
alter table KHO add constraint fk_kho_sp foreign key(sanphamid) references SANPHAM(id); 
alter table CTHD add constraint fk_cthd_sp foreign key(sanphamid) references SANPHAM(id); 
alter table CTHD add constraint fk_cthd_hd foreign key(hoadonid) references HOADON(id); 
alter table HOADON add constraint fk_hd_kh foreign key(khachhang_sdt) references KHACHHANG(sdt); 
alter table HOADON add constraint fk_hd_nv foreign key(nhanvienid) references NHANVIEN(id);
alter table THANHTOAN add constraint fk_tt_hd foreign key(hoadonid) references HOADON(id);