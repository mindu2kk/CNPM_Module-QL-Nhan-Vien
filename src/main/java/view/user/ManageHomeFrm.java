package view.user;

import model.Account;
import view.staff.ManageStaffFrm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageStaff;
    private Account user;

    public ManageHomeFrm(Account u) {
        this.user = u;
        initComponents();
        setTitle("Trang chủ - Quản lý nhân viên");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 80, 160));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Xin chào: " + user.getUsername(), SwingConstants.RIGHT);
        lblUser.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUser.setForeground(new Color(200, 220, 255));

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblUser, BorderLayout.SOUTH);

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        menuPanel.setBackground(new Color(245, 245, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        btnManageStaff = createMenuButton("👥 Quản lý nhân viên", new Color(52, 152, 219));
        btnManageStaff.addActionListener(this);

        JButton btnLogout = createMenuButton("🚪 Đăng xuất", new Color(231, 76, 60));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrm().setVisible(true);
            }
        });

        menuPanel.add(btnManageStaff);
        menuPanel.add(btnLogout);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnManageStaff) {
            new ManageStaffFrm(user).setVisible(true);
        }
    }
}
