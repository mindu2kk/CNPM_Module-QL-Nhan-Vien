package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    /**
     * checkLogin(user : Account) : boolean
     * Kiểm tra đăng nhập. Nếu thành công, cập nhật idAcc, role, status vào object user.
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

    /** Lấy tất cả tài khoản */
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblAccount ORDER BY createDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /** Tìm kiếm tài khoản theo username hoặc idAcc */
    public ArrayList<Account> searchAccounts(String key) {
        ArrayList<Account> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblAccount WHERE username LIKE ? OR idAcc LIKE ? ORDER BY username";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /** Thêm tài khoản mới */
    public boolean addAccount(Account a) {
        try {
            String sql = "INSERT INTO tblAccount(idAcc, username, password, role, status) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, a.getIdAcc());
            ps.setString(2, a.getUsername());
            ps.setString(3, a.getPassword());
            ps.setString(4, a.getRole());
            ps.setString(5, a.getStatus() != null ? a.getStatus() : "active");
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /** Cập nhật tài khoản (username, password, role, status) */
    public boolean updateAccount(Account a) {
        try {
            String sql;
            PreparedStatement ps;
            if (a.getPassword() != null && !a.getPassword().isEmpty()) {
                sql = "UPDATE tblAccount SET username=?, password=?, role=?, status=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getPassword());
                ps.setString(3, a.getRole());
                ps.setString(4, a.getStatus());
                ps.setString(5, a.getIdAcc());
            } else {
                sql = "UPDATE tblAccount SET username=?, role=?, status=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getRole());
                ps.setString(3, a.getStatus());
                ps.setString(4, a.getIdAcc());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /** Xóa tài khoản */
    public boolean deleteAccount(String idAcc) {
        try {
            String sql = "DELETE FROM tblAccount WHERE idAcc=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idAcc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /** Map ResultSet → Account */
    private Account map(ResultSet rs) throws Exception {
        Account a = new Account();
        a.setIdAcc(rs.getString("idAcc"));
        a.setUsername(rs.getString("username"));
        a.setPassword(rs.getString("password"));
        a.setRole(rs.getString("role"));
        a.setStatus(rs.getString("status"));
        a.setCreateDate(rs.getTimestamp("createDate"));
        a.setUpdateDate(rs.getTimestamp("updateDate"));
        return a;
    }
}
