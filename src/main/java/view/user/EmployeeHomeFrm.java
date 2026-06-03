package view.user;

import model.Account;

import javax.swing.*;
import java.awt.*;

/**
 * Trang chủ dành cho Employee – chỉ có thông tin cá nhân và đăng xuất.
 */
public class EmployeeHomeFrm extends JFrame {

    private final Account user;

    public EmployeeHomeFrm(Account u) {
        this.user = u;
        initComponents();
        setTitle("Trang chủ - " + u.getUsername());
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(245, 246, 250));

        // Header
        JPanel header = buildHeader();
        main.add(header, BorderLayout.NORTH);

        // Nội dung
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 246, 250));
        JLabel lbl = new JLabel("Xin chào, " + user.getUsername() + "! (Nhân viên)");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        center.add(lbl);
        main.add(center, BorderLayout.CENTER);

        // Nút đăng xuất
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setBackground(new Color(245, 246, 250));
        JButton btnLogout = buildButton("🚪 Đăng xuất", new Color(240, 180, 170));
        btnLogout.addActionListener(e -> logout());
        bottom.add(btnLogout);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 75, 160));
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        JLabel role = new JLabel("Vai trò: " + user.getRole(), SwingConstants.RIGHT);
        role.setFont(new Font("Arial", Font.ITALIC, 11));
        role.setForeground(new Color(200, 220, 255));
        header.add(title, BorderLayout.CENTER);
        header.add(role,  BorderLayout.SOUTH);
        return header;
    }

    private JButton buildButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
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
