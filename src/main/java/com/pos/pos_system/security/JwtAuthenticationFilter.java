package com.pos.pos_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Móc Token từ Header của request ra
            String jwt = getJwtFromRequest(request);

            // 2. Nếu có Token và Token đó xịn (không bị làm giả, chưa hết hạn)
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                
                // Đọc ID và Quyền từ thẻ
                Integer userId = tokenProvider.getUserIdFromToken(jwt);
                String role = tokenProvider.getRoleFromToken(jwt);

                // 3. Báo cáo với Spring Security: "Người này an toàn, hãy cho vào với quyền này!"
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            System.out.println("Lỗi không thể xác thực người dùng: " + ex.getMessage());
        }

        // Cho phép request đi tiếp vào Controller (nếu pass) hoặc bị chặn (nếu fail)
        filterChain.doFilter(request, response);
    }

    // Hàm phụ: Tìm Token trong Header có tên là "Authorization"
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Theo chuẩn quốc tế, Token thường bắt đầu bằng chữ "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}