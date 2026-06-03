package test.unit;

import dao.AccountDAO;
import model.Account;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountDAOTest {

    private AccountDAO dao;

    // Tài khoản test tạm – tự dọn sau khi chạy
    private static final String TEST_USERNAME = "test_junit_user";
    private static final String TEST_PASSWORD = "testpass123";

    @Before
    public void setUp() {
        dao = new AccountDAO();
    }

    // ================================================================
    // NHÓM 1: Đăng nhập (checkLogin)
    // ================================================================

    /** Đăng nhập đúng → trả về Account đầy đủ, không null */
    @Test
    public void test01_CheckLogin_Success() {
        Account user = dao.checkLogin("admin", "123456");
        assertNotNull("Đăng nhập đúng phải trả về Account", user);
        assertNotNull("idAcc phải được gán", user.getIdAcc());
        assertNotNull("role phải được gán", user.getRole());
        assertNotNull("status phải được gán", user.getStatus());
    }

    /** Sai mật khẩu → null */
    @Test
    public void test02_CheckLogin_WrongPassword() {
        assertNull("Sai mật khẩu phải trả về null", dao.checkLogin("admin", "wrong"));
    }

    /** Username không tồn tại → null */
    @Test
    public void test03_CheckLogin_UserNotExist() {
        assertNull("Username không tồn tại phải trả về null",
            dao.checkLogin("xxx_ghost_xxx", "123456"));
    }

    /** Username rỗng → null */
    @Test
    public void test04_CheckLogin_EmptyUsername() {
        assertNull("Username rỗng phải trả về null", dao.checkLogin("", "123456"));
    }

    /** Password rỗng → null */
    @Test
    public void test05_CheckLogin_EmptyPassword() {
        assertNull("Password rỗng phải trả về null", dao.checkLogin("admin", ""));
    }

    // ================================================================
    // NHÓM 2: Phân quyền – Role đúng theo từng tài khoản
    // ================================================================

    /** Tài khoản admin → role = "Admin" */
    @Test
    public void test06_Role_AdminAccount() {
        Account user = dao.checkLogin("admin", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản admin phải có role Admin",
            Account.ROLE_ADMIN, user.getRole());
    }

    /** Tài khoản manager → role = "Manager" */
    @Test
    public void test07_Role_ManagerAccount() {
        Account user = dao.checkLogin("manager", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản manager phải có role Manager",
            Account.ROLE_MANAGER, user.getRole());
    }

    /** Tài khoản employee → role = "Employee" */
    @Test
    public void test08_Role_EmployeeAccount() {
        Account user = dao.checkLogin("cuong", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản cuong phải có role Employee",
            Account.ROLE_EMPLOYEE, user.getRole());
    }

    // ================================================================
    // NHÓM 3: Helper method isAdmin / isManager / isEmployee
    // ================================================================

    /** isAdmin() đúng với Admin, false với các role khác */
    @Test
    public void test09_Permission_IsAdmin() {
        Account admin   = dao.checkLogin("admin",   "123456");
        Account manager = dao.checkLogin("manager", "123456");
        Account emp     = dao.checkLogin("cuong",   "123456");

        assertNotNull(admin); assertNotNull(manager); assertNotNull(emp);

        assertTrue("isAdmin() phải true với admin",    admin.isAdmin());
        assertFalse("isAdmin() phải false với manager", manager.isAdmin());
        assertFalse("isAdmin() phải false với employee", emp.isAdmin());
    }

    /** isManager() đúng với Manager, false với các role khác */
    @Test
    public void test10_Permission_IsManager() {
        Account admin   = dao.checkLogin("admin",   "123456");
        Account manager = dao.checkLogin("manager", "123456");
        Account emp     = dao.checkLogin("cuong",   "123456");

        assertNotNull(admin); assertNotNull(manager); assertNotNull(emp);

        assertFalse("isManager() phải false với admin",    admin.isManager());
        assertTrue("isManager() phải true với manager",    manager.isManager());
        assertFalse("isManager() phải false với employee", emp.isManager());
    }

    /** isEmployee() đúng với Employee, false với các role khác */
    @Test
    public void test11_Permission_IsEmployee() {
        Account admin   = dao.checkLogin("admin",   "123456");
        Account manager = dao.checkLogin("manager", "123456");
        Account emp     = dao.checkLogin("cuong",   "123456");

        assertNotNull(admin); assertNotNull(manager); assertNotNull(emp);

        assertFalse("isEmployee() phải false với admin",    admin.isEmployee());
        assertFalse("isEmployee() phải false với manager",  manager.isEmployee());
        assertTrue("isEmployee() phải true với employee",   emp.isEmployee());
    }

    /** Chỉ đúng một role tại một thời điểm (mutual exclusion) */
    @Test
    public void test12_Permission_MutualExclusion() {
        Account admin   = dao.checkLogin("admin",   "123456");
        Account manager = dao.checkLogin("manager", "123456");
        Account emp     = dao.checkLogin("cuong",   "123456");

        // admin chỉ là Admin
        assertTrue(admin.isAdmin());
        assertFalse(admin.isManager());
        assertFalse(admin.isEmployee());

        // manager chỉ là Manager
        assertFalse(manager.isAdmin());
        assertTrue(manager.isManager());
        assertFalse(manager.isEmployee());

        // employee chỉ là Employee
        assertFalse(emp.isAdmin());
        assertFalse(emp.isManager());
        assertTrue(emp.isEmployee());
    }

    // ================================================================
    // NHÓM 4: Tài khoản bị khóa (status = inactive)
    // ================================================================

    /**
     * Tài khoản inactive không được đăng nhập.
     * Test này: thêm tài khoản inactive, thử đăng nhập, phải trả về null.
     */
    @Test
    public void test13_InactiveAccount_CannotLogin() {
        // Dọn nếu còn sót từ lần trước
        cleanupTestAccount();

        // Thêm tài khoản inactive
        Account locked = new Account();
        locked.setIdAcc(dao.generateNextId());
        locked.setUsername(TEST_USERNAME);
        locked.setPassword(TEST_PASSWORD);
        locked.setRole(Account.ROLE_EMPLOYEE);
        locked.setStatus("inactive");
        assertTrue("Thêm tài khoản inactive phải thành công", dao.addAccount(locked));

        // Thử đăng nhập → phải bị từ chối
        Account result = dao.checkLogin(TEST_USERNAME, TEST_PASSWORD);
        assertNull("Tài khoản inactive không được đăng nhập", result);

        // Dọn dẹp
        cleanupTestAccount();
    }

    /**
     * Kích hoạt lại tài khoản (inactive → active) thì đăng nhập được.
     */
    @Test
    public void test14_ReactivatedAccount_CanLogin() {
        cleanupTestAccount();

        // Tạo tài khoản inactive
        Account locked = new Account();
        locked.setIdAcc(dao.generateNextId());
        locked.setUsername(TEST_USERNAME);
        locked.setPassword(TEST_PASSWORD);
        locked.setRole(Account.ROLE_EMPLOYEE);
        locked.setStatus("inactive");
        dao.addAccount(locked);

        // Kích hoạt lại
        locked.setStatus("active");
        assertTrue("Update status → active phải thành công", dao.updateAccount(locked));

        // Đăng nhập lại phải được
        Account result = dao.checkLogin(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull("Sau khi active lại phải đăng nhập được", result);
        assertEquals("active", result.getStatus());

        cleanupTestAccount();
    }

    // ================================================================
    // NHÓM 5: CRUD tài khoản (Admin quản lý tài khoản)
    // ================================================================

    /** Thêm tài khoản mới hợp lệ → true */
    @Test
    public void test15_AddAccount_Success() {
        cleanupTestAccount();

        Account a = buildTestAccount(Account.ROLE_EMPLOYEE, "active");
        assertTrue("Thêm tài khoản hợp lệ phải trả về true", dao.addAccount(a));

        cleanupTestAccount();
    }

    /** Username trùng → isUsernameExists trả về true */
    @Test
    public void test16_UsernameExists_Duplicate() {
        cleanupTestAccount();
        dao.addAccount(buildTestAccount(Account.ROLE_EMPLOYEE, "active"));

        assertTrue("Username đã tồn tại phải trả về true",
            dao.isUsernameExists(TEST_USERNAME, null));

        cleanupTestAccount();
    }

    /** Username chưa tồn tại → isUsernameExists trả về false */
    @Test
    public void test17_UsernameExists_NotFound() {
        assertFalse("Username không tồn tại phải trả về false",
            dao.isUsernameExists("xxx_not_exist_xxx", null));
    }

    /** Cập nhật role từ Employee → Manager (phân quyền) */
    @Test
    public void test18_UpdateAccount_ChangeRole() {
        cleanupTestAccount();
        Account a = buildTestAccount(Account.ROLE_EMPLOYEE, "active");
        dao.addAccount(a);

        // Tìm lại account vừa tạo
        ArrayList<Account> list = dao.searchAccounts(TEST_USERNAME);
        assertFalse("Phải tìm thấy tài khoản test", list.isEmpty());

        Account found = list.get(0);
        assertEquals("Role ban đầu phải là Employee", Account.ROLE_EMPLOYEE, found.getRole());

        // Đổi role → Manager
        found.setRole(Account.ROLE_MANAGER);
        assertTrue("Cập nhật role phải thành công", dao.updateAccount(found));

        // Kiểm tra DB thực sự thay đổi
        ArrayList<Account> updated = dao.searchAccounts(TEST_USERNAME);
        assertEquals("Role phải được đổi thành Manager",
            Account.ROLE_MANAGER, updated.get(0).getRole());

        cleanupTestAccount();
    }

    /** Xóa tài khoản → không tìm thấy nữa */
    @Test
    public void test19_DeleteAccount_Success() {
        cleanupTestAccount();
        Account a = buildTestAccount(Account.ROLE_EMPLOYEE, "active");
        dao.addAccount(a);

        ArrayList<Account> list = dao.searchAccounts(TEST_USERNAME);
        assertFalse("Phải tìm thấy tài khoản để xóa", list.isEmpty());

        assertTrue("Xóa tài khoản phải thành công",
            dao.deleteAccount(list.get(0).getIdAcc()));

        ArrayList<Account> after = dao.searchAccounts(TEST_USERNAME);
        assertTrue("Sau khi xóa không được tìm thấy nữa", after.isEmpty());
    }

    /** generateNextId sinh ra ID đúng định dạng ACC### */
    @Test
    public void test20_GenerateNextId_Format() {
        String id = dao.generateNextId();
        assertNotNull("ID không được null", id);
        assertTrue("ID phải bắt đầu bằng ACC", id.startsWith("ACC"));
        assertEquals("ID phải có đúng 6 ký tự (ACC + 3 số)", 6, id.length());
    }

    // ================================================================
    // Helper
    // ================================================================

    private Account buildTestAccount(String role, String status) {
        Account a = new Account();
        a.setIdAcc(dao.generateNextId());
        a.setUsername(TEST_USERNAME);
        a.setPassword(TEST_PASSWORD);
        a.setRole(role);
        a.setStatus(status);
        return a;
    }

    private void cleanupTestAccount() {
        ArrayList<Account> list = dao.searchAccounts(TEST_USERNAME);
        for (Account a : list) dao.deleteAccount(a.getIdAcc());
    }
}
