package test.unit;

import dao.AccountDAO;
import model.Account;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountDAOTest {

    private AccountDAO dao;

    @Before
    public void setUp() {
        dao = new AccountDAO();
    }

    /** Đăng nhập đúng → true, idAcc + idRole + roleName + permissions được gán */
    @Test
    public void testCheckLogin_Success() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("123456");

        assertTrue("Đăng nhập đúng phải trả về true", dao.checkLogin(u));
        assertTrue("idAcc phải > 0", u.getIdAcc() > 0);
        assertNotNull("idRole phải được gán",   u.getIdRole());
        assertNotNull("roleName phải được gán", u.getRoleName());
        assertNotNull("permissions không được null", u.getPermissions());
    }

    /** Sai mật khẩu → false */
    @Test
    public void testCheckLogin_WrongPassword() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("wrong_pw");
        assertFalse("Sai mật khẩu phải trả về false", dao.checkLogin(u));
    }

    /** Username không tồn tại → false */
    @Test
    public void testCheckLogin_UserNotExist() {
        Account u = new Account();
        u.setUsername("xxx_ghost_xxx");
        u.setPassword("123456");
        assertFalse("Username không tồn tại phải trả về false", dao.checkLogin(u));
    }

    /** Username rỗng → false */
    @Test
    public void testCheckLogin_EmptyUsername() {
        Account u = new Account();
        u.setUsername("");
        u.setPassword("123456");
        assertFalse("Username rỗng phải trả về false", dao.checkLogin(u));
    }

    /** Password rỗng → false */
    @Test
    public void testCheckLogin_EmptyPassword() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("");
        assertFalse("Password rỗng phải trả về false", dao.checkLogin(u));
    }

    /** Admin phải có đủ 4 permissions */
    @Test
    public void testCheckLogin_AdminHasAllPermissions() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("123456");
        dao.checkLogin(u);

        assertTrue("Admin phải có MANAGE_STAFF",   u.hasPermission("MANAGE_STAFF"));
        assertTrue("Admin phải có VIEW_STAFF",     u.hasPermission("VIEW_STAFF"));
        assertTrue("Admin phải có MANAGE_ACCOUNT", u.hasPermission("MANAGE_ACCOUNT"));
        assertTrue("Admin phải có VIEW_ACCOUNT",   u.hasPermission("VIEW_ACCOUNT"));
    }

    /** Manager chỉ có MANAGE_STAFF và VIEW_STAFF */
    @Test
    public void testCheckLogin_ManagerPermissions() {
        Account u = new Account();
        u.setUsername("manager");
        u.setPassword("123456");
        dao.checkLogin(u);

        assertTrue("Manager phải có MANAGE_STAFF", u.hasPermission("MANAGE_STAFF"));
        assertTrue("Manager phải có VIEW_STAFF",   u.hasPermission("VIEW_STAFF"));
        assertFalse("Manager không có MANAGE_ACCOUNT", u.hasPermission("MANAGE_ACCOUNT"));
        assertFalse("Manager không có VIEW_ACCOUNT",   u.hasPermission("VIEW_ACCOUNT"));
    }

    /** Employee chỉ có VIEW_STAFF */
    @Test
    public void testCheckLogin_EmployeePermissions() {
        Account u = new Account();
        u.setUsername("cuong");
        u.setPassword("123456");
        dao.checkLogin(u);

        assertFalse("Employee không có MANAGE_STAFF",   u.hasPermission("MANAGE_STAFF"));
        assertTrue ("Employee phải có VIEW_STAFF",      u.hasPermission("VIEW_STAFF"));
        assertFalse("Employee không có MANAGE_ACCOUNT", u.hasPermission("MANAGE_ACCOUNT"));
    }

    /** roleName đúng theo từng account */
    @Test
    public void testCheckLogin_RoleNames() {
        Account admin = login("admin",   "123456");
        Account mgr   = login("manager", "123456");
        Account emp   = login("cuong",   "123456");

        assertEquals("Admin",    admin.getRoleName());
        assertEquals("Manager",  mgr.getRoleName());
        assertEquals("Employee", emp.getRoleName());
    }

    /** hasPermission phân biệt đúng giữa các role */
    @Test
    public void testPermission_Isolation() {
        Account admin = login("admin",   "123456");
        Account mgr   = login("manager", "123456");
        Account emp   = login("cuong",   "123456");

        // MANAGE_ACCOUNT chỉ Admin
        assertTrue (admin.hasPermission("MANAGE_ACCOUNT"));
        assertFalse(mgr.hasPermission("MANAGE_ACCOUNT"));
        assertFalse(emp.hasPermission("MANAGE_ACCOUNT"));

        // MANAGE_STAFF: Admin + Manager
        assertTrue (admin.hasPermission("MANAGE_STAFF"));
        assertTrue (mgr.hasPermission("MANAGE_STAFF"));
        assertFalse(emp.hasPermission("MANAGE_STAFF"));
    }

    // ---- helper ----
    private Account login(String username, String password) {
        Account u = new Account();
        u.setUsername(username);
        u.setPassword(password);
        dao.checkLogin(u);
        return u;
    }
}
