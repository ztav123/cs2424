package com.pos.pos_system.service;

import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.repository.CaLamRepository;
import com.pos.pos_system.repository.HoaDonRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder; 
import com.pos.pos_system.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private HoaDonRepository hoaDonRepo;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private JwtTokenProvider tokenProvider;

    @Transactional
    public Map<String, Object> loginAndManageShift(Integer newId, String password, Integer oldId, boolean createShift, Integer mayPos) {
        NhanVien nv = nhanVienRepo.findById(newId).orElse(null);
        System.out.println("=== DEBUG ĐĂNG NHẬP ===");
        System.out.println("1. ID từ Frontend gửi lên: " + newId);
        System.out.println("2. Mật khẩu từ Frontend gửi lên: [" + password + "]");
        if (nv != null) {
            System.out.println("3. Mật khẩu mã hóa trong DB: [" + nv.getMkdangnhap() + "]");
            System.out.println("4. Trạng thái hoạt động: " + nv.getTrangthai());
            System.out.println("5. Kết quả so khớp: " + passwordEncoder.matches(password, nv.getMkdangnhap()));
            System.out.println("5. MatKhau mã hóa: " + passwordEncoder.encode(password));
            System.out.println("6. Trang thai nhan vien: " + (nv.getTrangthai() == 1 ? "Đang làm" : "Nghỉ việc"));
             
        } else {
            System.out.println("3. KHÔNG TÌM THẤY NHÂN VIÊN TRONG DB!");
        }
        System.out.println("=======================");
        if (nv == null || !passwordEncoder.matches(password, nv.getMkdangnhap()) || nv.getTrangthai() == 0) return null; 
        

        String role = "ADMIN".equalsIgnoreCase(nv.getVaitro()) ? "ADMIN" : "NHANVIEN";
        String token = tokenProvider.generateToken(nv.getId(), role);

        // 2. CHUẨN BỊ RESPONSE (Gắn thêm Token vào)
        Map<String, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("id", nv.getId());
        response.put("hoten", nv.getHoten());
        response.put("accessToken", token); // <-- ĐÂY LÀ ĐIỂM KHÁC BIỆT

        if ("ADMIN".equalsIgnoreCase(role)) {
            return response; 
        }

        // ==========================================
        // BƯỚC 1: ĐÓNG CA LÀM CŨ
        // ==========================================
        if (oldId != null) {
            List<CaLam> activeShifts = caLamRepo.findByNhanVienIdAndKetthucIsNull(oldId);
            for (CaLam shift : activeShifts) {
                LocalDateTime endTime = LocalDateTime.now();
                shift.setKetthuc(endTime);
                
                // LẤY TẤT CẢ HÓA ĐƠN TRONG CA NÀY
                List<HoaDon> hoaDons = hoaDonRepo.findByNhanVienIdAndNgaylapBetween(oldId, shift.getBatdau(), endTime);
                double sysCash = 0;
                double sysBank = 0;
                
                for (HoaDon hd : hoaDons) {
                    if ("Tiền mặt".equalsIgnoreCase(hd.getPhuongthucTt())) {
                        sysCash += (hd.getTongtien() != null ? hd.getTongtien() : 0);
                    } else {
                        sysBank += (hd.getTongtien() != null ? hd.getTongtien() : 0);
                    }
                }

                // Gán số liệu hệ thống tính toán được vào Ca làm
                shift.setTienNganhang(sysBank);
                shift.setTongDoanhthu(sysCash + sysBank); 
                
                // Lưu ý: Lúc này chưa set tienmatKetca và tongtienThucte
                // Nó sẽ được API save-log bên KetCaController cập nhật sau khi đếm tiền
                caLamRepo.save(shift);
            }
        }

        // ==========================================
        // BƯỚC 2: MỞ CA LÀM MỚI (Lấy tiền từ ca cũ)
        // ==========================================
        if (createShift) {
            Double beginCash = 0.0;
            
            // Tìm ca làm gần nhất trong hệ thống (chính là ca vừa đóng ở trên và đã đếm tiền)
            CaLam lastShift = caLamRepo.findTopByOrderByBatdauDesc();
            if (lastShift != null && lastShift.getTienmatKetca() != null) {
                beginCash = lastShift.getTienmatKetca(); // Chuyền tiền mặt qua ca mới
            }
            
            CaLam newShift = new CaLam();
            newShift.setNhanVien(nv);
            newShift.setBatdau(LocalDateTime.now());
            newShift.setTienmatBandau(beginCash); // Gán tiền mặt ban đầu
            newShift.setMayPos(mayPos); // Gán số máy POS
            caLamRepo.save(newShift);
        }
        return response;
    }
    @Transactional
    public Map<String, Object> employeeExit(Integer newId, String password) {
        NhanVien nv = nhanVienRepo.findById(newId).orElse(null);

        if (nv == null || !passwordEncoder.matches(password, nv.getMkdangnhap()) || nv.getTrangthai() == 0) return null; 

        String role = "ADMIN".equalsIgnoreCase(nv.getVaitro()) ? "ADMIN" : "NHANVIEN";
        String token = tokenProvider.generateToken(nv.getId(), role);

        Map<String, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("id", nv.getId());
        response.put("hoten", nv.getHoten());
        response.put("accessToken", token); 

        if ("ADMIN".equalsIgnoreCase(role)) {
            return response; 
        }
        return null;
    }
}