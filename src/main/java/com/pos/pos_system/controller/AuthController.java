package com.pos.pos_system.controller;

import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam Integer id, 
            @RequestParam String password,
            // (required = false) nghĩa là khi login bình thường không cần gửi oldId
            @RequestParam(required = false) Integer oldId) { 
        
        NhanVien nv = authService.loginAndManageShift(id, password, oldId);
        if (nv != null) {
            return ResponseEntity.ok(nv);
        }
        return ResponseEntity.status(401).body("Sai thông tin đăng nhập");
    }
}