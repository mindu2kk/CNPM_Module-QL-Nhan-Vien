package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Staff;

public class StaffDAO extends DAO {

    public StaffDAO() { super(); }

    // ----------------------------------------------------------------
    // Lấy tất cả nhân viên
    // ----------------------------------------------------------------
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblStaff ORDER BY fullname";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Staff(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("role"),
                    rs.getString("tel"),
                    rs.getString("email")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Tìm kiếm nhân viên theo tên hoặc id
    // ----------------------------------------------------------------
    public ArrayList<Staff> searchStaff(String key) {
        ArrayList<Staff> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblStaff WHERE fullname LIKE ? OR CAST(id AS CHAR) LIKE ? ORDER BY fullname";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Staff(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("role"),
                    rs.getString("tel"),
                    rs.getString("email")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ----------------------------------------------------------------
    // Thêm nhân viên mới – trả về id được sinh ra, -1 nếu lỗi
    // ----------------------------------------------------------------
    public int addStaff(Staff s) {
        try {
            String sql = "INSERT INTO tblStaff(fullname, role, tel, email) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getFullname());
            ps.setString(2, s.getRole());
            ps.setString(3, s.getTel());
            ps.setString(4, s.getEmail());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // ----------------------------------------------------------------
    // Cập nhật thông tin nhân viên
    // ----------------------------------------------------------------
    public boolean updateStaff(Staff s) {
        try {
            String sql = "UPDATE tblStaff SET fullname=?, role=?, tel=?, email=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, s.getFullname());
            ps.setString(2, s.getRole());
            ps.setString(3, s.getTel());
            ps.setString(4, s.getEmail());
            ps.setInt(5, s.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ----------------------------------------------------------------
    // Xóa nhân viên theo id
    // ----------------------------------------------------------------
    public boolean deleteStaff(int id) {
        try {
            String sql = "DELETE FROM tblStaff WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
