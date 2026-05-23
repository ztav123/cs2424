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
    
    private String ketThucTime; 
    @JsonProperty("mayPos")
    private Integer mayPos;
    public Integer getMayPos() { return mayPos; }
    public void setMayPos(Integer mayPos) { this.mayPos = mayPos; }
}