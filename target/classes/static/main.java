package DOANJAVA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class main extends Application {
    //tao 1 cai ket noi database
    class connectDatabase {
        public Connection getConnection(){
            try{
                 String url = "jdbc:mysql://127.0.0.1:3306/conveniencestore";
                String user = "root"; 
                String password = "123456";

                System.out.println("connected successful");
                return DriverManager.getConnection(url, user, password); 
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                return null; 
            }
        }
    }

     WebEngine webEngine; 
     Stage stage; 


     
     public class Bridge{
        public String getTime() {
            return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        //lay thong tin 
         int getid = 0; 
         String name = null; 

        public String loginchecked(String id , String password){
             int checkid = 0; 
             try{
                 checkid = Integer.parseInt(id); 
             }
             catch( NumberFormatException e){
                System.out.println(e.getMessage());
                return "id or password can be incorrect"; 
             }
             connectDatabase db = new connectDatabase(); 
             Connection cnt = null; 
             PreparedStatement pstmt = null; 
             ResultSet rs = null; 

             String passcheck  = null;

             String getiddb  = "select * from NHANVIEN where id = ?"; 

             //getthongtn
             
             try {
                cnt = db.getConnection(); 
                if(cnt == null) {
                    System.out.println("can not connect to database");
                    return "database error"; 
                }

                pstmt = cnt.prepareStatement(getiddb); 

                pstmt.setInt(1, checkid); 

                rs = pstmt.executeQuery(); 

                if(rs.next()){
                    name = rs.getString("hoten"); 
                    passcheck = rs.getString("mkdangnhap"); 
                    getid = rs.getInt("id"); 
                }
                rs.close();
                cnt.close();
                pstmt.close();
             }
             catch(SQLException e){
                System.out.println(e.getMessage());
             }
             if(passcheck != null && passcheck.equals(password)){
                System.out.println("User " + checkid + "logined successfull!");
                Platform.runLater(()->loadHomepage());
                return "Login successfull"; 
             }
             else{
                return "Password or ID can be incorrect"; 
             }

        }
        public String getemployeename(){
                return name; 
        }  
        public int getemployeeid(){
            return getid; 
        }

     }
    
     @Override 
     public void start(Stage stage){
        this.stage = stage; 
        WebView webView = new WebView(); 
        webEngine = webView.getEngine(); 

        Bridge bridge = new Bridge(); 

        webEngine.getLoadWorker().stateProperty().addListener((obs, obstate, newstate) ->{
            if(newstate == Worker.State.SUCCEEDED){
                JSObject window = (JSObject) webEngine.executeScript("window"); 
                window.setMember("app", bridge);
            }
            webEngine.executeScript("if (typeof getinforid ==='function') {getinforid(); }"); 

             webEngine.executeScript("if (typeof getinforname ==='function') {getinforname(); }"); 


        });

        loadloginpage1(); 

        stage.setTitle("Project");
        stage.setScene( new Scene(webView , 1440, 1024));
        stage.show();
     }

     public void loadloginpage1(){
         var resource = getClass().getResource("login.html"); 
         webEngine.load(resource.toExternalForm()); 
     }

     public void loadHomepage(){
        var resource = getClass().getResource("orderPage.html"); 
        webEngine.load(resource.toExternalForm());
     }

     public static void main (String[] args){
        launch(args);
     }
}
