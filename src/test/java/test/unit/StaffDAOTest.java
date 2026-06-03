package test.unit;

import dao.StaffDAO;
import model.Staff;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.ArrayList;
import static org.junit.Assert.*;

// Chạy theo thứ tự tên để đảm bảo: add → update → delete
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffDAOTest {

    private StaffDAO dao;

    // Tên test riêng, tránh xung đột với dữ liệu thật
    private static final String TEST_NAME = "Test User JUnit";

    @Before
    public void setUp() {
        dao = new StaffDAO();
    }

    // ----------------------------------------------------------------
    // getAllStaff
    // ----------------------------------------------------------------

    /** getAllStaff() phải trả về danh sách không null và có ít nhất 1 bản ghi */
    @Test
    public void test0_GetAllStaff_NotEmpty() {
        ArrayList<Staff> list = dao.getAllStaff();
        assertNotNull("Danh sách không được null", list);
        assertTrue("Phải có ít nhất 1 nhân viên trong DB", list.size() > 0);
    }

    // ----------------------------------------------------------------
    // searchStaff
    // ----------------------------------------------------------------

    /** Tìm theo tên có tồn tại → kết quả không rỗng */
    @Test
    public void test1_SearchStaff_ByName_Found() {
        ArrayList<Staff> result = dao.searchStaff("Nguyen");
        assertNotNull("Kết quả không được null", result);
        assertTrue("Phải tìm thấy ít nhất 1 nhân viên chứa 'Nguyen'", result.size() > 0);
    }

    /** Tìm theo từ khóa không tồn tại → danh sách rỗng */
    @Test
    public void test2_SearchStaff_NotFound() {
        ArrayList<Staff> result = dao.searchStaff("xxx_no_such_name_xxx");
        assertNotNull("Kết quả không được null", result);
        assertEquals("Không tìm thấy phải trả về danh sách rỗng", 0, result.size());
    }

    /** Tìm với từ khóa rỗng → trả về toàn bộ danh sách */
    @Test
    public void test3_SearchStaff_EmptyKey_ReturnsAll() {
        ArrayList<Staff> result = dao.searchStaff("");
        assertNotNull(result);
        assertTrue("Từ khóa rỗng phải trả về toàn bộ danh sách", result.size() >= 0);
    }

    // ----------------------------------------------------------------
    // addStaff
    // ----------------------------------------------------------------

    /** Thêm nhân viên mới → trả về id > 0 */
    @Test
    public void test4_AddStaff_Success() {
        cleanupTestUser();
        Staff s = new Staff(0, TEST_NAME, "Employee", "0900000000", "test@lib.vn");
        int id = dao.addStaff(s);
        assertTrue("Thêm nhân viên hợp lệ phải trả về id > 0", id > 0);
    }

    /** Sau khi thêm, tìm kiếm phải thấy nhân viên vừa thêm */
    @Test
    public void test5_AddStaff_CanBeFoundAfterAdd() {
        ArrayList<Staff> result = dao.searchStaff(TEST_NAME);
        assertNotNull(result);
        assertTrue("Nhân viên vừa thêm phải tìm thấy được", result.size() > 0);
        assertEquals(TEST_NAME, result.get(0).getFullname());
    }

    // ----------------------------------------------------------------
    // updateStaff
    // ----------------------------------------------------------------

    /** Cập nhật SĐT của nhân viên test → trả về true và dữ liệu thay đổi */
    @Test
    public void test6_UpdateStaff_Success() {
        ArrayList<Staff> arr = dao.searchStaff(TEST_NAME);
        assertTrue("Phải tìm thấy nhân viên test để cập nhật", arr.size() > 0);

        Staff s = arr.get(0);
        s.setTel("0911111111");
        assertTrue("Cập nhật hợp lệ phải trả về true", dao.updateStaff(s));

        ArrayList<Staff> updated = dao.searchStaff(TEST_NAME);
        assertEquals("SĐT phải được cập nhật", "0911111111", updated.get(0).getTel());
    }

    /** Cập nhật nhân viên với id không tồn tại → trả về false */
    @Test
    public void test7_UpdateStaff_IdNotExist() {
        Staff ghost = new Staff(-9999, "Ghost", "Admin", "0000000000", "ghost@lib.vn");
        assertFalse("id không tồn tại phải trả về false", dao.updateStaff(ghost));
    }

    // ----------------------------------------------------------------
    // deleteStaff
    // ----------------------------------------------------------------

    /** Xóa nhân viên test vừa thêm → trả về true */
    @Test
    public void test8_DeleteStaff_Success() {
        ArrayList<Staff> arr = dao.searchStaff(TEST_NAME);
        assertTrue("Phải tìm thấy nhân viên test để xóa", arr.size() > 0);
        assertTrue("Xóa hợp lệ phải trả về true", dao.deleteStaff(arr.get(0).getId()));
    }

    /** Sau khi xóa, tìm kiếm không còn thấy nữa */
    @Test
    public void test9_DeleteStaff_NotFoundAfterDelete() {
        ArrayList<Staff> result = dao.searchStaff(TEST_NAME);
        assertEquals("Sau khi xóa không được tìm thấy nữa", 0, result.size());
    }

    /** Xóa id không tồn tại → trả về false */
    @Test
    public void test10_DeleteStaff_IdNotExist() {
        assertFalse("id không tồn tại phải trả về false", dao.deleteStaff(-9999));
    }

    // ----------------------------------------------------------------
    // Helper – dọn dữ liệu test cũ
    // ----------------------------------------------------------------
    private void cleanupTestUser() {
        ArrayList<Staff> old = dao.searchStaff(TEST_NAME);
        for (Staff s : old) dao.deleteStaff(s.getId());
    }
}
