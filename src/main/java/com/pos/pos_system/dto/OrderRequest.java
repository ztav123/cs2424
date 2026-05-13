package com.pos.pos_system.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String id;   
    private Integer nhanVienId;
    private String khachHangSdt; // Có thể null nếu là khách lẻ
    private Integer tongTien;
    private String phuongThucThanhToan;
    private List<OrderItem> chiTiet;

    @Data
    public static class OrderItem {
        private Integer sanPhamId;
        private Integer soLuong;
        private Integer donGia;
    }
}