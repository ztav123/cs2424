package com.pos.pos_system.dto;

import java.util.List;

public class ThongKeResponse {
    private Double tongDoanhThu;
    private Long soDonHang;
    private String spBanChayNhat;
    private List<String> labels; 
    private List<ChartDataset> datasets; 

    // Constructor không tham số (Bắt buộc cho Jackson)
    public ThongKeResponse() {
    }

    // Constructor đầy đủ tham số
    public ThongKeResponse(Double tongDoanhThu, Long soDonHang, String spBanChayNhat, List<String> labels, List<ChartDataset> datasets) {
        this.tongDoanhThu = tongDoanhThu;
        this.soDonHang = soDonHang;
        this.spBanChayNhat = spBanChayNhat;
        this.labels = labels;
        this.datasets = datasets;
    }

    // ================= GETTER VÀ SETTER =================

    public Double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(Double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public Long getSoDonHang() {
        return soDonHang;
    }

    public void setSoDonHang(Long soDonHang) {
        this.soDonHang = soDonHang;
    }

    public String getSpBanChayNhat() {
        return spBanChayNhat;
    }

    public void setSpBanChayNhat(String spBanChayNhat) {
        this.spBanChayNhat = spBanChayNhat;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<ChartDataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ChartDataset> datasets) {
        this.datasets = datasets;
    }

    // ================= LỚP NỘI BỘ (INNER CLASS) =================
    public static class ChartDataset {
        private String label;
        private List<Double> data;

        // Constructor không tham số
        public ChartDataset() {
        }

        // Constructor đầy đủ tham số
        public ChartDataset(String label, List<Double> data) {
            this.label = label;
            this.data = data;
        }

        // Getter và Setter cho Inner Class
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<Double> getData() {
            return data;
        }

        public void setData(List<Double> data) {
            this.data = data;
        }
    }
}