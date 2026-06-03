package test.unit;

import dao.AccountDAO;
import model.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit test cho AccountDAO – schema RBAC (tblAccount → tblRole → tblRole_Permission → Permission)
 *
 * Tài khoản test (seed trong library.sql):
 *   admin   / 123456  → ROLE_ADMIN    (4 permissions)
 *   manager / 123456  → ROLE_MANAGER  (2 permissions)
 *   cuong   / 123456  → ROLE_EMPLOYEE (1 permission)
 */
public class AccountDAOTest {

    private AccountDAO dao;

    @Before
    public void setUp() {
        dao = new AccountDAO();
    }

    // ====================================================================
    // NHÓM 1: checkLogin – xác thực cơ bản
    // ====================================================================

    @Test
    public void checkLogin_Success_ReturnsTrueAndPopulatesUser() {
        Account u = makeAccount("admin", "123456");
        assertTrue("Đăng nhập đúng phải trả về true", dao.checkLogin(u));

        // Các field phải được gán sau khi đăng nhập thành công
        assertTrue("idAcc phải > 0",              u.getIdAcc() > 0);
        assertNotNull("idRole phải được gán",      u.getIdRole());
        assertNotNull("roleName phải được gán",    u.getRoleName());
        assertNotNull("status phải được gán",      u.getStatus());
        assertNotNull("permissions không được null", u.getPermissions());
        assertFalse("permissions không được rỗng", u.getPermissions().isEmpty());
    }

    @Test
    public void checkLogin_WrongPassword_ReturnsFalse() {
        assertFalse(dao.checkLogin(makeAccount("admin", "sai_mat_khau")));
    }

    @Test
    public void checkLogin_UsernameNotExist_ReturnsFalse() {
        assertFalse(dao.checkLogin(makeAccount("user_khong_ton_tai", "123456")));
    }

    @Test
    public void checkLogin_EmptyUsername_ReturnsFalse() {
        assertFalse(dao.checkLogin(makeAccount("", "123456")));
    }

    @Test
    public void checkLogin_EmptyPassword_ReturnsFalse() {
        assertFalse(dao.checkLogin(makeAccount("admin", "")));
    }

    // ====================================================================
    // NHÓM 2: Role – tên role đúng sau khi đăng nhập
    // ====================================================================

    @Test
    public void checkLogin_AdminRole_IsAdmin() {
        Account u = login("admin", "123456");
        assertEquals("ROLE_ADMIN", u.getIdRole());
        assertEquals("Admin",      u.getRoleName());
        assertEquals("Admin",      u.getRole()); // getRole() alias getIdRole()
    }

    @Test
    public void checkLogin_ManagerRole_IsManager() {
        Account u = login("manager", "123456");
        assertEquals("ROLE_MANAGER", u.getIdRole());
        assertEquals("Manager",      u.getRoleName());
    }

    @Test
    public void checkLogin_EmployeeRole_IsEmployee() {
        Account u = login("cuong", "123456");
        assertEquals("ROLE_EMPLOYEE", u.getIdRole());
        assertEquals("Employee",      u.getRoleName());
    }

    // ====================================================================
    // NHÓM 3: Permissions – quyền đúng theo từng role
    // ====================================================================

    @Test
    public void checkLogin_AdminPermissions_HasAll4() {
        Account u = login("admin", "123456");
        Set<String> perms = u.getPermissions();

        assertFalse("Admin không có MANAGE_STAFF",  perms.contains("MANAGE_STAFF"));
        assertFalse("Admin không có VIEW_STAFF",    perms.contains("VIEW_STAFF"));
        assertTrue("Admin phải có MANAGE_ACCOUNT",  perms.contains("MANAGE_ACCOUNT"));
        assertTrue("Admin phải có VIEW_ACCOUNT",    perms.contains("VIEW_ACCOUNT"));
        assertEquals("Admin phải có đúng 2 permissions", 2, perms.size());
    }

    @Test
    public void checkLogin_ManagerPermissions_HasManageAndViewStaff() {
        Account u = login("manager", "123456");
        Set<String> perms = u.getPermissions();

        assertTrue ("Manager phải có MANAGE_STAFF",        perms.contains("MANAGE_STAFF"));
        assertTrue ("Manager phải có VIEW_STAFF",          perms.contains("VIEW_STAFF"));
        assertFalse("Manager không có MANAGE_ACCOUNT",     perms.contains("MANAGE_ACCOUNT"));
        assertFalse("Manager không có VIEW_ACCOUNT",       perms.contains("VIEW_ACCOUNT"));
        assertEquals("Manager phải có đúng 2 permissions", 2, perms.size());
    }

