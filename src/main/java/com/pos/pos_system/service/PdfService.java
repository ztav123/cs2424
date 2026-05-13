package com.pos.pos_system.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pos.pos_system.entity.CaLam;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
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
        document.add(new Paragraph("Nhan vien: " + shift.getNhanVien().getHoten()));
        document.add(new Paragraph("Bat dau: " + shift.getBatdau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        document.add(new Paragraph("Ket thuc: " + (shift.getKetthuc() != null ? shift.getKetthuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A")));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell("Chi so"); table.addCell("So tien (VND)");
        table.addCell("Doanh thu he thong"); table.addCell(String.format("%,.0f", shift.getTongDoanhthu()));
        table.addCell("Thuc te kiem ke"); table.addCell(String.format("%,.0f", shift.getTongtienThucte()));
        document.add(table);

        document.close();
    }
}