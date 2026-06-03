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

    /** Đăng nhập đúng → trả về Account với đầy đủ idAcc và role */
    @Test
    public void testCheckLogin_Success() {
        Account user = dao.checkLogin("admin", "123456");
        assertNotNull("Đăng nhập đúng phải trả về Account, không phải null", user);
        assertNotNull("idAcc phải được gán", user.getIdAcc());
        assertNotNull("Role phải được gán", user.getRole());
    }

    /** Đăng nhập đúng với role Admin → role phải là 'Admin' */
    @Test
    public void testCheckLogin_AdminRole() {
        Account user = dao.checkLogin("admin", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản admin phải có role Admin",
            Account.ROLE_ADMIN, user.getRole());
    }

    /** Đăng nhập đúng với role Manager */
    @Test
    public void testCheckLogin_ManagerRole() {
        Account user = dao.checkLogin("manager", "123456");
        assertNotNull(user);
        assertEquals("Tài khoản manager phải có role Manager",
            Account.ROLE_MANAGER, user.getRole());
    }

    /** Đăng nhập sai mật khẩu → trả về null */
    @Test
    public void testCheckLogin_WrongPassword() {
        Account user = dao.checkLogin("admin", "wrong_pw");
        assertNull("Sai mật khẩu phải trả về null", user);
    }

    /** Đăng nhập username không tồn tại → trả về null */
    @Test
    public void testCheckLogin_UserNotExist() {
        Account user = dao.checkLogin("not_exist_user", "123456");
        assertNull("Username không tồn tại phải trả về null", user);
    }

    /** Đăng nhập username rỗng → trả về null */
    @Test
    public void testCheckLogin_EmptyUsername() {
        Account user = dao.checkLogin("", "123456");
        assertNull("Username rỗng phải trả về null", user);
    }

    /** Đăng nhập password rỗng → trả về null */
    @Test
    public void testCheckLogin_EmptyPassword() {
        Account user = dao.checkLogin("admin", "");
        assertNull("Password rỗng phải trả về null", user);
    }

    /** isAdmin() / isManager() helper đúng */
    @Test
    public void testRoleHelpers() {
        Account user = dao.checkLogin("admin", "123456");
        assertNotNull(user);
        assertTrue("isAdmin() phải trả về true cho admin", user.isAdmin());
        assertFalse("isManager() phải false cho admin", user.isManager());
    }
}