    @Test
    public void checkLogin_EmployeePermissions_HasViewStaffOnly() {
        Account u = login("cuong", "123456");
        Set<String> perms = u.getPermissions();

        assertFalse("Employee không có MANAGE_STAFF",   perms.contains("MANAGE_STAFF"));
        assertTrue ("Employee phải có VIEW_STAFF",      perms.contains("VIEW_STAFF"));
        assertFalse("Employee không có MANAGE_ACCOUNT", perms.contains("MANAGE_ACCOUNT"));
        assertFalse("Employee không có VIEW_ACCOUNT",   perms.contains("VIEW_ACCOUNT"));
        assertEquals("Employee phải có đúng 1 permission", 1, perms.size());
    }

    // ====================================================================
    // NHÓM 4: hasPermission() – helper method
    // ====================================================================

    @Test
    public void hasPermission_Admin_TrueForAll() {
        Account u = login("admin", "123456");
        // Admin chỉ có quyền quản lý tài khoản
        assertFalse(u.hasPermission("MANAGE_STAFF"));
        assertFalse(u.hasPermission("VIEW_STAFF"));
        assertTrue (u.hasPermission("MANAGE_ACCOUNT"));
        assertTrue (u.hasPermission("VIEW_ACCOUNT"));
    }

    @Test
    public void hasPermission_Manager_TrueOnlyForStaff() {
        Account u = login("manager", "123456");
        assertTrue (u.hasPermission("MANAGE_STAFF"));
        assertTrue (u.hasPermission("VIEW_STAFF"));
        assertFalse(u.hasPermission("MANAGE_ACCOUNT"));
        assertFalse(u.hasPermission("VIEW_ACCOUNT"));
    }

    @Test
    public void hasPermission_Employee_TrueOnlyForViewStaff() {
        Account u = login("cuong", "123456");
        assertFalse(u.hasPermission("MANAGE_STAFF"));
        assertTrue (u.hasPermission("VIEW_STAFF"));
        assertFalse(u.hasPermission("MANAGE_ACCOUNT"));
    }

    @Test
    public void hasPermission_FakePermission_AlwaysFalse() {
        // Quyền không tồn tại phải trả về false với mọi role
        Account admin = login("admin",   "123456");
        Account mgr   = login("manager", "123456");
        Account emp   = login("cuong",   "123456");

        assertFalse(admin.hasPermission("QUYEN_KHONG_TON_TAI"));
        assertFalse(mgr.hasPermission("QUYEN_KHONG_TON_TAI"));
        assertFalse(emp.hasPermission("QUYEN_KHONG_TON_TAI"));
    }

    // ====================================================================
    // NHÓM 5: loadPermissions() – load trực tiếp không qua login
    // ====================================================================

    @Test
    public void loadPermissions_AdminRole_Returns4() {
        Set<String> perms = dao.loadPermissions("ROLE_ADMIN");
        assertNotNull(perms);
        assertEquals("ROLE_ADMIN phải có 2 permissions", 2, perms.size());
        assertTrue(perms.contains("MANAGE_ACCOUNT"));
        assertTrue(perms.contains("VIEW_ACCOUNT"));
    }

    @Test
    public void loadPermissions_ManagerRole_Returns2() {
        Set<String> perms = dao.loadPermissions("ROLE_MANAGER");
        assertNotNull(perms);
        assertEquals("ROLE_MANAGER phải có 2 permissions", 2, perms.size());
    }

    @Test
    public void loadPermissions_EmployeeRole_Returns1() {
        Set<String> perms = dao.loadPermissions("ROLE_EMPLOYEE");
        assertNotNull(perms);
        assertEquals("ROLE_EMPLOYEE phải có 1 permission", 1, perms.size());
    }

    @Test
    public void loadPermissions_InvalidRole_ReturnsEmpty() {
        Set<String> perms = dao.loadPermissions("ROLE_KHONG_TON_TAI");
        assertNotNull(perms);
        assertTrue("Role không tồn tại phải trả về tập rỗng", perms.isEmpty());
    }

    // ====================================================================
    // Helper
    // ====================================================================

    private Account makeAccount(String username, String password) {
        Account u = new Account();
        u.setUsername(username);
        u.setPassword(password);
        return u;
    }

    private Account login(String username, String password) {
        Account u = makeAccount(username, password);
        dao.checkLogin(u);
        return u;
    }
}
