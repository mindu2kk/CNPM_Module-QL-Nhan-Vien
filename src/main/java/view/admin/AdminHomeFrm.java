package view.admin;

import model.Account;
import view.user.LoginFrm;

import javax.swing.*;
import java.awt.*;

/**
 * Trang chủ dành cho Admin.
 * Chức năng: Quản lý tài khoản (CRUD + phân quyền) + Đăng xuất.
 */
public class AdminHomeFrm extends JFrame {

    private final Account user;

    public AdminHomeFrm(Account u) {
        this.user = u;
        initComponents();
        setTitle("Admin - " + u.getUsername());
        setSize(500, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(245, 246, 250));

        main.add(buildHeader(), BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(1, 2, 20, 0));
        menu.setBackground(new Color(245, 246, 250));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 40, 15, 40));

        JButton btnAccount = buildBtn("🔐 Quản lý\nTài khoản", new Color(210, 175, 230));
        JButton btnLogout  = buildBtn("🚪 Đăng xuất",          new Color(240, 180, 170));

        btnAccount.addActionListener(e -> new ManageAccountFrm(user).setVisible(true));
        btnLogout.addActionListener(e  -> logout());

        menu.add(btnAccount);
        menu.add(btnLogout);
        main.add(menu, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(new Color(80, 30, 130));
        h.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Xin chào, " + user.getUsername() + "  |  Vai trò: Admin", SwingConstants.RIGHT);
        sub.setFont(new Font("Arial", Font.ITALIC, 11));
        sub.setForeground(new Color(220, 200, 255));
        h.add(title, BorderLayout.CENTER);
        h.add(sub,   BorderLayout.SOUTH);
        return h;
    }

    private JButton buildBtn(String text, Color bg) {
        JButton btn = new JButton("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 70));
        return btn;
    }

    private void logout() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrm().setVisible(true);
        }
    }
}
