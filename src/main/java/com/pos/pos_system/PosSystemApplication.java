package com.pos.pos_system;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PosSystemApplication extends Application {

    // Biến này dùng để lưu trữ và quản lý máy chủ Spring Boot
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Thay vì chạy SpringApplication.run ngay lập tức, ta khởi chạy JavaFX.
        // JavaFX sẽ tự động gọi lần lượt 3 hàm: init() -> start() -> stop()
        Application.launch(PosSystemApplication.class, args);
    }

    /**
     * HÀM INIT: Chạy trước khi cửa sổ giao diện bật lên.
     * Nhiệm vụ: Khởi động máy chủ Spring Boot (khởi tạo H2 Database, tạo các API REST).
     */
    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(PosSystemApplication.class);
    }

    /**
     * HÀM START: Dựng cửa sổ giao diện cho người dùng.
     */
    @Override
    public void start(Stage primaryStage) {
    // 1. Tạo trình duyệt nhúng (WebView)
    WebView webView = new WebView();
    
    // 2. Tải trang web nội bộ
    webView.getEngine().load("http://localhost:8080/login.html");

    // 3. Tạo Scene
    // Lưu ý: Kích thước ở đây sẽ là kích thước "mặc định" khi người dùng nhấn nút "Restore Down"
    Scene scene = new Scene(webView,    1350,   836);

    // 4. Thiết lập cho cửa sổ chính (Stage)
    primaryStage.setTitle("Hệ Thống POS - Cửa Hàng Tiện Lợi");
    primaryStage.setScene(scene);
    
    // --- CÁC THAY ĐỔI CHÍNH Ở ĐÂY ---
    
    // Cho phép người dùng kéo giãn, thu nhỏ cửa sổ
    primaryStage.setResizable(true); 

    // Thiết lập cửa sổ ở trạng thái phóng to toàn màn hình (Maximized) ngay khi mở
    // Lưu ý: Trạng thái này vẫn giữ thanh tiêu đề và các nút điều khiển (Khác với FullScreen)
    primaryStage.setMaximized(true); 
    
    // (Tùy chọn) Đặt kích thước tối thiểu để tránh vỡ layout nếu người dùng thu quá nhỏ
    primaryStage.setMinWidth(1024);
    primaryStage.setMinHeight(768);

    // Hiển thị cửa sổ
    primaryStage.show();
}

    /**
     * HÀM STOP: Chạy khi người dùng bấm dấu X màu đỏ để tắt cửa sổ phần mềm.
     * Nhiệm vụ: Tắt máy chủ Spring Boot một cách an toàn để giải phóng RAM.
     */
    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
    }
}