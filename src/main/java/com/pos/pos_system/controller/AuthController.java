package com.pos.pos_system.controller;

import com.pos.pos_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") // Mở CORS để Frontend ở mọi port có thể gọi tới[cite: 9]
public class AuthController {

    @Autowired 
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam Integer id, 
            @RequestParam String password,
            @RequestParam(required = false) Integer oldId,
            @RequestParam(required = false, defaultValue = "true") boolean createShift,
            @RequestParam(required = false, defaultValue = "1") Integer mayPos) { 
        
        // Nhận về 1 Map chứa thông tin Role, ID và Họ tên[cite: 9]
        Map<String, Object> result = authService.loginAndManageShift(id, password, oldId, createShift, mayPos); // Giả sử mayPos là 1
        
        if (result != null) {
            // Trả về HTTP Status 200 (OK) và data Map dạng JSON[cite: 9]
            return ResponseEntity.ok(result);
        }
        
        // Trả về HTTP Status 401 (Unauthorized) nếu đăng nhập thất bại[cite: 9]
        return ResponseEntity.status(401).body("Sai thông tin đăng nhập");
    }
    @PostMapping("/exit")
    public ResponseEntity<?> exit(
            @RequestParam Integer id, 
            @RequestParam String password) { 

        Map<String, Object> result = authService.employeeExit(id, password);
        
        if (result != null) {
            // Trả về HTTP Status 200 (OK) và data Map dạng JSON[cite: 9]
            return ResponseEntity.ok(result);
        }
        
        // Trả về HTTP Status 401 (Unauthorized) nếu đăng nhập thất bại[cite: 9]
        return ResponseEntity.status(401).body("Chỉ có quản lý mới có thể cho nhân viên đăng xuất!");
    

    }
    
}