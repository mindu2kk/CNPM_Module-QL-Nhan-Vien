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

    /** Đăng nhập đúng username + password → trả về true và idAcc được gán */
    @Test
    public void testCheckLogin_Success() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("123456");

        assertTrue("Đăng nhập đúng phải trả về true", dao.checkLogin(u));
        assertNotNull("idAcc phải được gán sau khi đăng nhập thành công", u.getIdAcc());
    }

    /** Đăng nhập sai mật khẩu → trả về false */
    @Test
    public void testCheckLogin_WrongPassword() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("wrong_pw");

        assertFalse("Sai mật khẩu phải trả về false", dao.checkLogin(u));
    }

    /** Đăng nhập với username không tồn tại → trả về false */
    @Test
    public void testCheckLogin_UserNotExist() {
        Account u = new Account();
        u.setUsername("not_exist_user");
        u.setPassword("123456");

        assertFalse("Username không tồn tại phải trả về false", dao.checkLogin(u));
    }

    /** Đăng nhập với username rỗng → trả về false */
    @Test
    public void testCheckLogin_EmptyUsername() {
        Account u = new Account();
        u.setUsername("");
        u.setPassword("123456");

        assertFalse("Username rỗng phải trả về false", dao.checkLogin(u));
    }

    /** Đăng nhập với password rỗng → trả về false */
    @Test
    public void testCheckLogin_EmptyPassword() {
        Account u = new Account();
        u.setUsername("admin");
        u.setPassword("");

        assertFalse("Password rỗng phải trả về false", dao.checkLogin(u));
    }
}
