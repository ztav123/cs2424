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

@Service
public class AuthService {

    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private CaLamRepository caLamRepo;
    @Autowired private HoaDonRepository hoaDonRepo;

    @Transactional
    public Map<String, Object> loginAndManageShift(Integer newId, String password, Integer oldId, boolean createShift) {
        NhanVien nv = nhanVienRepo.findById(newId).orElse(null);
        if (nv == null || !nv.getMkdangnhap().equals(password)) return null; 

        if ("ADMIN".equalsIgnoreCase(nv.getVaitro())) {
            Map<String, Object> response = new HashMap<>();
            response.put("role", "ADMIN");
            response.put("id", nv.getId());
            response.put("hoten", nv.getHoten());
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
            caLamRepo.save(newShift);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("role", "NHANVIEN");
        response.put("id", nv.getId());
        response.put("hoten", nv.getHoten());
        return response;
    }
}