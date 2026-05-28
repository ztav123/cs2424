package com.pos.pos_system.dto;

import java.util.List;

public class ThongKeResponse {
    private Double tongDoanhThu;
    private Long soDonHang;
    private String spBanChayNhat;
    private List<DailyReport> chiTietNgay; // Danh sách các dòng cho bảng

    public ThongKeResponse() {}

    public ThongKeResponse(Double tongDoanhThu, Long soDonHang, String spBanChayNhat, List<DailyReport> chiTietNgay) {
        this.tongDoanhThu = tongDoanhThu;
        this.soDonHang = soDonHang;
        this.spBanChayNhat = spBanChayNhat;
        this.chiTietNgay = chiTietNgay;
    }

    // Getters and Setters
    public Double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(Double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
    
    public Long getSoDonHang() { return soDonHang; }
    public void setSoDonHang(Long soDonHang) { this.soDonHang = soDonHang; }
    
    public String getSpBanChayNhat() { return spBanChayNhat; }
    public void setSpBanChayNhat(String spBanChayNhat) { this.spBanChayNhat = spBanChayNhat; }
    
    public List<DailyReport> getChiTietNgay() { return chiTietNgay; }
    public void setChiTietNgay(List<DailyReport> chiTietNgay) { this.chiTietNgay = chiTietNgay; }

    // ================= LỚP NỘI BỘ ĐẠI DIỆN CHO 1 DÒNG TRONG BẢNG =================
    public static class DailyReport {
        private String ngay;
        private Double doanhThu;
        private String spBanChay;

        public DailyReport() {}

        public DailyReport(String ngay, Double doanhThu, String spBanChay) {
            this.ngay = ngay;
            this.doanhThu = doanhThu;
            this.spBanChay = spBanChay;
        }

        public String getNgay() { return ngay; }
        public void setNgay(String ngay) { this.ngay = ngay; }

        public Double getDoanhThu() { return doanhThu; }
        public void setDoanhThu(Double doanhThu) { this.doanhThu = doanhThu; }

        public String getSpBanChay() { return spBanChay; }
        public void setSpBanChay(String spBanChay) { this.spBanChay = spBanChay; }
    }
}