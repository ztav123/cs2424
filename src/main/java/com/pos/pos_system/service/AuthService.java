package com.pos.pos_system.service;

import com.pos.pos_system.entity.CaLam;
import com.pos.pos_system.entity.KetCaLog;
import com.pos.pos_system.entity.NhanVien;
import com.pos.pos_system.entity.ThanhToan;
import com.pos.pos_system.repository.CaLamRepository;
import com.pos.pos_system.repository.KetCaLogRepository;
import com.pos.pos_system.repository.NhanVienRepository;
import com.pos.pos_system.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AuthService {
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private ThanhToanRepository thanhToanRepo;
    @Autowired private KetCaLogRepository ketCaLogRepo;

    @Transactional
    public NhanVien loginAndManageShift(Integer newId, String password, Integer oldId) {
        NhanVien nv = nhanVienRepo.findById(newId).orElse(null);
        
        if (nv != null && nv.getMkdangnhap().equals(password)) {
            
            // 1. ĐÓNG CA CŨ VÀ TÍNH TIỀN HỆ THỐNG
            if (oldId != null) {
                List<CaLam> activeShifts = caLamRepo.findByNhanVienIdAndTgianKetThucIsNull(oldId);
                for (CaLam shift : activeShifts) {
                    shift.setTgianKetThuc(LocalTime.now());
                    caLamRepo.save(shift);

                    // Tính tổng tiền từ các giao dịch trong ca
                    LocalDateTime start = LocalDateTime.of(shift.getNgaylam(), shift.getTgianBatDau());
                    LocalDateTime end = LocalDateTime.of(shift.getNgaylam(), shift.getTgianKetThuc());
                    List<ThanhToan> payments = thanhToanRepo.findByNhanVienAndTime(oldId, start, end);

                    int expectedCash = 0;
                    int expectedBank = 0;
                    for (ThanhToan t : payments) {
                        if ("Tiền mặt".equalsIgnoreCase(t.getPhuongthuc())) expectedCash += t.getSotien();
                        else expectedBank += t.getSotien();
                    }

                    // Tạo Log nháp chờ người dùng đếm tiền
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

            // 2. MỞ CA MỚI CHO NHÂN VIÊN VỪA ĐĂNG NHẬP
            CaLam newShift = new CaLam();
            newShift.setNhanVien(nv);
            newShift.setNgaylam(LocalDate.now());
            newShift.setTgianBatDau(LocalTime.now());
            caLamRepo.save(newShift);

            return nv;
        }
        return null;
    }
}