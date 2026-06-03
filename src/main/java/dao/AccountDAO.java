package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    /**
     * Kiểm tra đăng nhập.
     * Trả về true nếu username/password khớp và status = "active".
     * Nếu thành công, cập nhật idAcc và status vào object user.
     */
    public boolean checkLogin(Account user) {
        try {
            String sql = "SELECT * FROM tblAccount WHERE username = ? AND password = ? AND status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, "active");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setIdAcc(rs.getString("idAcc"));
                user.setStatus(rs.getString("status"));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
