package com.pos.pos_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // Thêm import này
import org.springframework.web.cors.CorsConfigurationSource; // Thêm import này
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Thêm import này

import com.pos.pos_system.security.JwtAuthenticationFilter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    // CẤU HÌNH BỘ LỌC CORS CHUẨN ĐỂ KHÔNG BỊ CHẶN KHI SERVER TRẢ VỀ LỖI
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Cho phép mọi Origin gọi tới
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            // THAY THẾ DÒNG .cors.disable() THÀNH DÒNG DƯỚI ĐÂY:
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                .requestMatchers("/api/auth/login", "/api/auth/exit").permitAll() 
                .requestMatchers("/api/checkout/vnpay-return").permitAll()
                .requestMatchers("/api/checkout/**").hasAnyAuthority("ADMIN", "NHANVIEN")
                .requestMatchers("/api/calam/**").hasAnyAuthority("ADMIN", "NHANVIEN")
                .requestMatchers("/api/store/**").hasAnyAuthority("ADMIN", "NHANVIEN")
                .requestMatchers("/api/ketca/**").hasAnyAuthority("ADMIN", "NHANVIEN")
                .anyRequest().hasAnyAuthority("ADMIN", "NHANVIEN") 
            );
            
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}