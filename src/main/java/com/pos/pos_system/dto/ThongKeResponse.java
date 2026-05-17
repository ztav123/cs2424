package com.pos.pos_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ThongKeResponse {
    private Double tongDoanhThu;
    private Long soDonHang;
    private String spBanChayNhat; // Tên sản phẩm bán chạy nhất tổng thể trong kỳ
    private List<BieuDoData> bieuDo;

    @Data
    @AllArgsConstructor
    public static class BieuDoData {
        private String ngay;
        private Double doanhThu;
        private String spBanChay; // Tên SP bán chạy nhất của ngày đó
    }
}