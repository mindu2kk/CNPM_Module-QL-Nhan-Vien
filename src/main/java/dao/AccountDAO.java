package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    // ----------------------------------------------------------------
    // checkLogin(user : Account) : boolean  ← diagram gốc
    // Nếu thành công: gán idAcc, idRole, roleName, status, permissions
    // ----------------------------------------------------------------
    public boolean checkLogin(Account user) {
        try {
            String sql =
                "SELECT a.idAcc, a.idRole, a.status, a.staffId, " +
                "       a.createDate, a.updateDate, r.roleName " +
                "FROM   tblAccount a " +
                "JOIN   tblRole    r ON a.idRole = r.idRole " +
                "WHERE  a.username = ? AND a.password = ? AND a.status = 'active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false;

            user.setIdAcc(rs.getInt("idAcc"));
            user.setIdRole(rs.getString("idRole"));
            user.setRoleName(rs.getString("roleName"));
            user.setStatus(rs.getString("status"));
            user.setStaffId(rs.getObject("staffId") != null ? rs.getInt("staffId") : null);
            user.setCreateDate(rs.getTimestamp("createDate"));
            user.setUpdateDate(rs.getTimestamp("updateDate"));
            user.setPermissions(loadPermissions(user.getIdRole()));
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Load permissions cho một role
    // ----------------------------------------------------------------
    public Set<String> loadPermissions(String idRole) {
        Set<String> set = new HashSet<>();
        try {
            String sql =
                "SELECT p.permissionName " +
                "FROM   tblRole_Permission rp " +
                "JOIN   Permission         p ON rp.idPermission = p.idPermission " +
                "WHERE  rp.idRole = ? AND rp.status = 'active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idRole);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) set.add(rs.getString("permissionName"));
        } catch (Exception e) { e.printStackTrace(); }
        return set;
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
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Tìm kiếm tài khoản theo username
    // ----------------------------------------------------------------
    public ArrayList<Account> searchAccounts(String key) {
        ArrayList<Account> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblAccount WHERE username LIKE ? ORDER BY username";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Thêm tài khoản mới
    // ----------------------------------------------------------------
    public boolean addAccount(Account a) {
        try {
            String sql = "INSERT INTO tblAccount(username, password, idRole, status) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getIdRole());
            ps.setString(4, a.getStatus() != null ? a.getStatus() : "active");
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Cập nhật tài khoản (username, idRole, status; password nếu không rỗng)
    // ----------------------------------------------------------------
    public boolean updateAccount(Account a) {
        try {
            PreparedStatement ps;
            if (a.getPassword() != null && !a.getPassword().isEmpty()) {
                String sql = "UPDATE tblAccount SET username=?, password=?, idRole=?, status=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getPassword());
                ps.setString(3, a.getIdRole());
                ps.setString(4, a.getStatus());
                ps.setInt(5, a.getIdAcc());
            } else {
                String sql = "UPDATE tblAccount SET username=?, idRole=?, status=?, updateDate=NOW() WHERE idAcc=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, a.getUsername());
                ps.setString(2, a.getIdRole());
                ps.setString(3, a.getStatus());
                ps.setInt(4, a.getIdAcc());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Xóa tài khoản theo idAcc
    // ----------------------------------------------------------------
    public boolean deleteAccount(int idAcc) {
        try {
            String sql = "DELETE FROM tblAccount WHERE idAcc=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAcc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Helper: map ResultSet → Account
    // ----------------------------------------------------------------
    private Account mapRow(ResultSet rs) throws Exception {
        Account a = new Account();
        a.setIdAcc(rs.getInt("idAcc"));
        a.setUsername(rs.getString("username"));
        a.setPassword(rs.getString("password"));
        a.setIdRole(rs.getString("idRole"));
        a.setStatus(rs.getString("status"));
        a.setStaffId(rs.getObject("staffId") != null ? rs.getInt("staffId") : null);
        a.setCreateDate(rs.getTimestamp("createDate"));
        a.setUpdateDate(rs.getTimestamp("updateDate"));
        return a;
    }
}
