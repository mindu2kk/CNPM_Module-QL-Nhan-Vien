package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

    public static Connection con;

    public DAO() {
        if (con == null) {
            String dbUrl  = "jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
            String dbUser = "root";
            String dbPass = "123456";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                System.out.println("Kết nối database thành công!");
            } catch (Exception e) {
                System.err.println("Lỗi kết nối database: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
