package test.unit;

import dao.StaffDAO;
import model.Staff;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Unit test cho StaffDAO – bảng tblStaff(ID, fullname, role, tel, email)
 *
 * Dữ liệu seed (library.sql):
 *   ID=1  Nguyen Van An   Manager
 *   ID=2  Tran Thi Binh   Manager
 *   ID=3  Le Van Cuong    Employee
 *   ID=4  Pham Thi Dung   Employee
 *   ID=5  Hoang Van Em    Employee
 *
 * Test tự tạo / xóa bản ghi riêng (TEST_NAME) để không ảnh hưởng seed.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffDAOTest {

    private StaffDAO dao;

    // Tên dùng cho bản ghi test – khác hoàn toàn với seed
    private static final String TEST_NAME  = "__JUnit_TestStaff__";
    private static final String TEST_ROLE  = "Tester";
    private static final String TEST_TEL   = "0100000000";
    private static final String TEST_EMAIL = "test@junit.local";

    @Before
    public void setUp() {
        dao = new StaffDAO();
        cleanup(); // đảm bảo không còn dư từ lần chạy trước
    }

    // ====================================================================
    // NHÓM 1: searchStaff(key) : Staff[]
    // ====================================================================

    @Test
    public void test1_Search_ByName_Found() {
        Staff[] result = dao.searchStaff("Nguyen");
        assertNotNull(result);
        assertTrue("Phải tìm thấy ít nhất 1 nhân viên có tên 'Nguyen'",
            result.length > 0);
    }

    @Test
    public void test2_Search_ByName_NotFound() {
        Staff[] result = dao.searchStaff("xxxKhongTonTaixxx");
        assertNotNull(result);
        assertEquals("Từ khóa không tồn tại phải trả về mảng rỗng", 0, result.length);
    }

    @Test
    public void test3_Search_EmptyKey_ReturnsAll() {
        Staff[] result = dao.searchStaff("");
        assertNotNull(result);
        assertTrue("Từ khóa rỗng phải trả về toàn bộ danh sách (>= 5)", result.length >= 5);
    }

    @Test
    public void test4_Search_ById_Found() {
        // Seed có ID=1 → Nguyen Van An
        Staff[] result = dao.searchStaff("1");
        assertNotNull(result);
        assertTrue("Tìm theo ID '1' phải có kết quả", result.length > 0);
    }

    @Test
    public void test5_Search_ResultContainsCorrectFields() {
        Staff[] result = dao.searchStaff("Nguyen Van An");
        assertTrue(result.length > 0);
        Staff s = result[0];

        assertNotNull("fullname không được null", s.getFullname());
        assertNotNull("role không được null",     s.getRole());
        assertTrue("ID phải > 0",                 s.getId() > 0);
    }

    // ====================================================================
    // NHÓM 2: addStaff(s) : int  – trả về ID mới, -1 nếu lỗi
    // ====================================================================

    @Test
    public void test6_Add_ValidStaff_ReturnsPositiveId() {
        Staff s = new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL);
        int newId = dao.addStaff(s);
        assertTrue("addStaff hợp lệ phải trả về ID > 0", newId > 0);
        cleanup();
    }

    @Test
    public void test7_Add_StaffCanBeFoundAfterInsert() {
        Staff s = new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL);
        dao.addStaff(s);

        Staff[] found = dao.searchStaff(TEST_NAME);
        assertTrue("Nhân viên vừa thêm phải tìm thấy được", found.length > 0);
        assertEquals("Fullname phải khớp",  TEST_NAME,  found[0].getFullname());
        assertEquals("Role phải khớp",      TEST_ROLE,  found[0].getRole());
        assertEquals("Tel phải khớp",       TEST_TEL,   found[0].getTel());
        assertEquals("Email phải khớp",     TEST_EMAIL, found[0].getEmail());
        cleanup();
    }

    // ====================================================================
    // NHÓM 3: updateStaff(s) : boolean
    // ====================================================================

    @Test
    public void test8_Update_ValidChange_ReturnsTrue() {
        // Chuẩn bị
        int id = dao.addStaff(new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL));
        assertTrue(id > 0);

        // Cập nhật tel
        Staff toUpdate = dao.searchStaff(TEST_NAME)[0];
        toUpdate.setTel("0199999999");
        assertTrue("updateStaff hợp lệ phải trả về true", dao.updateStaff(toUpdate));

        // Xác nhận DB đã thay đổi
        Staff updated = dao.searchStaff(TEST_NAME)[0];
        assertEquals("Tel phải được cập nhật trong DB", "0199999999", updated.getTel());
        cleanup();
    }

    @Test
    public void test9_Update_NonExistentId_ReturnsFalse() {
        Staff ghost = new Staff(-9999, "Ghost", "Admin", "0000000000", "ghost@lib.vn");
        assertFalse("Update với ID không tồn tại phải trả về false", dao.updateStaff(ghost));
    }

    @Test
    public void test10_Update_AllFields_Correctly() {
        dao.addStaff(new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL));
        Staff s = dao.searchStaff(TEST_NAME)[0];

        s.setFullname(TEST_NAME + "_updated");
        s.setRole("Senior Tester");
        s.setTel("0188888888");
        s.setEmail("updated@junit.local");
        assertTrue(dao.updateStaff(s));

        Staff check = dao.searchStaff(TEST_NAME + "_updated")[0];
        assertEquals(TEST_NAME + "_updated", check.getFullname());
        assertEquals("Senior Tester",        check.getRole());
        assertEquals("0188888888",           check.getTel());
        assertEquals("updated@junit.local",  check.getEmail());
        cleanup();
    }

    // ====================================================================
    // NHÓM 4: deleteStaff(id) : boolean
    // ====================================================================

    @Test
    public void test11_Delete_ValidId_ReturnsTrue() {
        int id = dao.addStaff(new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL));
        assertTrue(id > 0);
        assertTrue("deleteStaff với ID hợp lệ phải trả về true", dao.deleteStaff(id));
    }

    @Test
    public void test12_Delete_NotFoundAfterDelete() {
        int id = dao.addStaff(new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL));
        dao.deleteStaff(id);

        Staff[] result = dao.searchStaff(TEST_NAME);
        assertEquals("Sau khi xóa không được tìm thấy nữa", 0, result.length);
    }

    @Test
    public void test13_Delete_NonExistentId_ReturnsFalse() {
        assertFalse("deleteStaff với ID không tồn tại phải trả về false",
            dao.deleteStaff(-9999));
    }

    @Test
    public void test14_Delete_SeedDataNotAffected() {
        // Xóa bản ghi test không được ảnh hưởng seed
        int id = dao.addStaff(new Staff(0, TEST_NAME, TEST_ROLE, TEST_TEL, TEST_EMAIL));
        dao.deleteStaff(id);

        Staff[] seeds = dao.searchStaff("");
        assertTrue("Dữ liệu seed phải còn đủ sau khi xóa bản ghi test",
            seeds.length >= 5);
    }

    // ====================================================================
    // Helper – dọn dữ liệu test
    // ====================================================================

    private void cleanup() {
        for (Staff s : dao.searchStaff(TEST_NAME)) dao.deleteStaff(s.getId());
        for (Staff s : dao.searchStaff(TEST_NAME + "_updated")) dao.deleteStaff(s.getId());
    }
}
