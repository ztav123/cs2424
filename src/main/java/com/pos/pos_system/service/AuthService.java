package com.pos.pos_system.service;

import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.entity.KetCaLog;
import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.entity.QuanLy;
import com.pos.pos_system.entity.ThanhToan;
import com.pos.pos_system.repository.CaLamRepository;
import com.pos.pos_system.repository.KetCaLogRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import com.pos.pos_system.repository.QuanLyRepository;
import com.pos.pos_system.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private QuanLyRepository quanLyRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private ThanhToanRepository thanhToanRepo;
    @Autowired private KetCaLogRepository ketCaLogRepo;

    @Transactional
    public Map<String, Object> loginAndManageShift(Integer newId, String password, Integer oldId, boolean createShift) {
        NhanVien nv = nhanVienRepo.findById(newId).orElse(null);
        // 1. KIỂM TRA XEM CÓ PHẢI LÀ QUẢN LÝ KHÔNG[cite: 8]
        QuanLy ql = quanLyRepo.findById(newId).orElse(null);
        if (ql != null && ql.getMkdangnhap().equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("role", "QuanLy");
            response.put("id", ql.getId());
            response.put("hoten", ql.getHoten());
            // Trả về luôn, quản lý không ảnh hưởng tới bảng Ca Làm[cite: 8]
            return response; 
        }

        // 2. KIỂM TRA XEM CÓ PHẢI LÀ NHÂN VIÊN KHÔNG[cite: 8]
        
        else if (nv != null && nv.getMkdangnhap().equals(password)) {
            
            // --- LOGIC ĐÓNG CA CŨ VÀ TÍNH TIỀN HỆ THỐNG (Giữ nguyên) ---[cite: 8]
            if (oldId != null) {
                List<CaLam> activeShifts = caLamRepo.findByNhanVienIdAndTgianKetThucIsNull(oldId);
                for (CaLam shift : activeShifts) {
                    shift.setTgianKetThuc(LocalTime.now());
                    caLamRepo.save(shift);

                    // Tính tổng tiền từ các giao dịch trong ca[cite: 8]
                    LocalDateTime start = LocalDateTime.of(shift.getNgaylam(), shift.getTgianBatDau());
                    LocalDateTime end = LocalDateTime.of(shift.getNgaylam(), shift.getTgianKetThuc());
                    List<ThanhToan> payments = thanhToanRepo.findByNhanVienAndTime(oldId, start, end);

                    int expectedCash = 0;
                    int expectedBank = 0;
                    for (ThanhToan t : payments) {
                        if ("Tiền mặt".equalsIgnoreCase(t.getPhuongthuc())) expectedCash += t.getSotien();
                        else expectedBank += t.getSotien();
                    }

                    // Tạo Log nháp chờ người dùng đếm tiền[cite: 8]
                    KetCaLog log = new KetCaLog();
                    log.setNhanVien(nhanVienRepo.findById(oldId).orElse(null));
                    log.setThoigian(LocalDateTime.now());
                    log.setTiencannop(expectedCash + expectedBank);
                    log.setTiennganhang(expectedBank);
                    log.setTienmat(0); 
                    log.setTongtien(expectedBank); 
                    ketCaLogRepo.save(log);
                }
            }

            // --- LOGIC MỞ CA MỚI CHO NHÂN VIÊN VỪA ĐĂNG NHẬP ---[cite: 8]
            // (Chỉ thực hiện nếu cờ createShift là true)
            if (createShift) {
                CaLam newShift = new CaLam();
                newShift.setNhanVien(nv);
                newShift.setNgaylam(LocalDate.now());
                newShift.setTgianBatDau(LocalTime.now());
                caLamRepo.save(newShift);
            }

            // --- TRẢ VỀ THÔNG TIN NHÂN VIÊN ---[cite: 8]
            Map<String, Object> response = new HashMap<>();
            response.put("role", "NhanVien");
            response.put("id", nv.getId());
            response.put("hoten", nv.getHoten());
            return response;
        }
        
        // Trả về null nếu ID không tồn tại ở cả 2 bảng hoặc sai mật khẩu[cite: 8]
        return null;
    }
}