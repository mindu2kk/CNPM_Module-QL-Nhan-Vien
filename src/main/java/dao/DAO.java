package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

    public static Connection con;

    public DAO() {
        if (con == null) {
            // Đổi port/password cho khớp với MySQL của bạn
            // XAMPP mặc định: port 3306, password rỗng ""
            String dbUrl   = "jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
            String dbUser  = "root";
            String dbPass  = "123456";

            try {
                // Driver mới cho mysql-connector 8.x
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
