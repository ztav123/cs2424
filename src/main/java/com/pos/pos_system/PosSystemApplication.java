package com.pos.pos_system;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PosSystemApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private WebEngine webEngine;

    @Override
    public void init() throws Exception {
        // Để hàm init() trống, không block luồng JavaFX-Launcher ở đây nữa!
    }

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
            if (newExc != null) {
                System.out.println("❌ LỖI JAVA-FX WEBVIEW KHI TẢI TRANG: " + newExc.getMessage());
            }
        });
        webEngine.setOnError(event -> {
            System.out.println("⚠️ LỖI JAVASCRIPT TRÊN GIAO DIỆN: " + event.getMessage());
        });

        // 1. CẤU HÌNH CÁC TÍNH NĂNG NHÚNG (ALERT, JAVABRIDGE)
        webEngine.setOnAlert(event -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo hệ thống POS");
                alert.setHeaderText(null);
                alert.setContentText(event.getData());
                alert.showAndWait();
            });
        });

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", new JavaBridge());
            }
        });

        // 2. HIỂN THỊ MÀN HÌNH CHỜ TRONG KHI SPRING BOOT ĐANG KHỞI ĐỘNG
        // Cách này giúp JavaFX lên hình ngay lập tức, không bao giờ bị lỗi trắng màn hình
        webEngine.loadContent(
            "<html><body style='background:#2c3338; color:white; font-family:Arial; text-align:center; padding-top:200px;'>" +
            "<h2>HỆ THỐNG MÁY POS CS24H</h2>" +
            "<p style='color:#ccc;'>Đang kết nối cơ sở dữ liệu Oracle và khởi chạy dịch vụ...</p>" +
            "<div style='margin:20px auto; width:50px; height:50px; border:5px solid #f3f3f3; border-top:5px solid #0b5aa6; border-radius:50%; animation:spin 1s linear infinite;'></div>" +
            "<style>@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }</style>" +
            "</body></html>"
        );

        Scene scene = new Scene(webView, 1350, 836);
        primaryStage.setTitle("Hệ Thống POS - Cửa Hàng Tiện Lợi");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); 
        primaryStage.setMaximized(true); 
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.show();

        // 3. ĐẨY SPRING BOOT CHẠY TRÊN THREAD RIÊNG (TỐI ƯU TỐC ĐỘ, CHỐNG DÍNH TRẮNG MÀN)
        Thread springThread = new Thread(() -> {
            try {
                // Nhận tham số args từ hàm khởi chạy toàn cục
                String[] args = getParameters().getRaw().toArray(new String[0]);
                springContext = SpringApplication.run(PosSystemApplication.class, args);

                // Sau khi Spring Boot báo "Started" thành công, ép JavaFX đổi trang sang Login
                Platform.runLater(() -> {
                    webEngine.load("http://localhost:8080/login.html");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    webEngine.loadContent("<h2 style='color:red; text-align:center; padding-top:100px;'>Lỗi khởi động hệ thống Backend!</h2>");
                });
            }
        });
        springThread.setDaemon(true); // Đảm bảo thread này tự tắt khi tắt app
        springThread.start();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }
    public class JavaBridge {
        public void downloadPdf(String endpoint, String token, String filename) {
            new Thread(() -> {
                HttpURLConnection httpConn = null;
                try {
                    URL url = new URL("http://localhost:8080" + endpoint);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    
                    if (token != null && !token.isEmpty() && !token.equals("null")) {
                        httpConn.setRequestProperty("Authorization", "Bearer " + token);
                    }
                    
                    int responseCode = httpConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        
                        // YÊU CẦU: Mặc định lưu toàn bộ vào thư mục C:/POS_pdf độc lập
                        File targetDir = new File("C:/POS_pdf");
                        if (!targetDir.exists()) {
                            targetDir.mkdirs(); // Tự động tạo thư mục nếu chưa có
                        }
                        File saveFile = new File(targetDir, filename);
                        
                        // Sử dụng try-with-resources để tự động giải phóng và đóng file ngay khi ghi xong
                        try (InputStream inputStream = httpConn.getInputStream();
                             FileOutputStream outputStream = new FileOutputStream(saveFile)) {
                            
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.flush(); // Ép xả hết dữ liệu nhị phân xuống đĩa cứng
                        }
                        
                        // Thông báo và mở file trên luồng đồ họa JavaFX
                        Platform.runLater(() -> {
                            try {
                                java.awt.Desktop.getDesktop().open(saveFile);
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Hệ Thống Máy POS");
                                alert.setHeaderText("✅ ĐÃ XUẤT FILE THÀNH CÔNG!");
                                alert.setContentText("File đã được lưu an toàn tại: " + saveFile.getAbsolutePath());
                                alert.showAndWait();
                            }
                        });
                    } else {
                        final int finalCode = responseCode;
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi Tải File");
                            alert.setContentText("Máy chủ trả về mã lỗi bảo mật (HTTP Code): " + finalCode);
                            alert.showAndWait();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi Kết Nối");
                        alert.setContentText("Không thể kết nối tới máy chủ Backend: " + e.getMessage());
                        alert.showAndWait();
                    });
                } finally {
                    // ĐÓNG CƯỠNG BỨC KẾT NỐI MẠNG để giải phóng tài nguyên cho lần bấm tiếp theo
                    if (httpConn != null) {
                        httpConn.disconnect();
                    }
                }
            }).start();
        }
    }
}