package com.pos.pos_system.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pos.pos_system.entity.CaLam;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService4KetCa {
    public void exportShiftReport(HttpServletResponse response, CaLam shift) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("BAO CAO KET THUC CA LAM", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Ma ca: " + shift.getId()));
        
        // Chống Null cho thông tin nhân viên và thời gian
        String tenNhanVien = shift.getNhanVien() != null ? shift.getNhanVien().getHoten() : "N/A";
        String thoiGianBatDau = shift.getBatdau() != null ? shift.getBatdau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        String thoiGianKetThuc = shift.getKetthuc() != null ? shift.getKetthuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        
        document.add(new Paragraph("Nhan vien: " + tenNhanVien));
        document.add(new Paragraph("Bat dau: " + thoiGianBatDau));
        document.add(new Paragraph("Ket thuc: " + thoiGianKetThuc));
        document.add(new Paragraph("\n"));

        // ========================================================
        // XỬ LÝ CHỐNG LỖI NULL POINTER CHO CÁC PHÉP TÍNH TIỀN TỆ
        // ========================================================
        Double doanhThu = shift.getTongDoanhthu() != null ? shift.getTongDoanhthu() : 0.0;
        Double nganHang = shift.getTienNganhang() != null ? shift.getTienNganhang() : 0.0;
        Double tienmatBandau = shift.getTienmatBandau() != null ? shift.getTienmatBandau() : 0.0;
        Double tienmatKetca = shift.getTienmatKetca() != null ? shift.getTienmatKetca() : 0.0;
        
        // Tính toán độ lệch an toàn
        Double canNop = doanhThu - nganHang + tienmatBandau;
        Double chenhLech = tienmatKetca - canNop;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell("Chi so"); table.addCell("So tien (VND)");
        
        table.addCell("Doanh thu he thong"); table.addCell(String.format("%,.0f", doanhThu));
        table.addCell("Tien ngan hang"); table.addCell(String.format("%,.0f", nganHang));
        table.addCell("Tien mat moi ca truoc"); table.addCell(String.format("%,.0f", tienmatBandau));
        table.addCell("Tien mat can nop"); table.addCell(String.format("%,.0f", canNop));
        table.addCell("Thuc te kiem ke"); table.addCell(String.format("%,.0f", tienmatKetca));
        table.addCell("Chenh lech"); table.addCell(String.format("%,.0f", chenhLech));

        document.add(table);
        document.close();
    }
}