package com.pos.pos_system.service;

import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCleanupJob {

    @Autowired
    private HoaDonRepository hoaDonRepo;

    // Chạy ngầm định kỳ mỗi phút 1 lần (cron: giây, phút, giờ, ngày, tháng, thứ)
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cancelTimeoutVNPayOrders() {
        // Tìm thời điểm cách đây đúng 5 phút
        LocalDateTime fiveMinsAgo = LocalDateTime.now().minusMinutes(5);

        // Tìm các đơn đang bị treo "CHO_THANH_TOAN" và đã quá hạn 5 phút
        List<HoaDon> pendingOrders = hoaDonRepo.findByTrangthaiAndNgaylapBefore("CHO_THANH_TOAN", fiveMinsAgo);

        for (HoaDon hd : pendingOrders) {
            hd.setTrangthai("THAT_BAI"); // Đánh dấu thất bại
            hoaDonRepo.save(hd);
            System.out.println("Đã tự động hủy đơn hàng quá hạn thanh toán: " + hd.getId());
            // Vì trạng thái đổi sang THAT_BAI (không phải HOAN_THANH) nên Trigger Oracle sẽ KO trừ tồn kho. Cực kỳ an toàn!
        }
    }
}