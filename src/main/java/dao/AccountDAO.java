package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import model.Account;

public class AccountDAO extends DAO {

    public AccountDAO() { super(); }

    /**
     * checkLogin(user : Account) : boolean
     *
     * Xác thực đăng nhập. Nếu thành công:
     *   – Gán idAcc, idRole, roleName, status, staffId, createDate, updateDate
     *   – Load toàn bộ permissions của role đó vào user.permissions
     */
    public boolean checkLogin(Account user) {
        try {
            // 1. Kiểm tra username / password / status
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

            // 2. Load permissions của role này
            user.setPermissions(loadPermissions(user.getIdRole()));
            return true;

        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /**
     * Load tất cả permissionName mà một role được gán (status = active).
     */
    public Set<String> loadPermissions(String idRole) {
        Set<String> set = new HashSet<>();
        try {
            String sql =
                "SELECT p.permissionName " +
                "FROM   tblRole_Permission rp " +
                "JOIN   Permission         p  ON rp.idPermission = p.idPermission " +
                "WHERE  rp.idRole = ? AND rp.status = 'active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idRole);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) set.add(rs.getString("permissionName"));
        } catch (Exception e) { e.printStackTrace(); }
        return set;
    }
}
