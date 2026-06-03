package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    // ----------------------------------------------------------------
    // Đăng nhập – trả về Account đầy đủ (có role) nếu hợp lệ, null nếu sai
    // ----------------------------------------------------------------
    public Account checkLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM tblAccount WHERE username = ? AND password = ? AND status = 'active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account a = new Account();
                a.setIdAcc(rs.getString("idAcc"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setRole(rs.getString("role"));
                a.setStatus(rs.getString("status"));
                a.setStaffId(rs.getObject("staffId") != null ? rs.getInt("staffId") : null);
                a.setCreateDate(rs.getTimestamp("createDate"));
                a.setUpdateDate(rs.getTimestamp("updateDate"));
                return a;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ----------------------------------------------------------------
    // Lấy tất cả tài khoản (Admin dùng)
    // ----------------------------------------------------------------
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblAccount ORDER BY createDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setIdAcc(rs.getString("idAcc"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setRole(rs.getString("role"));
                a.setStatus(rs.getString("status"));
                a.setStaffId(rs.getObject("staffId") != null ? rs.getInt("staffId") : null);
                a.setCreateDate(rs.getTimestamp("createDate"));
                a.setUpdateDate(rs.getTimestamp("updateDate"));
                list.add(a);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Tìm kiếm tài khoản theo username
    // ----------------------------------------------------------------
    public ArrayList<Account> searchAccounts(String key) {
        ArrayList<Account> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblAccount WHERE username LIKE ? OR idAcc LIKE ? ORDER BY username";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setIdAcc(rs.getString("idAcc"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setRole(rs.getString("role"));
                a.setStatus(rs.getString("status"));
                a.setStaffId(rs.getObject("staffId") != null ? rs.getInt("staffId") : null);
                a.setCreateDate(rs.getTimestamp("createDate"));
                a.setUpdateDate(rs.getTimestamp("updateDate"));
                list.add(a);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Thêm tài khoản mới
    // ----------------------------------------------------------------
    public boolean addAccount(Account a) {
        try {
            String sql = "INSERT INTO tblAccount(idAcc, username, password, role, status, staffId) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, a.getIdAcc());
            ps.setString(2, a.getUsername());
            ps.setString(3, a.getPassword());
            ps.setString(4, a.getRole());
            ps.setString(5, a.getStatus() != null ? a.getStatus() : "active");
            if (a.getStaffId() != null) ps.setInt(6, a.getStaffId());
            else ps.setNull(6, java.sql.Types.INTEGER);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Cập nhật tài khoản (username, role, status, staffId)
    // Nếu password != null thì đổi luôn, null = giữ nguyên
    // ----------------------------------------------------------------
    public boolean updateAccount(Account a) {
        try {
            String sql;
            PreparedStatement ps;
            if (a.getPassword() != null && !a.getPassword().isEmpty()) {
                sql = "UPDATE tblAccount SET username=?, password=?, role=?, status=?, staffId=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getPassword());
                ps.setString(3, a.getRole());
                ps.setString(4, a.getStatus());
                if (a.getStaffId() != null) ps.setInt(5, a.getStaffId());
                else ps.setNull(5, java.sql.Types.INTEGER);
                ps.setString(6, a.getIdAcc());
            } else {
                sql = "UPDATE tblAccount SET username=?, role=?, status=?, staffId=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getRole());
                ps.setString(3, a.getStatus());
                if (a.getStaffId() != null) ps.setInt(4, a.getStaffId());
                else ps.setNull(4, java.sql.Types.INTEGER);
                ps.setString(5, a.getIdAcc());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Đổi mật khẩu
    // ----------------------------------------------------------------
    public boolean changePassword(String idAcc, String newPassword) {
        try {
            String sql = "UPDATE tblAccount SET password=?, updateDate=NOW() WHERE idAcc=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, idAcc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Xóa tài khoản
    // ----------------------------------------------------------------
    public boolean deleteAccount(String idAcc) {
        try {
            String sql = "DELETE FROM tblAccount WHERE idAcc = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idAcc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Sinh ID tự động (ACC001, ACC002, ...)
    // ----------------------------------------------------------------
    public String generateNextId() {
        try {
            String sql = "SELECT idAcc FROM tblAccount ORDER BY idAcc DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String last = rs.getString("idAcc"); // e.g. "ACC005"
                int num = Integer.parseInt(last.replaceAll("[^0-9]", "")) + 1;
                return String.format("ACC%03d", num);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "ACC001";
    }

    // ----------------------------------------------------------------
    // Kiểm tra username đã tồn tại chưa (bỏ qua idAcc hiện tại khi update)
    // ----------------------------------------------------------------
    public boolean isUsernameExists(String username, String excludeIdAcc) {
        try {
            String sql = "SELECT idAcc FROM tblAccount WHERE username = ? AND idAcc != ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, excludeIdAcc != null ? excludeIdAcc : "");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
