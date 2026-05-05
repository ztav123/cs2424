package com.pos.pos_system.service;

import com.pos.pos_system.dto.OrderRequest;
import com.pos.pos_system.entity.*;
import com.pos.pos_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CheckoutService {

    @Autowired private HoaDonRepository hoaDonRepo;
    @Autowired private CTHDRepository cthdRepo;
    @Autowired private KhoRepository khoRepo;
    @Autowired private KhachHangRepository khachHangRepo;
    @Autowired private ThanhToanRepository thanhToanRepo;
    @Autowired private NhanVienRepository nhanVienRepo;

    @Transactional // Đảm bảo lỗi ở đâu thì Rollback toàn bộ
    public HoaDon processOrder(OrderRequest req) {
        // 1. Tạo Hóa Đơn
        HoaDon hd = new HoaDon();
        // Để tạm random ID cho Hóa đơn (Trong thực tế nên cấu hình auto_increment cho HOADON.id)
        hd.setId((int)(Math.random() * 100000)); 
        hd.setNhanVien(nhanVienRepo.findById(req.getNhanVienId()).orElse(null));
        hd.setNgayLap(LocalDateTime.now());
        hd.setTrangthai("HOAN_THANH"); // Sau này tích hợp VNPay có thể đổi thành "CHO_THANH_TOAN"
        hd.setTongtien(req.getTongTien());

        // 2. Xử lý Khách Hàng (Tích điểm)
        if (req.getKhachHangSdt() != null && !req.getKhachHangSdt().isEmpty()) {
            KhachHang kh = khachHangRepo.findById(req.getKhachHangSdt()).orElse(null);
            if (kh != null) {
                hd.setKhachHang(kh);
                int diemCong = req.getTongTien() / 10000;
                kh.setDiem(kh.getDiem() + diemCong);
                khachHangRepo.save(kh); // Cập nhật điểm
            }
        }
        HoaDon savedHd = hoaDonRepo.save(hd);

        // 3. Xử lý Chi Tiết Hóa Đơn & Trừ Kho
        for (OrderRequest.OrderItem item : req.getChiTiet()) {
            // Lưu CTHD
            CTHD cthd = new CTHD();
            cthd.setHoadonid(savedHd.getId());
            cthd.setSanphamid(item.getSanPhamId());
            cthd.setSoluong(item.getSoLuong());
            cthd.setDongia(item.getDonGia());
            cthdRepo.save(cthd);

            // Trừ Kho
            Kho kho = khoRepo.findBySanphamid(item.getSanPhamId());
            if (kho != null) {
                if (kho.getTonkho() < item.getSoLuong()) {
                    throw new RuntimeException("Sản phẩm ID " + item.getSanPhamId() + " không đủ tồn kho!");
                }
                kho.setTonkho(kho.getTonkho() - item.getSoLuong());
                khoRepo.save(kho);
            }
            // Lưu ý: Nếu DB chưa có dữ liệu tồn kho, code vẫn chạy qua mà không lỗi nhờ check (kho != null).
            // Rất phù hợp để sau này bạn làm tính năng Nhập kho sau.
        }

        // 4. Lưu lịch sử thanh toán
        ThanhToan tt = new ThanhToan();
        tt.setHoaDon(savedHd);
        tt.setPhuongthuc(req.getPhuongThucThanhToan());
        tt.setNgayThanhToan(LocalDateTime.now());
        tt.setSotien(req.getTongTien());
        thanhToanRepo.save(tt);

        return savedHd;
    }
}