package com.pos.pos_system.controller;

import com.pos.pos_system.dto.OrderRequest;
import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.repository.HoaDonRepository;
import com.pos.pos_system.service.CheckoutService;
import com.pos.pos_system.service.PdfService4HoaDon;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin("*")

public class CheckoutController {
    @Autowired private CheckoutService checkoutService;
    @Autowired private HoaDonRepository hoaDonRepo;
    @Autowired private PdfService4HoaDon pdfService4HoaDon;

    @Value("${vnpay.tmn-code}") private String vnp_TmnCode;
    @Value("${vnpay.hash-secret}") private String vnp_HashSecret;
    
    private String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private String vnp_ReturnUrl = "http://localhost:8080/api/checkout/vnpay-return";

    @PostMapping("/thanhtoan")
    public ResponseEntity<?> thanhToan(@RequestBody OrderRequest req) {
        try {
            // 1. Lưu Hóa đơn vào DB (Trạng thái CHO_THANH_TOAN nếu là Ngân Hàng)
            checkoutService.processOrder(req);

            // 2. Nếu là Ngân hàng -> Tạo URL VNPay và gửi về Frontend
            if ("Ngân hàng".equalsIgnoreCase(req.getPhuongThucThanhToan())) {
                String paymentUrl = generateVNPayUrl(req.getTongTien().longValue(), req.getId());
                // Trả về JSON chứa URL
                return ResponseEntity.ok(Map.of("url", paymentUrl));
            }

            // 3. Nếu là Tiền mặt / Momo -> Trả về thông báo thành công bình thường
            return ResponseEntity.ok(Map.of("message", "Thanh toán " + req.getPhuongThucThanhToan() + " thành công!"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi thanh toán: " + e.getMessage()));
        }
    }

    // =======================================================
    // API HỨNG KẾT QUẢ TỪ VNPAY TRẢ VỀ (TỰ ĐỘNG CHUYỂN TRANG)
    // =======================================================
    @GetMapping(value = "/vnpay-return", produces = "text/html; charset=UTF-8")
    public String vnpayReturn(@RequestParam Map<String, String> queryParams) {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String vnp_TxnRef = queryParams.get("vnp_TxnRef");
        String orderId = vnp_TxnRef.split("-")[0]; 

        if ("00".equals(vnp_ResponseCode)) {
            
            // =================================================================================
            // FALLBACK CHỐNG LỖI LOCALHOST: 
            // Nếu VNPay không gọi được IPN (do chạy localhost), ta update tạm ở đây để có dữ liệu
            // Nếu IPN đã chạy thành công trước đó (khi up lên server thật), đoạn này sẽ bỏ qua
            // =================================================================================
            HoaDon hd = hoaDonRepo.findById(orderId).orElse(null);
            if (hd != null && "CHO_THANH_TOAN".equals(hd.getTrangthai())) {
                checkoutService.updateOrderStatus(orderId, "HOAN_THANH");
                System.out.println("Return Fallback: Đã cập nhật thành công đơn hàng " + orderId);
            }

            // ĐÃ SỬA: Đưa url về đúng localhost:8080 để trình duyệt không làm mất Token
            return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'>" +
                   "<meta http-equiv='refresh' content='5;url=http://localhost:8080/orderPage.html?printBill=" + orderId + "' />" +
                   "<title>Thanh toán thành công</title>" +
                   "<style>body{font-family:Arial;text-align:center;padding-top:100px;background:#eaf1f7;}</style>" +
                   "</head><body>" +
                   "<h1 style='color:#1a9e2a;'>✅ GIAO DỊCH THÀNH CÔNG!</h1>" +
                   "<h3>Mã hóa đơn: " + orderId + "</h3>" +
                   "<p>Hệ thống tự động in hóa đơn và quay về trang Bán Hàng sau 5 giây...</p>" +
                   "<button onclick='window.location.href=\"http://localhost:8080/orderPage.html?printBill=" + orderId + "\"' style='padding:10px 20px;background:#0b5aa6;color:white;border:none;border-radius:5px;cursor:pointer;font-size:16px;margin-top:20px;'>Quay về & In Bill</button>" +
                   "</body></html>";
        } else {
            // FALLBACK KHI THANH TOÁN THẤT BẠI
            HoaDon hd = hoaDonRepo.findById(orderId).orElse(null);
            if (hd != null && "CHO_THANH_TOAN".equals(hd.getTrangthai())) {
                checkoutService.updateOrderStatus(orderId, "THAT_BAI");
            }
            
            // ĐÃ SỬA: Đưa url về đúng localhost:8080
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
                   "<meta http-equiv='refresh' content='5;url=http://localhost:8080/orderPage.html' />" +
                   "</head>" +
                   "<body style='text-align:center;padding-top:100px;font-family:Arial;'>" +
                   "<h1 style='color:red;'>❌ GIAO DỊCH THẤT BẠI HOẶC BỊ HỦY!</h1>" +
                   "<p>Tự động quay về trang Bán Hàng sau 5 giây...</p></body></html>";
        }
    }
    @Autowired private com.pos.pos_system.repository.CTHDRepository cthdRepo; // Thêm dòng này

    @GetMapping(value = "/export-pdf/{id}", produces = "application/pdf")
    public void exportReceiptPDF(HttpServletResponse response, @PathVariable String id) throws IOException {        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=HoaDon_" + id + ".pdf");

        HoaDon hd = hoaDonRepo.findById(id).orElse(null);
        if (hd != null) {
            // Ép nạp danh sách sản phẩm từ DB sạch không qua bộ nhớ đệm Hibernate Cache
            hd.setChiTiet(cthdRepo.findByHoadon_Id(id)); 
            
            // Tiến hành ghi file xuất trực tiếp ra luồng Output của Response
            pdfService4HoaDon.exportReceipt(response, hd);
        }
    }

    // =======================================================
    // HÀM HELPER: TẠO URL VNPAY (Lấy từ demo của bạn)
    // =======================================================
    private String generateVNPayUrl(Long amountParam, String orderId) throws Exception {
        long amount = amountParam * 100; 
        String vnp_TxnRef = orderId + "-" + System.currentTimeMillis();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (fieldNames.indexOf(fieldName) != fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return vnp_Url + "?" + query.toString();
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(2 * result.length);
        for (byte b : result) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateInvoiceStatus(@PathVariable String id, @RequestParam String status) {
        try {
            // Kiểm tra trạng thái hợp lệ tránh lỗi truyền chuỗi rác
            if (!"HOAN_THANH".equalsIgnoreCase(status) && !"THAT_BAI".equalsIgnoreCase(status) && !"CHO_THANH_TOAN".equalsIgnoreCase(status)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Trạng thái không hợp lệ!"));
            }
            
            // Gọi hàm xử lý Transactional có sẵn trong CheckoutService
            checkoutService.updateOrderStatus(id, status.toUpperCase());
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái hóa đơn thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi: " + e.getMessage()));
        }
    }
}