package com.pos.pos_system.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;

@Component
public class JwtTokenProvider {

    // Chữ ký bí mật của Server (Cực kỳ quan trọng). Nên là một chuỗi ngẫu nhiên dài.
    private final String JWT_SECRET = "DayLaChuKyBiMatCuaHeThongPOSSieuCapVipPro123456789";
    
    // Thời gian sống của Token (Ví dụ: 12 giờ = 43200000 miligiây)
    private final long JWT_EXPIRATION = 43200000L; 

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    // Hàm tạo JWT Token từ thông tin nhân viên
    public String generateToken(Integer id, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(id)) // Lưu ID nhân viên vào Token
                .claim("role", role)            // Lưu Quyền vào Token
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 1. Hàm giải mã lấy ID từ Token
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject()); // Lấy ID ra
    }

    // 2. Hàm giải mã lấy Quyền (Role) từ Token
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class); // Lấy chữ "ADMIN" hoặc "NHANVIEN" ra
    }

    // 3. Hàm kiểm tra Token có hợp lệ / hết hạn chưa
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.out.println("Token không hợp lệ hoặc đã hết hạn: " + ex.getMessage());
        }
        return false;
    }
    
    // Các hàm kiểm tra và lấy thông tin từ Token sẽ được thêm vào khi làm Filter đánh chặn
}