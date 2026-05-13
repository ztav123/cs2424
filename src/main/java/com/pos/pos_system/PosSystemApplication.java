package com.pos.pos_system;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PosSystemApplication extends Application {
    
    private ConfigurableApplicationContext springContext;

    private JavaAppBridge appBridge = new JavaAppBridge();

    public static void main(String[] args) {
        Application.launch(PosSystemApplication.class, args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(PosSystemApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Tạo trình duyệt nhúng
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        
        // --- 2. THIẾT LẬP CẦU NỐI TẢI PDF (BẮT BUỘC Ở ĐÂY) ---
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // Đăng ký đối tượng "app" vào JS mỗi khi tải xong bất kỳ trang nào
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", appBridge);
            }
        });

        // 3. Tải trang web nội bộ (bắt đầu từ login)
        engine.load("http://localhost:8080/login.html");

        // 4. Thiết lập UI cho cửa sổ
        Scene scene = new Scene(webView, 1350, 836);
        primaryStage.setTitle("Hệ Thống POS - Cửa Hàng Tiện Lợi");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); 
        primaryStage.setMaximized(true); 
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }

    // 5. Lớp nội bộ xử lý việc tải PDF từ HTML gọi sang
    public class JavaAppBridge {
        public void downloadPdf(String id) {
            // (Viết tiếng Việt không dấu để Terminal không bị lỗi font ??)
            System.out.println(">>> Da nhan lenh tai PDF cho Hoa don so: " + id);
            
            // Chạy lệnh mở web trên luồng giao diện của JavaFX
            Platform.runLater(() -> {
                try {
                    String url = "http://localhost:8080/api/ketca/export-pdf/" + id;
                    
                    // Sử dụng HostServices chuẩn của JavaFX thay cho java.awt.Desktop
                    getHostServices().showDocument(url);
                    
                    System.out.println(">>> Da gui lenh tai file cho trinh duyet thanh cong!");
                } catch (Exception e) {
                    System.err.println(">>> Loi mo trinh duyet: " + e.getMessage());
                }
            });
        }
    }
}