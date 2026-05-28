-- =====================================================
-- DỮ LIỆU BẢNG NHANVIEN (Bao gồm cả Quản lý)
-- =====================================================
-- Cấu trúc: id, hoten, sdt, mkdangnhap, vaitro, trangthai
INSERT INTO NHANVIEN (id, hoten, sdt, mkdangnhap, vaitro, trangthai) 
VALUES (999999, 'Phan Trần Quản Lý', '0900000000', '$2a$10$6GBBn2Me0DKmHG3ogIqY9OovUG0CX4dM3oZEzyuAN3LmeT2eVPezu', 'ADMIN', 1);


-- =====================================================
-- DỮ LIỆU BẢNG SANPHAM (Đã gộp Tồn kho)
-- =====================================================
-- Cấu trúc: id, ten, category, giaban, tonkho, imageurl
-- ==========================================================
-- NHÓM: FOOD (Đồ ăn) - Tổng cộng 60 món
-- ==========================================================
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (101, 'Bánh mì thịt nướng', 'Food', 25000, 50, './img/101.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (102, 'Cơm nắm cá hồi', 'Food', 18000, 50, './img/102.png');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (103, 'Xôi gà nấm', 'Food', 20000, 50, './img/103.webp');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (104, 'Mì ly lẩu Thái', 'Food', 12000, 50, './img/104.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (105, 'Sandwich trứng phô mai', 'Food', 22000, 50, './img/105.png');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (106, 'Xúc xích tiệt trùng', 'Food', 10000, 50, './img/106.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (107, 'Bánh bao nhân thịt', 'Food', 15000, 50, './img/107.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (108, 'Trứng luộc lòng đào', 'Food', 8000, 50, './img/108.png');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (109, 'Salad trộn dầu giấm', 'Food', 30000, 50, './img/109.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (110, 'Bento lươn Nhật', 'Food', 55000, 50, './img/110.jpg');
-- Thêm mới 50 món Food
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (151, 'Phở bò ăn liền', 'Food', 15000, 50, './img/151.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (152, 'Bún bò Huế đóng gói', 'Food', 18000, 50, './img/152.webp');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (153, 'Cơm chiên Dương Châu', 'Food', 35000, 50, './img/153.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (154, 'Mì xào giòn', 'Food', 30000, 50, './img/154.jpg');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (155, 'Bánh giò nóng', 'Food', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (156, 'Bánh chưng mini', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (157, 'Hủ tiếu Nam Vang', 'Food', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (158, 'Bánh giầy chả lụa', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (159, 'Gà rán KFC miếng', 'Food', 39000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (160, 'Hamburger bò', 'Food', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (161, 'Pizza mini hải sản', 'Food', 49000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (162, 'Cháo lòng đóng hộp', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (163, 'Xôi gấc', 'Food', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (164, 'Bánh hỏi nem nướng', 'Food', 40000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (165, 'Mì Ý sốt cà bò bằm', 'Food', 38000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (166, 'Cơm lam ống tre', 'Food', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (167, 'Bánh đa cua', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (168, 'Nem chua rán', 'Food', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (169, 'Phô mai que', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (170, 'Khoai tây chiên', 'Food', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (171, 'Cá viên chiên', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (172, 'Bánh tráng trộn', 'Food', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (173, 'Bánh tráng cuốn', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (174, 'Bắp xào mỡ hành', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (175, 'Trứng vịt lộn', 'Food', 8000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (176, 'Súp cua', 'Food', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (177, 'Phá lấu bò', 'Food', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (178, 'Bánh xèo mini', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (179, 'Bánh khọt', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (180, 'Gỏi cuốn tôm thịt', 'Food', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (181, 'Bánh mì que', 'Food', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (182, 'Bánh mì chả cá', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (183, 'Mì trộn muối ớt', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (184, 'Cơm tấm sườn bì', 'Food', 40000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (185, 'Bánh tôm Hồ Tây', 'Food', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (186, 'Chả giò rế', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (187, 'Lạp xưởng nướng', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (188, 'Cá hồi áp chảo', 'Food', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (189, 'Thịt kho tàu hộp', 'Food', 32000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (190, 'Cá nục sốt cà', 'Food', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (191, 'Đậu hũ thối', 'Food', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (192, 'Sủi cảo hấp', 'Food', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (193, 'Hoành thánh súp', 'Food', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (194, 'Bánh canh cua', 'Food', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (195, 'Bún đậu mắm tôm', 'Food', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (196, 'Gỏi ngó sen', 'Food', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (197, 'Thịt bò khô miếng', 'Food', 55000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (198, 'Bánh mì pate', 'Food', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (199, 'Xôi xiêm', 'Food', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (200, 'Chè thái', 'Food', 25000, 50, '');

-- ==========================================================
-- NHÓM: DRINK (Đồ uống) - Tổng cộng 60 món
-- ==========================================================
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (111, 'Nước khoáng Lavie 500ml', 'Drink', 6000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (112, 'Pepsi lon 320ml', 'Drink', 11000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (113, 'Trà xanh không độ', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (114, 'Nước tăng lực Sting', 'Drink', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (115, 'Sữa tươi Vinamilk ít đường', 'Drink', 8000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (116, 'Trà sữa đóng chai', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (117, 'Cà phê sữa đá lon', 'Drink', 14000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (118, 'Nước cam ép Teppy', 'Drink', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (119, 'Bia Heineken lon', 'Drink', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (120, 'Nước bù khoáng Revive', 'Drink', 10000, 50, '');
-- Thêm mới 50 món Drink
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (201, 'Coca Cola Zero', 'Drink', 11000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (202, '7Up lon', 'Drink', 11000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (203, 'Nước tăng lực Redbull', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (204, 'Sữa đậu nành Fami', 'Drink', 7000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (205, 'Trà đào Collagen', 'Drink', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (206, 'Nước suối Aquafina', 'Drink', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (207, 'Sữa Milo hộp', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (208, 'Cà phê đen đá ly', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (209, 'Nước ép táo Vfresh', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (210, 'Bia Tiger Crystal', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (211, 'Soda chanh đường', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (212, 'Trà túi lọc Lipton', 'Drink', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (213, 'Sữa chua uống TH True', 'Drink', 9000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (214, 'Nước dừa tươi đóng chai', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (215, 'Trà vải túi lọc', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (216, 'Nước chanh leo', 'Drink', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (217, 'Bia Sài Gòn Special', 'Drink', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (218, 'Cacao nóng', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (219, 'Sữa tươi trân châu đường đen', 'Drink', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (220, 'Nước sâm lạnh', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (221, 'Nước tăng lực Warrior', 'Drink', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (222, 'Strongbow Gold Apple', 'Drink', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (223, 'Sữa bắp non', 'Drink', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (224, 'Trà Ô Long Tea Plus', 'Drink', 11000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (225, 'Nước ép dứa', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (226, 'Nước tăng lực Monster', 'Drink', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (227, 'Trà Atiso đóng chai', 'Drink', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (228, 'Nước khoáng có ga Perrier', 'Drink', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (229, 'Nước uống điện giải Pocari', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (230, 'Rượu vang đỏ mini', 'Drink', 120000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (231, 'Nước rau má đậu xanh', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (232, 'Sữa hạt hạnh nhân', 'Drink', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (233, 'Trà hoa cúc mật ong', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (234, 'Sinh tố bơ', 'Drink', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (235, 'Nước ép dưa hấu', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (236, 'Cà phê hòa tan G7', 'Drink', 3000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (237, 'Sữa yến mạch', 'Drink', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (238, 'Nước tăng lực Lipovitan', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (239, 'Trà xanh Thái Lan', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (240, 'Nước ép cà rốt', 'Drink', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (241, 'Bia Budweiser', 'Drink', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (242, 'Trà sữa Matcha', 'Drink', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (243, 'Sữa tươi Dalat Milk', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (244, 'Nước chanh muối', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (245, 'Trà sen vàng', 'Drink', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (246, 'Nước bí đao Wonderfarm', 'Drink', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (247, 'Cà phê Cold Brew', 'Drink', 40000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (248, 'Nước ngọt Mirinda Cam', 'Drink', 11000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (249, 'Sữa hạt sen', 'Drink', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (250, 'Nước khoáng hữu cơ', 'Drink', 25000, 50, '');

-- ==========================================================
-- NHÓM: SNACK (Ăn vặt) - Tổng cộng 60 món
-- ==========================================================
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (121, 'Snack khoai tây Lays', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (122, 'Bánh quy OREO', 'Snack', 16000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (123, 'Kẹo dẻo Haribo', 'Snack', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (124, 'Hạt điều rang muối', 'Snack', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (125, 'Socola KitKat', 'Snack', 14000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (126, 'Bánh gạo One One', 'Snack', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (127, 'Khô gà lá chanh', 'Snack', 28000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (128, 'Bắp rang bơ', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (129, 'Rong biển sấy khô', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (130, 'Kẹo cao su Cool Air', 'Snack', 6000, 50, '');
-- Thêm mới 50 món Snack
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (251, 'Bánh que Pocky', 'Snack', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (252, 'Snack Poca tôm cay', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (253, 'Kẹo mút Chupa Chups', 'Snack', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (254, 'Hạt hướng dương Chacha', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (255, 'Bánh bông lan Solite', 'Snack', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (256, 'Socola Snickers', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (257, 'Đậu phộng Tân Tân', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (258, 'Bánh gấu nhân kem', 'Snack', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (259, 'Mực rim me', 'Snack', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (260, 'Trái cây sấy Vinamit', 'Snack', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (261, 'Thạch dừa túi', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (262, 'Kẹo bạc hà Dynamit', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (263, 'Snack mực Bento', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (264, 'Bánh bích quy bơ', 'Snack', 40000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (265, 'Hạnh nhân rang bơ', 'Snack', 55000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (266, 'Kẹo sâm Hàn Quốc', 'Snack', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (267, 'Snack hành tây', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (268, 'Bánh Karo trứng tươi', 'Snack', 30000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (269, 'Khô bò que', 'Snack', 60000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (270, 'Bánh socola Pie', 'Snack', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (271, 'Hạt dẻ cười', 'Snack', 70000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (272, 'Snack phô mai miếng', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (273, 'Kẹo dẻo trái cây Mỹ', 'Snack', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (274, 'Bánh Crepe sầu riêng', 'Snack', 50000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (275, 'Bắp nếp sấy giòn', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (276, 'Snack rong biển cuộn', 'Snack', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (277, 'Bánh trứng Tipo', 'Snack', 32000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (278, 'Kẹo ngậm ho thảo dược', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (279, 'Bánh quy mặn AFC', 'Snack', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (280, 'Hạt óc chó tách vỏ', 'Snack', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (281, 'Snack khoai tây lon Pringles', 'Snack', 42000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (282, 'Kẹo sô cô la M và M', 'Snack', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (283, 'Bánh Donut sô cô la', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (284, 'Kẹo dừa Bến Tre', 'Snack', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (285, 'Đậu Hà Lan sấy muối', 'Snack', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (286, 'Bánh mè đen', 'Snack', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (287, 'Snack đùi gà cay', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (288, 'Kẹo cao su Hubba Bubba', 'Snack', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (289, 'Bánh tráng bơ', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (290, 'Hạt mắc ca nứt vỏ', 'Snack', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (291, 'Snack ngô cay', 'Snack', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (292, 'Bánh phồng tôm gói', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (293, 'Kẹo Marshmallow', 'Snack', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (294, 'Bánh quế Cosy', 'Snack', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (295, 'Socola Ferrero Rocher', 'Snack', 150000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (296, 'Snack đậu nành sấy', 'Snack', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (297, 'Bánh quy yến mạch', 'Snack', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (298, 'Kẹo gừng dẻo', 'Snack', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (299, 'Khoai lang tím sấy', 'Snack', 28000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (300, 'Bánh tai heo', 'Snack', 15000, 50, '');

-- ==========================================================
-- NHÓM: PERSONAL CARE (Chăm sóc cá nhân) - Tổng cộng 60 món
-- ==========================================================
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (131, 'Bàn chải đánh răng P/S', 'Personal Care', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (132, 'Kem đánh răng Colgate', 'Personal Care', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (133, 'Dầu gội Clear gói', 'Personal Care', 2000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (134, 'Sữa rửa mặt Oxy', 'Personal Care', 65000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (135, 'Lăn khử mùi Nivea', 'Personal Care', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (136, 'Băng vệ sinh Diana', 'Personal Care', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (137, 'Dao cạo râu Gillette', 'Personal Care', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (138, 'Khăn giấy bỏ túi', 'Personal Care', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (139, 'Khẩu trang y tế (set 5)', 'Personal Care', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (140, 'Nước rửa tay khô', 'Personal Care', 30000, 50, '');
-- Thêm mới 50 món Personal Care
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (301, 'Dầu gội đầu Dove chai', 'Personal Care', 120000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (302, 'Sữa tắm Lifebuoy', 'Personal Care', 150000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (303, 'Nước súc miệng Listerine', 'Personal Care', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (304, 'Kem chống nắng Sunplay', 'Personal Care', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (305, 'Bông tẩy trang (gói 100)', 'Personal Care', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (306, 'Dầu xả Pantene', 'Personal Care', 110000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (307, 'Sữa tắm nam X-Men', 'Personal Care', 140000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (308, 'Xịt toàn thân Axe', 'Personal Care', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (309, 'Gel vuốt tóc Gatsby', 'Personal Care', 55000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (310, 'Kem dưỡng da Pond''s', 'Personal Care', 150000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (311, 'Sữa rửa mặt Acnes', 'Personal Care', 60000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (312, 'Băng cá nhân Urgo', 'Personal Care', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (313, 'Bông ngoáy tai (hộp)', 'Personal Care', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (314, 'Nước tẩy trang Bioderma', 'Personal Care', 250000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (315, 'Chỉ nha khoa Oral-B', 'Personal Care', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (316, 'Xịt khoáng Vichy', 'Personal Care', 180000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (317, 'Sữa dưỡng thể Hazeline', 'Personal Care', 80000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (318, 'Kem cạo râu Gillette', 'Personal Care', 75000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (319, 'Mặt nạ giấy Senka', 'Personal Care', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (320, 'Dung dịch vệ sinh Lactacyd', 'Personal Care', 65000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (321, 'Lược chải tóc plastic', 'Personal Care', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (322, 'Cắt móng tay (bộ)', 'Personal Care', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (323, 'Sáp dưỡng môi Vaseline', 'Personal Care', 50000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (324, 'Dầu gội trị gàu Selsun', 'Personal Care', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (325, 'Sữa tắm Johnson Baby', 'Personal Care', 110000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (326, 'Phấn rôm Enchanteur', 'Personal Care', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (327, 'Lăn khử mùi Romano', 'Personal Care', 55000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (328, 'Kem trị mụn Decumar', 'Personal Care', 75000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (329, 'Tăm bông người lớn', 'Personal Care', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (330, 'Cồn y tế 70 độ', 'Personal Care', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (331, 'Dầu gió Trường Sơn', 'Personal Care', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (332, 'Xà bông cục Coast', 'Personal Care', 18000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (333, 'Kem dưỡng tay Kamill', 'Personal Care', 65000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (334, 'Khăn mặt bông cotton', 'Personal Care', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (335, 'Nước rửa tay Safeguard', 'Personal Care', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (336, 'Keo xịt tóc Double Rich', 'Personal Care', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (337, 'Sáp vuốt tóc nam', 'Personal Care', 120000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (338, 'Kem tẩy lông Veet', 'Personal Care', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (339, 'Xịt khử mùi chân', 'Personal Care', 75000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (340, 'Mặt nạ đất sét', 'Personal Care', 160000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (341, 'Nước hoa mini chiết', 'Personal Care', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (342, 'Kính bơi người lớn', 'Personal Care', 150000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (343, 'Mũ tắm nilon', 'Personal Care', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (344, 'Gương soi mini', 'Personal Care', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (345, 'Thun cột tóc', 'Personal Care', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (346, 'Băng vệ sinh hằng ngày', 'Personal Care', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (347, 'Dao cạo râu dùng 1 lần', 'Personal Care', 8000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (348, 'Sữa tắm trắng da', 'Personal Care', 180000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (349, 'Dầu xả tóc Tresemme', 'Personal Care', 160000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (350, 'Nước súc miệng trẻ em', 'Personal Care', 45000, 50, '');

-- ==========================================================
-- NHÓM: HOUSEHOLD (Nhu yếu phẩm gia đình) - Tổng cộng 60 món
-- ==========================================================
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (141, 'Pin AA Energizer (cặp)', 'Household', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (142, 'Bật lửa', 'Household', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (143, 'Áo mưa tiện lợi', 'Household', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (144, 'Nước rửa bát Sunlight', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (145, 'Khăn giấy ướt', 'Household', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (146, 'Bút bi Thiên Long', 'Household', 4000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (147, 'Sổ tay mini', 'Household', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (148, 'Băng keo trong', 'Household', 8000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (149, 'Dung dịch vệ sinh giày', 'Household', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (150, 'Sạc dự phòng cơ bản', 'Household', 150000, 50, '');
-- Thêm mới 50 món Household
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (351, 'Nước lau sàn Sunlight', 'Household', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (352, 'Bột giặt OMO 400g', 'Household', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (353, 'Nước xả vải Downy', 'Household', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (354, 'Túi rác đen (cuộn)', 'Household', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (355, 'Miếng rửa bát (set 3)', 'Household', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (356, 'Bàn chải giặt đồ', 'Household', 8000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (357, 'Kẹp phơi quần áo', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (358, 'Móc treo áo quần', 'Household', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (359, 'Bóng đèn LED 12W', 'Household', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (360, 'Ổ cắm điện 3 lỗ', 'Household', 65000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (361, 'Chuột máy tính không dây', 'Household', 120000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (362, 'Lót chuột gaming', 'Household', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (363, 'Tai nghe có dây', 'Household', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (364, 'Cáp sạc iPhone 1m', 'Household', 120000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (365, 'Cáp sạc Type-C', 'Household', 95000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (366, 'Gậy tự sướng mini', 'Household', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (367, 'Quạt cầm tay tích điện', 'Household', 75000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (368, 'Đèn pin siêu sáng', 'Household', 90000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (369, 'Khóa cửa đồng', 'Household', 110000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (370, 'Búa đóng đinh', 'Household', 55000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (371, 'Thước dây 5m', 'Household', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (372, 'Bút chì 2B', 'Household', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (373, 'Gôm tẩy Thiên Long', 'Household', 3000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (374, 'Thước kẻ 20cm', 'Household', 4000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (375, 'Bút dạ quang', 'Household', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (376, 'Keo 502', 'Household', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (377, 'Kéo văn phòng', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (378, 'Bấm kim số 10', 'Household', 22000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (379, 'Kim bấm số 10 (hộp)', 'Household', 5000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (380, 'Kẹp giấy (hộp)', 'Household', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (381, 'Giấy A4 (ram 500 tờ)', 'Household', 65000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (382, 'File hồ sơ kẹp', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (383, 'Bình giữ nhiệt 500ml', 'Household', 150000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (384, 'Ly thủy tinh uống nước', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (385, 'Chén sứ trắng', 'Household', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (386, 'Đũa gỗ (bộ 10 đôi)', 'Household', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (387, 'Thìa inox', 'Household', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (388, 'Hộp thực phẩm nhựa', 'Household', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (389, 'Rổ nhựa tròn', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (390, 'Thớt gỗ xà cừ', 'Household', 85000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (391, 'Dao thái thịt', 'Household', 45000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (392, 'Cọ rửa chai lọ', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (393, 'Xà phòng rửa tay', 'Household', 15000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (394, 'Túi giữ nhiệt', 'Household', 35000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (395, 'Khăn trải bàn nilon', 'Household', 25000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (396, 'Găng tay cao su', 'Household', 20000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (397, 'Khăn lau đa năng', 'Household', 10000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (398, 'Băng keo điện', 'Household', 12000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (399, 'Vợt muỗi điện', 'Household', 110000, 50, '');
INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (400, 'Nhang muỗi (hộp)', 'Household', 15000, 50, '');

INSERT INTO SANPHAM (id, ten, category, giaban, tonkho, imageurl) VALUES (3636, 'Methamphetamine', 'Hàng_hiếm', 367367420, 102, '');


COMMIT;