package com.pos.pos_system;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PosSystemApplication extends Application {

    private ConfigurableApplicationContext springContext;
    
    private static final String DEFAULT_PORT = "8080";
    private static final String BACKUP_PORT = "8082";
    
    private static String selectedPort = DEFAULT_PORT;
    private static String baseUrl = "http://localhost:" + DEFAULT_PORT;

    // CHÌA KHÓA VÀNG: Tạo duy nhất một thực thể JavaBridge dùng chung (Singleton) cho mọi cửa sổ
    private static final JavaBridge SHARED_BRIDGE = new JavaBridge();

    @Override
    public void init() throws Exception {
        if (isPortAvailable(Integer.parseInt(DEFAULT_PORT))) {
            selectedPort = DEFAULT_PORT;
            System.out.println("✅ Cổng 8080 trống! Hệ thống sẽ chạy trên cổng mặc định: 8080");
        } else {
            selectedPort = BACKUP_PORT;
            System.out.println("⚠️ Cổng 8080 đã bị chiếm dụng! Tự động chuyển giao diện sang cổng dự phòng: 8082");
        }
        baseUrl = "http://localhost:" + selectedPort;
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        openNewWindow(primaryStage, "Hệ Thống POS - Màn Hình Kiểm Thử Đồ Án", true);

        Thread springThread = new Thread(() -> {
            try {
                List<String> argsList = new ArrayList<>(getParameters().getRaw());
                argsList.add("--server.port=" + selectedPort);
                String[] args = argsList.toArray(new String[0]);

                springContext = SpringApplication.run(PosSystemApplication.class, args);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi Khởi Động");
                    alert.setContentText("Không thể khởi chạy ứng dụng kể cả trên cổng dự phòng!");
                    alert.showAndWait();
                });
            }
        });
        springThread.setDaemon(true);
        springThread.start();
    }

    /**
     * Hàm xử lý đa cửa sổ JavaFX - Đã sửa lỗi mất kết nối window.app khi mở nhiều cửa sổ
     */
    public void openNewWindow(Stage stage, String title, boolean isMainWindow) {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
                if (newExc != null) System.out.println("❌ LỖI WEBVIEW: " + newExc.getMessage());
            });
            webEngine.setOnError(event -> System.out.println("⚠️ LỖI JS: " + event.getMessage()));

            webEngine.setOnAlert(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo POS");
                alert.setHeaderText(null);
                alert.setContentText(event.getData());
                alert.showAndWait();
            });

            // ĐÃ SỬA: Ép tất cả các WebEngine độc lập cùng trỏ chung vào thực thể SHARED_BRIDGE duy nhất
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("app", SHARED_BRIDGE); 
                    System.out.println("✅ Đã đồng bộ thành công cấu trúc window.app dùng chung cho cửa sổ này!");
                }
            });

            if (isMainWindow && springContext == null) {
                webEngine.loadContent(
                    "<html><body style='background:#2c3338; color:white; font-family:Arial; text-align:center; padding-top:200px;'>" +
                    "<h2>HỆ THỐNG MÁY POS TIỆN LỢI</h2>" +
                    "<p style='color:#ccc;'>Hệ thống đang được kích hoạt tự động trên cổng: <b>" + selectedPort + "</b></p>" +
                    "<div style='margin:20px auto; width:50px; height:50px; border:5px solid #f3f3f3; border-top:5px solid #0b5aa6; border-radius:50%; animation:spin 1s linear infinite;'></div>" +
                    "<style>@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }</style>" +
                    "</body></html>"
                );

                new Thread(() -> {
                    while (springContext == null || !springContext.isRunning()) {
                        try { Thread.sleep(400); } catch (InterruptedException e) {}
                    }
                    Platform.runLater(() -> webEngine.load(baseUrl + "/login.html"));
                }).start();
            } else {
                webEngine.load(baseUrl + "/login.html");
            }

            Scene scene = new Scene(webView, 1200, 800);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        });
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
        System.exit(0);
    }

    // ĐÃ SỬA: Giữ nguyên lớp cấu trúc JavaBridge và chuyển lên cấp static nếu cần thiết, đảm bảo thread-safe
    public static class JavaBridge {
        public void downloadPdf(String endpoint, String token, String filename) {
            new Thread(() -> {
                HttpURLConnection httpConn = null;
                try {
                    URL url = new URL(endpoint);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    
                    if (token != null && !token.isEmpty() && !token.equals("null")) {
                        httpConn.setRequestProperty("Authorization", "Bearer " + token);
                    }
                    
                    int responseCode = httpConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        File targetDir = new File("C:/POS_pdf");
                        if (!targetDir.exists()) {
                            targetDir.mkdirs();
                        }
                        File saveFile = new File(targetDir, filename);
                        
                        try (InputStream inputStream = httpConn.getInputStream();
                             FileOutputStream outputStream = new FileOutputStream(saveFile)) {
                            
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.flush();
                        }
                        
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
                    if (httpConn != null) {
                        httpConn.disconnect();
                    }
                }
            }).start();
        }
    }
}