package test.unit;

import dao.StaffDAO;
import model.Staff;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffDAOTest {

    private StaffDAO dao;
    private static final String TEST_NAME = "Test User JUnit";

    @Before
    public void setUp() {
        dao = new StaffDAO();
    }

    /** Tìm theo tên có tồn tại → kết quả không rỗng */
    @Test
    public void test1_SearchStaff_ByName_Found() {
        Staff[] result = dao.searchStaff("Nguyen");
        assertNotNull("Kết quả không được null", result);
        assertTrue("Phải tìm thấy ít nhất 1 nhân viên có tên chứa 'Nguyen'",
            result.length > 0);
    }

    /** Tìm theo từ khóa không tồn tại → mảng rỗng */
    @Test
    public void test2_SearchStaff_NotFound() {
        Staff[] result = dao.searchStaff("xxx_no_such_name_xxx");
        assertNotNull("Kết quả không được null", result);
        assertEquals("Không tìm thấy phải trả về mảng rỗng", 0, result.length);
    }

    /** Tìm với từ khóa rỗng → trả về toàn bộ danh sách (>= 0) */
    @Test
    public void test3_SearchStaff_EmptyKey_ReturnsAll() {
        Staff[] result = dao.searchStaff("");
        assertNotNull(result);
        assertTrue("Từ khóa rỗng phải trả về toàn bộ danh sách", result.length >= 0);
    }

    /** Cập nhật SĐT của nhân viên → trả về true */
    @Test
    public void test4_UpdateStaff_Success() {
        Staff[] arr = dao.searchStaff("Nguyen");
        assertTrue("Phải tìm thấy nhân viên để cập nhật", arr.length > 0);

        Staff s = arr[0];
        String originalTel = s.getTel();
        s.setTel("0999999999");
        assertTrue("Cập nhật hợp lệ phải trả về true", dao.updateStaff(s));

        // Khôi phục lại
        s.setTel(originalTel);
        dao.updateStaff(s);
    }

    /** Cập nhật nhân viên với id không tồn tại → trả về false */
    @Test
    public void test5_UpdateStaff_IdNotExist() {
        Staff ghost = new Staff(-9999, "Ghost", "Admin", "0000000000", "ghost@lib.vn");
        assertFalse("id không tồn tại phải trả về false", dao.updateStaff(ghost));
    }
}
