package com.pos.pos_system.dto;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PayLoadTienMat {
    // Chỉ định rõ tên key JSON map với biến này để không bao giờ bị lỗi
    @JsonProperty("nhanVienId") 
    private Integer nhanVienId;   
    
    @JsonProperty("tienmat")
    private Double tienmat;
    
    private Boolean quanly;
}