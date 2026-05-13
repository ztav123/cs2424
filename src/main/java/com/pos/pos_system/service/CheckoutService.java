package com.pos.pos_system.service;

import com.pos.pos_system.dto.OrderRequest;
import com.pos.pos_system.entity.CTHD;
import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.CTHDRepository;
import com.pos.pos_system.repository.HoaDonRepository;
import com.pos.pos_system.repository.SanPhamRepository;
import com.pos.pos_system.repository.KhachHangRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CheckoutService {
    @Autowired private HoaDonRepository hoaDonRepo;
    @Autowired private CTHDRepository cthdRepo;
    @Autowired private SanPhamRepository sanPhamRepo;
    @Autowired private KhachHangRepository khachHangRepo;
    @Autowired private NhanVienRepository nhanVienRepo;

    @Transactional
    public HoaDon processOrder(OrderRequest req) {
        HoaDon hd = new HoaDon();
        
        // --- NHẬN ID DẠNG CHUỖI TỪ FRONTEND ---
        hd.setId(req.getId()); 
        
        hd.setNhanVien(nhanVienRepo.findById(req.getNhanVienId()).orElse(null));
        
        if (req.getKhachHangSdt() != null && !req.getKhachHangSdt().isEmpty()) {
            hd.setKhachHang(khachHangRepo.findById(req.getKhachHangSdt()).orElse(null));
            hd.getKhachHang().setDiemtichluy(hd.getKhachHang().getDiemtichluy() + (int)(req.getTongTien() / 10000));
            khachHangRepo.save(hd.getKhachHang());
        }
        
        // Xóa dòng hd.setId(null); cũ đi
        hd.setNgaylap(LocalDateTime.now());
        hd.setTongtien(Double.valueOf(req.getTongTien())); 
        hd.setPhuongthucTt(req.getPhuongThucThanhToan()); 
        hd.setNgaylap(LocalDateTime.now());
        hd.setTongtien(Double.valueOf(req.getTongTien())); 
        hd.setPhuongthucTt(req.getPhuongThucThanhToan()); 
        
        // THAY ĐỔI Ở ĐÂY: Nếu là Ngân hàng thì chuyển trạng thái thành Chờ
        if ("Ngân hàng".equalsIgnoreCase(req.getPhuongThucThanhToan())) {
            hd.setTrangthai("CHO_THANH_TOAN");
        } else {
            hd.setTrangthai("HOAN_THANH");
        }

        HoaDon savedHd = hoaDonRepo.save(hd);

        for (OrderRequest.OrderItem item : req.getChiTiet()) {
            CTHD cthd = new CTHD();
            cthd.setHoadonid(savedHd.getId());
            cthd.setSanphamid(item.getSanPhamId());
            cthd.setSoluong(item.getSoLuong());
            cthd.setDongia(item.getDonGia());
            cthdRepo.save(cthd);

            // Trừ Kho
            SanPham sp = sanPhamRepo.findById(item.getSanPhamId()).orElseThrow(() -> new RuntimeException("Lỗi SP"));
            if (sp.getTonkho() < item.getSoLuong()) {
                throw new RuntimeException("Sản phẩm " + sp.getTen() + " không đủ tồn kho!");
            }
            sp.setTonkho(sp.getTonkho() - item.getSoLuong());
            sanPhamRepo.save(sp);
        }
        
        return savedHd;
    }

    @Transactional
    public void updateOrderStatus(String orderId, String status) {
        HoaDon hd = hoaDonRepo.findById(orderId).orElse(null);
        if (hd != null) {
            hd.setTrangthai(status);
            hoaDonRepo.save(hd);
        }
    }
}