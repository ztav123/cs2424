 package com.pos.pos_system.dto;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PayLoadTienMat {
    @JsonProperty("nhanVienId") 
    private Integer nhanVienId;   
    
    @JsonProperty("tienmat")
    private Double tienmat;
    
    private Boolean quanly;
    
    // THÊM TRƯỜNG NÀY: Để nhận chuỗi thời gian đóng ca từ Frontend
    private String ketThucTime; 
}