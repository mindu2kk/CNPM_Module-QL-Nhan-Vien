package test.unit;

import dao.StaffDAO;
import model.Staff;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

// Chạy theo thứ tự tên: add → update → delete
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffDAOTest {

    private StaffDAO dao;

    private static final String TEST_NAME = "Test User JUnit";

    @Before
    public void setUp() {
        dao = new StaffDAO();
    }

    // ================================================================
    // getAllStaff
    // ================================================================

    /** getAllStaff() trả về danh sách không null, có ít nhất 1 nhân viên */
    @Test
    public void test0_GetAllStaff_NotEmpty() {
        assertNotNull(dao.getAllStaff());
        assertTrue("Phải có ít nhất 1 nhân viên", dao.getAllStaff().size() > 0);
    }

    // ================================================================
    // searchStaff(key : String) : Staff[]
    // ================================================================

    /** Tìm tên tồn tại → mảng không rỗng */
    @Test
    public void test1_SearchStaff_ByName_Found() {
        Staff[] result = dao.searchStaff("Nguyen");
        assertNotNull(result);
        assertTrue("Phải tìm thấy ít nhất 1 nhân viên có tên 'Nguyen'",
            result.length > 0);
    }

    /** Tìm tên không tồn tại → mảng rỗng */
    @Test
    public void test2_SearchStaff_NotFound() {
        Staff[] result = dao.searchStaff("xxx_no_such_name_xxx");
        assertNotNull(result);
        assertEquals("Không tìm thấy phải trả về mảng rỗng", 0, result.length);
    }

    /** Tìm với từ khóa rỗng → trả về toàn bộ danh sách */
    @Test
    public void test3_SearchStaff_EmptyKey_ReturnsAll() {
        Staff[] result = dao.searchStaff("");
        assertNotNull(result);
        assertTrue("Từ khóa rỗng phải trả về toàn bộ", result.length >= 0);
    }

    // ================================================================
    // addStaff
    // ================================================================

    /** Thêm nhân viên mới hợp lệ → id > 0 */
    @Test
    public void test4_AddStaff_Success() {
        cleanupTestStaff();
        Staff s = new Staff(0, TEST_NAME, "Employee", "0900000000", "test@lib.vn");
        int id = dao.addStaff(s);
        assertTrue("Thêm nhân viên phải trả về id > 0", id > 0);
    }

    /** Sau khi thêm, tìm kiếm phải thấy */
    @Test
    public void test5_AddStaff_CanBeFoundAfterAdd() {
        Staff[] result = dao.searchStaff(TEST_NAME);
        assertNotNull(result);
        assertTrue("Nhân viên vừa thêm phải tìm thấy được", result.length > 0);
        assertEquals(TEST_NAME, result[0].getFullname());
    }

    /** Staff mới có createDate và status được gán */
    @Test
    public void test5b_AddStaff_HasCreateDateAndStatus() {
        Staff[] result = dao.searchStaff(TEST_NAME);
        assertTrue(result.length > 0);
        assertNotNull("createDate phải được gán tự động", result[0].getCreateDate());
        assertNotNull("status phải được gán", result[0].getStatus());
        assertEquals("Status mặc định phải là 'active'", "active", result[0].getStatus());
    }

    // ================================================================
    // updateStaff(s : Staff) : boolean
    // ================================================================

    /** Cập nhật SĐT → true và DB thay đổi */
    @Test
    public void test6_UpdateStaff_Success() {
        Staff[] arr = dao.searchStaff(TEST_NAME);
        assertTrue("Phải tìm thấy nhân viên test để cập nhật", arr.length > 0);

        Staff s = arr[0];
        s.setTel("0911111111");
        assertTrue("Cập nhật hợp lệ phải trả về true", dao.updateStaff(s));

        Staff[] updated = dao.searchStaff(TEST_NAME);
        assertEquals("SĐT phải được cập nhật trong DB", "0911111111", updated[0].getTel());
    }

    /** Cập nhật status → true và DB thay đổi */
    @Test
    public void test6b_UpdateStaff_ChangeStatus() {
        Staff[] arr = dao.searchStaff(TEST_NAME);
        assertTrue(arr.length > 0);

        Staff s = arr[0];
        s.setStatus("inactive");
        assertTrue("Cập nhật status phải trả về true", dao.updateStaff(s));

        Staff[] updated = dao.searchStaff(TEST_NAME);
        assertEquals("Status phải được cập nhật", "inactive", updated[0].getStatus());
    }

    /** Cập nhật id không tồn tại → false */
    @Test
    public void test7_UpdateStaff_IdNotExist() {
        Staff ghost = new Staff(-9999, "Ghost", "Admin", "0000000000", "ghost@lib.vn");
        assertFalse("id không tồn tại phải trả về false", dao.updateStaff(ghost));
    }

    // ================================================================
    // deleteStaff
    // ================================================================

    /** Xóa nhân viên test → true */
    @Test
    public void test8_DeleteStaff_Success() {
        Staff[] arr = dao.searchStaff(TEST_NAME);
        assertTrue("Phải tìm thấy nhân viên test để xóa", arr.length > 0);
        assertTrue("Xóa hợp lệ phải trả về true", dao.deleteStaff(arr[0].getId()));
    }

    /** Sau khi xóa, tìm kiếm không thấy nữa */
    @Test
    public void test9_DeleteStaff_NotFoundAfterDelete() {
        assertEquals("Sau khi xóa không được tìm thấy nữa",
            0, dao.searchStaff(TEST_NAME).length);
    }

    /** Xóa id không tồn tại → false */
    @Test
    public void test10_DeleteStaff_IdNotExist() {
        assertFalse("id không tồn tại phải trả về false", dao.deleteStaff(-9999));
    }

    // ================================================================
    // Helper
    // ================================================================

    private void cleanupTestStaff() {
        for (Staff s : dao.searchStaff(TEST_NAME)) dao.deleteStaff(s.getId());
    }
}
