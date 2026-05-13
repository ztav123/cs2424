-- =====================================================
-- DỮ LIỆU BẢNG NHANVIEN (Bao gồm cả Quản lý)
-- =====================================================
-- Cấu trúc: id, hoten, sdt, mkdangnhap, vaitro, trangthai
INSERT INTO NHANVIEN (id, hoten, sdt, mkdangnhap, vaitro, trangthai) 
VALUES (999999, 'Phan Trần Quản Lý', '0900000000', '123456', 'ADMIN', 1);

INSERT INTO NHANVIEN (id, hoten, sdt, mkdangnhap, vaitro, trangthai) 
VALUES (67, 'Nguyen Phu Quoc', '0365931022', '123', 'NHANVIEN', 1);

INSERT INTO NHANVIEN (id, hoten, sdt, mkdangnhap, vaitro, trangthai) 
VALUES (36, 'Peter Lam', '0365122167', '1', 'NHANVIEN', 1);


-- =====================================================
-- DỮ LIỆU BẢNG SANPHAM (Đã gộp Tồn kho)
-- =====================================================
-- Cấu trúc: id, ten, category, giaban, tonkho, image_url

-- Nhóm: Food (Đồ ăn)
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (101, 'Bánh mì thịt nướng', 'Food', 25000, 10, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (102, 'Cơm nắm cá hồi', 'Food', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (103, 'Xôi gà nấm', 'Food', 20000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (104, 'Mì ly lẩu Thái', 'Food', 12000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (105, 'Sandwich trứng phô mai', 'Food', 22000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (106, 'Xúc xích tiệt trùng', 'Food', 10000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (107, 'Bánh bao nhân thịt', 'Food', 15000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (108, 'Trứng luộc lòng đào', 'Food', 8000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (109, 'Salad trộn dầu giấm', 'Food', 30000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (110, 'Bento lươn Nhật', 'Food', 55000, 0, '');

-- Nhóm: Drink (Đồ uống)
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (111, 'Nước khoáng Lavie 500ml', 'Drink', 6000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (112, 'Pepsi lon 320ml', 'Drink', 11000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (113, 'Trà xanh không độ', 'Drink', 10000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (114, 'Nước tăng lực Sting', 'Drink', 12000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (115, 'Sữa tươi Vinamilk ít đường', 'Drink', 8000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (116, 'Trà sữa đóng chai', 'Drink', 15000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (117, 'Cà phê sữa đá lon', 'Drink', 14000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (118, 'Nước cam ép Teppy', 'Drink', 12000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (119, 'Bia Heineken lon', 'Drink', 22000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (120, 'Nước bù khoáng Revive', 'Drink', 10000, 0, '');

-- Nhóm: Snack (Ăn vặt)
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (121, 'Snack khoai tây Lays', 'Snack', 12000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (122, 'Bánh quy OREO', 'Snack', 16000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (123, 'Kẹo dẻo Haribo', 'Snack', 25000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (124, 'Hạt điều rang muối', 'Snack', 35000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (125, 'Socola KitKat', 'Snack', 14000, 0, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (126, 'Bánh gạo One One', 'Snack', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (127, 'Khô gà lá chanh', 'Snack', 28000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (128, 'Bắp rang bơ', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (129, 'Rong biển sấy khô', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (130, 'Kẹo cao su Cool Air', 'Snack', 6000, 50, '');

-- Nhóm: Personal Care & Household (Gộp các mục còn lại)
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (131, 'Bàn chải đánh răng P/S', 'Care', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (132, 'Kem đánh răng Colgate', 'Care', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (141, 'Pin AA Energizer (cặp)', 'House', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, image_url) VALUES (150, 'Sạc dự phòng cơ bản', 'House', 150000, 50, '');

COMMIT;