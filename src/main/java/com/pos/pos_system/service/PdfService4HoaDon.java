package com.pos.pos_system.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pos.pos_system.entity.CTHD;
import com.pos.pos_system.entity.HoaDon;
import com.pos.pos_system.entity.SanPham;
import com.pos.pos_system.repository.CTHDRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService4HoaDon {
    @Autowired
    private CTHDRepository cthdRepository;

    public void exportReceipt(HttpServletResponse response, HoaDon hd) throws IOException {
        Rectangle pageSize = new Rectangle(226, 800); 
        Document document = new Document(pageSize, 10, 10, 15, 15); 
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        

        // 1. CHÈN LOGO TRÊN CÙNG
        try {
            Image logo = Image.getInstance("src/main/resources/static/img/logo.png");
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(130, 60); 
            document.add(logo);
        } catch (Exception e) {
            // Bỏ qua nếu không có logo
        }

        // =========================================================
        // 2. KHỞI TẠO FONT TIẾNG VIỆT UNICODE (ARIAL)
        // =========================================================
        BaseFont unicodeBaseFont;
        try {
            // Trỏ tới file font arial.ttf bạn vừa copy vào
            String fontPath = "src/main/resources/static/fonts/arial.ttf";
            // IDENTITY_H là bắt buộc để gõ được tiếng Việt
            unicodeBaseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            // Fallback phòng hờ nếu bạn quên copy file font
            unicodeBaseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            System.out.println("CẢNH BÁO: Không tìm thấy file arial.ttf, PDF sẽ bị lỗi font tiếng Việt.");
        }

        Font boldFont = new Font(unicodeBaseFont, 12, Font.BOLD);
        Font normalFont = new Font(unicodeBaseFont, 9, Font.NORMAL);
        Font smallFont = new Font(unicodeBaseFont, 8, Font.NORMAL);
        Font boldSmallFont = new Font(unicodeBaseFont, 8, Font.BOLD);

        // 3. TIÊU ĐỀ HÓA ĐƠN
        Paragraph title = new Paragraph("HÓA ĐƠN BÁN LẺ", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // 4. THÔNG TIN CHUNG
        document.add(new Paragraph("Số HĐ: " + hd.getId(), normalFont));
        String dateStr = hd.getNgaylap() != null ? hd.getNgaylap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        document.add(new Paragraph("Ngày: " + dateStr, normalFont));
        
        String nvName = hd.getNhanVien() != null ? hd.getNhanVien().getHoten() : "N/A";
        document.add(new Paragraph("Thu ngân: " + nvName, normalFont));
        
        String khName = hd.getKhachHang() != null ? hd.getKhachHang().getHoten() : "Khách lẻ";
        document.add(new Paragraph("Khách hàng: " + khName, normalFont));

        document.add(new Paragraph("Phương thức: " + hd.getPhuongthucTt(), normalFont));

        Paragraph dashedLine = new Paragraph("---------------------------------------------------------", normalFont);
        dashedLine.setAlignment(Element.ALIGN_CENTER);
        document.add(dashedLine);

        // 5. BẢNG CHI TIẾT SẢN PHẨM
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        try { table.setWidths(new float[]{4f, 1.2f, 2.5f, 2.8f}); } catch (Exception ignored) {}
        
        PdfPCell c1 = new PdfPCell(new Phrase("Tên hàng", boldSmallFont)); c1.setBorder(Rectangle.BOTTOM); table.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("SL", boldSmallFont)); c2.setBorder(Rectangle.BOTTOM); c2.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase("ĐG", boldSmallFont)); c3.setBorder(Rectangle.BOTTOM); c3.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase("T.Tiền", boldSmallFont)); c4.setBorder(Rectangle.BOTTOM); c4.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(c4);

        Double tongTien = 0.0;
        
        List<CTHD> chiTietList = cthdRepository.findByHoadon_Id(hd.getId());

        if (chiTietList != null && !chiTietList.isEmpty()) {
            for (CTHD ct : chiTietList) {
                // Lấy tên sản phẩm thông qua sanphamid
                String tenSp = "SP Không rõ";
                if (ct.getSanPham() != null) {
                    SanPham sp = ct.getSanPham();
                    if (sp != null) {
                        tenSp = sp.getTen();
                    }
                }
                
                PdfPCell cell1 = new PdfPCell(new Phrase(tenSp, smallFont)); cell1.setBorder(Rectangle.NO_BORDER); table.addCell(cell1);
                
                // Sử dụng getSoluong() thay vì getSoLuong() theo Entity CTHD
                PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), smallFont)); 
                cell2.setBorder(Rectangle.NO_BORDER); cell2.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(cell2);
                
                // Sử dụng getDongia() thay vì getDonGia() theo Entity CTHD
                PdfPCell cell3 = new PdfPCell(new Phrase(String.format("%,.0f", (double) ct.getDonGia()), smallFont)); 
                cell3.setBorder(Rectangle.NO_BORDER); cell3.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(cell3);
                
                Double lineTotal = (double) (ct.getSoLuong() * ct.getDonGia());
                tongTien += lineTotal;
                
                PdfPCell cell4 = new PdfPCell(new Phrase(String.format("%,.0f", lineTotal), smallFont)); 
                cell4.setBorder(Rectangle.NO_BORDER); cell4.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(cell4);
            }
        }
        document.add(table);
        document.add(dashedLine);

        // 6. TỔNG KẾT TIỀN
        PdfPTable sumTable = new PdfPTable(2);
        sumTable.setWidthPercentage(100);
        
        PdfPCell sum1 = new PdfPCell(new Phrase("TỔNG TIỀN:", boldFont)); sum1.setBorder(Rectangle.NO_BORDER); sumTable.addCell(sum1);
        PdfPCell sum2 = new PdfPCell(new Phrase(String.format("%,.0f", tongTien) + " đ", boldFont)); 
        sum2.setBorder(Rectangle.NO_BORDER); sum2.setHorizontalAlignment(Element.ALIGN_RIGHT); sumTable.addCell(sum2);
        
        document.add(sumTable);
        
        
        document.add(new Paragraph(" "));

        // 7. CHÂN HÓA ĐƠN
        Paragraph footer1 = new Paragraph("Cảm ơn quý khách đã mua hàng", smallFont);
        footer1.setAlignment(Element.ALIGN_CENTER);
        document.add(footer1);
        
        Paragraph footer2 = new Paragraph("Easy come, easy go!", new Font(unicodeBaseFont, 7, Font.ITALIC));
        footer2.setAlignment(Element.ALIGN_CENTER);
        document.add(footer2);
        
        Paragraph footer3 = new Paragraph("Miễn đổi trả sau khi mua!", new Font(unicodeBaseFont, 7, Font.ITALIC));
        footer3.setAlignment(Element.ALIGN_CENTER);
        document.add(footer3);

        document.close();
    }
}