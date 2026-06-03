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
    // NHÓM 1: Đăng nhập  –  checkLogin(user : Account) : boolean
    // ================================================================

    /** Đăng nhập đúng → true, user được cập nhật idAcc + role + status */
    @Test
    public void test01_CheckLogin_Success() {
        Account user = new Account();
        user.setUsername("admin");
        user.setPassword("123456");

        assertTrue("Đăng nhập đúng phải trả về true", dao.checkLogin(user));
        assertNotNull("idAcc phải được gán sau khi đăng nhập", user.getIdAcc());
        assertNotNull("role phải được gán sau khi đăng nhập",  user.getRole());
        assertNotNull("status phải được gán sau khi đăng nhập", user.getStatus());
    }

    /** Sai mật khẩu → false */
    @Test
    public void test02_CheckLogin_WrongPassword() {
        Account user = new Account();
        user.setUsername("admin");
        user.setPassword("wrong_pw");
        assertFalse("Sai mật khẩu phải trả về false", dao.checkLogin(user));
    }

    /** Username không tồn tại → false */
    @Test
    public void test03_CheckLogin_UserNotExist() {
        Account user = new Account();
        user.setUsername("xxx_ghost_xxx");
        user.setPassword("123456");
        assertFalse("Username không tồn tại phải trả về false", dao.checkLogin(user));
    }

    /** Username rỗng → false */
    @Test
    public void test04_CheckLogin_EmptyUsername() {
        Account user = new Account();
        user.setUsername("");
        user.setPassword("123456");
        assertFalse("Username rỗng phải trả về false", dao.checkLogin(user));
    }

    /** Password rỗng → false */
    @Test
    public void test05_CheckLogin_EmptyPassword() {
        Account user = new Account();
        user.setUsername("admin");
        user.setPassword("");
        assertFalse("Password rỗng phải trả về false", dao.checkLogin(user));
    }

    // ================================================================
    // NHÓM 2: Phân quyền – Role đúng theo từng tài khoản
    // ================================================================

    /** Tài khoản admin → role = "Admin" */
    @Test
    public void test06_Role_AdminAccount() {
        Account user = loginAs("admin", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản admin phải có role Admin",
            Account.ROLE_ADMIN, user.getRole());
    }

    /** Tài khoản manager → role = "Manager" */
    @Test
    public void test07_Role_ManagerAccount() {
        Account user = loginAs("manager", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản manager phải có role Manager",
            Account.ROLE_MANAGER, user.getRole());
    }

    /** Tài khoản employee → role = "Employee" */
    @Test
    public void test08_Role_EmployeeAccount() {
        Account user = loginAs("cuong", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản cuong phải có role Employee",
            Account.ROLE_EMPLOYEE, user.getRole());
    }

    // ================================================================
    // NHÓM 3: Helper method isAdmin / isManager / isEmployee
    // ================================================================

    /** isAdmin() chỉ true với Admin */
    @Test
    public void test09_Permission_IsAdmin() {
        Account admin   = loginAs("admin",   "123456");
        Account manager = loginAs("manager", "123456");
        Account emp     = loginAs("cuong",   "123456");

        assertTrue ("isAdmin() phải true  với admin",    admin.isAdmin());
        assertFalse("isAdmin() phải false với manager",  manager.isAdmin());
        assertFalse("isAdmin() phải false với employee", emp.isAdmin());
    }

    /** isManager() chỉ true với Manager */
    @Test
    public void test10_Permission_IsManager() {
        Account admin   = loginAs("admin",   "123456");
        Account manager = loginAs("manager", "123456");
        Account emp     = loginAs("cuong",   "123456");

        assertFalse("isManager() phải false với admin",    admin.isManager());
        assertTrue ("isManager() phải true  với manager",  manager.isManager());
        assertFalse("isManager() phải false với employee", emp.isManager());
    }

    /** isEmployee() chỉ true với Employee */
    @Test
    public void test11_Permission_IsEmployee() {
        Account admin   = loginAs("admin",   "123456");
        Account manager = loginAs("manager", "123456");
        Account emp     = loginAs("cuong",   "123456");

        assertFalse("isEmployee() phải false với admin",    admin.isEmployee());
        assertFalse("isEmployee() phải false với manager",  manager.isEmployee());
        assertTrue ("isEmployee() phải true  với employee", emp.isEmployee());
    }

    /** Mỗi tài khoản chỉ có đúng 1 role (mutual exclusion) */
    @Test
    public void test12_Permission_MutualExclusion() {
        Account admin   = loginAs("admin",   "123456");
        Account manager = loginAs("manager", "123456");
        Account emp     = loginAs("cuong",   "123456");

        assertTrue(admin.isAdmin());
        assertFalse(admin.isManager());
        assertFalse(admin.isEmployee());

        assertFalse(manager.isAdmin());
        assertTrue(manager.isManager());
        assertFalse(manager.isEmployee());

        assertFalse(emp.isAdmin());
        assertFalse(emp.isManager());
        assertTrue(emp.isEmployee());
    }

    // ================================================================
    // NHÓM 4: Tài khoản bị khóa (status = inactive)
    // ================================================================

    /** Tài khoản inactive không được đăng nhập */
    @Test
    public void test13_InactiveAccount_CannotLogin() {
        cleanupTestAccount();
        Account locked = buildTestAccount(Account.ROLE_EMPLOYEE, "inactive");
        assertTrue("Thêm tài khoản inactive phải thành công", dao.addAccount(locked));

        Account tryLogin = new Account();
        tryLogin.setUsername(TEST_USERNAME);
        tryLogin.setPassword(TEST_PASSWORD);
        assertFalse("Tài khoản inactive không được đăng nhập", dao.checkLogin(tryLogin));

        cleanupTestAccount();
    }

    /** Kích hoạt lại tài khoản (inactive → active) thì đăng nhập được */
    @Test
    public void test14_ReactivatedAccount_CanLogin() {
        cleanupTestAccount();
        Account locked = buildTestAccount(Account.ROLE_EMPLOYEE, "inactive");
        dao.addAccount(locked);

        locked.setStatus("active");
        assertTrue("Update status → active phải thành công", dao.updateAccount(locked));

        Account tryLogin = new Account();
        tryLogin.setUsername(TEST_USERNAME);
        tryLogin.setPassword(TEST_PASSWORD);
        assertTrue("Sau khi active lại phải đăng nhập được", dao.checkLogin(tryLogin));
        assertEquals("active", tryLogin.getStatus());

        cleanupTestAccount();
    }

    // ================================================================
    // NHÓM 5: CRUD tài khoản
    // ================================================================

    /** Thêm tài khoản mới hợp lệ → true */
    @Test
    public void test15_AddAccount_Success() {
        cleanupTestAccount();
        assertTrue("Thêm tài khoản hợp lệ phải trả về true",
            dao.addAccount(buildTestAccount(Account.ROLE_EMPLOYEE, "active")));
        cleanupTestAccount();
    }

    /** Username trùng → isUsernameExists = true */
    @Test
    public void test16_UsernameExists_Duplicate() {
        cleanupTestAccount();
        dao.addAccount(buildTestAccount(Account.ROLE_EMPLOYEE, "active"));
        assertTrue("Username đã tồn tại phải trả về true",
            dao.isUsernameExists(TEST_USERNAME, null));
        cleanupTestAccount();
    }

    /** Username chưa tồn tại → isUsernameExists = false */
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

        ArrayList<Account> list = dao.searchAccounts(TEST_USERNAME);
        assertFalse("Phải tìm thấy tài khoản test", list.isEmpty());

        Account found = list.get(0);
        assertEquals(Account.ROLE_EMPLOYEE, found.getRole());

        found.setRole(Account.ROLE_MANAGER);
        assertTrue("Cập nhật role phải thành công", dao.updateAccount(found));

        ArrayList<Account> updated = dao.searchAccounts(TEST_USERNAME);
        assertEquals("Role phải được đổi thành Manager",
            Account.ROLE_MANAGER, updated.get(0).getRole());

        cleanupTestAccount();
    }

    /** Xóa tài khoản → không tìm thấy nữa */
    @Test
    public void test19_DeleteAccount_Success() {
        cleanupTestAccount();
        dao.addAccount(buildTestAccount(Account.ROLE_EMPLOYEE, "active"));

        ArrayList<Account> list = dao.searchAccounts(TEST_USERNAME);
        assertFalse("Phải tìm thấy tài khoản để xóa", list.isEmpty());

        assertTrue("Xóa tài khoản phải thành công",
            dao.deleteAccount(list.get(0).getIdAcc()));
        assertTrue("Sau khi xóa không được tìm thấy nữa",
            dao.searchAccounts(TEST_USERNAME).isEmpty());
    }

    /** generateNextId sinh ra ID đúng định dạng ACC### */
    @Test
    public void test20_GenerateNextId_Format() {
        String id = dao.generateNextId();
        assertNotNull(id);
        assertTrue("ID phải bắt đầu bằng ACC", id.startsWith("ACC"));
        assertEquals("ID phải có đúng 6 ký tự", 6, id.length());
    }

    // ================================================================
    // Helper
    // ================================================================

    /** Đăng nhập tiện ích, trả về Account đã được cập nhật */
    private Account loginAs(String username, String password) {
        Account u = new Account();
        u.setUsername(username);
        u.setPassword(password);
        dao.checkLogin(u);
        return u;
    }

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
        for (Account a : dao.searchAccounts(TEST_USERNAME))
            dao.deleteAccount(a.getIdAcc());
    }
}
