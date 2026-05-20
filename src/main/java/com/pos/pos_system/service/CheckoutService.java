package com.pos.pos_system.service;

import com.pos.pos_system.dto.OrderRequest;
import com.pos.pos_system.entity.CTHD;
import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.CTHDRepository;
import com.pos.pos_system.repository.HoaDonRepository;
import com.pos.pos_system.repository.KhachHangRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import com.pos.pos_system.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import org.springframework.transaction.annotation.Isolation;

@Service
public class CheckoutService {
    @Autowired private HoaDonRepository hoaDonRepo;
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private KhachHangRepository khachHangRepo; // Thêm dòng này
    @Autowired private CTHDRepository cthdRepo; // Thêm dòng này
    @Autowired private SanPhamRepository sanPhamRepo; // Thêm dòng này

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HoaDon processOrder(OrderRequest req) {
        // =========================================================================
        // TUYỆT CHIÊU CHỐNG DEADLOCK & LOST UPDATE
        // =========================================================================
        
        // 1. Gom tất cả ID Sản phẩm trong đơn hàng và SẮP XẾP TĂNG DẦN (Lock Ordering)
        List<Integer> productIds = req.getChiTiet().stream()
                .map(OrderRequest.OrderItem::getSanPhamId)
                .sorted() // Cực kỳ quan trọng để chống Deadlock
                .distinct()
                .collect(Collectors.toList());

        // 2. Kích hoạt câu lệnh SELECT ... FOR UPDATE dưới Oracle DB
        // Máy 2 khi chạy đến dòng này sẽ bị "bắt đứng chờ" nếu Máy 1 đang thanh toán các ID này
        List<SanPham> lockedProducts = sanPhamRepo.findByIdsForUpdate(productIds);
        
        Map<Integer, SanPham> productMap = lockedProducts.stream()
                .collect(Collectors.toMap(SanPham::getId, p -> p));

        // 3. Kiểm tra tồn kho THỰC TẾ ngay tại thời điểm đã cầm chắc Lock trong tay
        for (OrderRequest.OrderItem item : req.getChiTiet()) {
            SanPham sp = productMap.get(item.getSanPhamId());
            if (sp == null) {
                throw new RuntimeException("Sản phẩm ID " + item.getSanPhamId() + " không tồn tại!");
            }
            if (sp.getTonkho() < item.getSoLuong()) {
                throw new RuntimeException("Hết hàng! [" + sp.getTen() + "] chỉ còn " + sp.getTonkho() + " cái trên kệ.");
            }
        }

        // =========================================================================
        // TỒN KHO ĐÃ ĐẢM BẢO AN TOÀN -> BẮT ĐẦU LƯU HÓA ĐƠN
        // =========================================================================
        HoaDon hd = new HoaDon();
        hd.setId(req.getId());
        // 1. Khởi tạo và gán thông tin vỏ Hóa đơn
        hd.setId(req.getId());
        hd.setNhanVien(nhanVienRepo.findById(req.getNhanVienId()).orElse(null));
        
        if (req.getKhachHangSdt() != null && !req.getKhachHangSdt().isEmpty()) {
            hd.setKhachHang(khachHangRepo.findById(req.getKhachHangSdt()).orElse(null));
        }

        hd.setNgaylap(LocalDateTime.now());
        hd.setTongtien(Long.valueOf(req.getTongTien()));
        hd.setPhuongthucTt(req.getPhuongThucThanhToan());
        hd.setGiamgia(0.0);

        if ("Ngân hàng".equalsIgnoreCase(req.getPhuongThucThanhToan()) || "VNPay".equalsIgnoreCase(req.getPhuongThucThanhToan())) {
            hd.setTrangthai("CHO_THANH_TOAN");
        } else {
            hd.setTrangthai("HOAN_THANH");
        }

        // Lưu vỏ Hóa đơn xuống Database trước để đồng bộ trạng thái
        HoaDon savedHd = hoaDonRepo.save(hd); 

        // 2. Gom danh sách CTHD vào một List tạm (KHÔNG gọi cthdRepo.save lẻ tẻ bên trong vòng lặp)
        List<CTHD> listChiTiet = new ArrayList<>();
        
        if (req.getChiTiet() != null) {
            for (OrderRequest.OrderItem item : req.getChiTiet()) {
                CTHD cthd = new CTHD();
                cthd.setHoadon(savedHd); // Gán thực thể cha đã cứu cánh cache
                cthd.setSanPham(sanPhamRepo.findById(item.getSanPhamId()).orElse(null));
                cthd.setSoLuong(item.getSoLuong());
                cthd.setDonGia(Double.valueOf(item.getDonGia()));
                
                listChiTiet.add(cthd); // Chỉ add vào danh sách tạm trên RAM
            }
        }

        // 3. DÙNG SAVE_ALL ĐỂ HIBERNATE TỰ BĂM CHUỖI BATCH INSERT AN TOÀN VÀ TỐI ƯU TỐC ĐỘ
        if (!listChiTiet.isEmpty()) {
            List<CTHD> savedChiTiet = cthdRepo.saveAll(listChiTiet);
            savedHd.setChiTiet(savedChiTiet); // Gắn ngược lại danh sách đã persistent vào hóa đơn
        } else {
            savedHd.setChiTiet(new ArrayList<>());
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
    @Transactional
    public void processSuccessfulOrder(HoaDon hd) {
        hd.setTrangthai("HOAN_THANH");
        hoaDonRepo.save(hd);
    }
}