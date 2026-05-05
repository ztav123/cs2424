package com.pos.pos_system.controller;

import com.pos.pos_system.dto.OrderRequest;
import com.pos.pos_system.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin("*")
public class CheckoutController {
    @Autowired private CheckoutService checkoutService;

    @PostMapping("/thanhtoan")
    public ResponseEntity<?> thanhToan(@RequestBody OrderRequest req) {
        try {
            // Điểm neo VNPay: Nếu req.getPhuongThucThanhToan().equals("Ngân hàng") 
            // -> Gọi hàm tạo URL VNPay và trả về URL ở đây.
            
            checkoutService.processOrder(req);
            return ResponseEntity.ok("Thanh toán thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi thanh toán: " + e.getMessage());
        }
    }
}