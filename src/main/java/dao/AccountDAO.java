package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    /**
     * checkLogin(user : Account) : boolean
     * Kiểm tra đăng nhập. Nếu thành công, cập nhật idAcc và status vào object user.
     */
    public boolean checkLogin(Account user) {
        try {
            String sql = "SELECT * FROM tblAccount WHERE username = ? AND password = ? AND status = 'active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setIdAcc(rs.getString("idAcc"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreateDate(rs.getTimestamp("createDate"));
                user.setUpdateDate(rs.getTimestamp("updateDate"));
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
