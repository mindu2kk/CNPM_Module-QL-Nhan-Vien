package view.user;

import dao.AccountDAO;
import model.Account;
import view.admin.AdminHomeFrm;
import view.manager.ManagerHomeFrm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginFrm extends JFrame implements ActionListener {

    private JTextField     txtUN;
    private JPasswordField txtPW;
    private JButton        btnLogin;

    public LoginFrm() {
        initComponents();
        setTitle("Đăng nhập - Hệ thống Quản lý Nhân viên");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        main.setBackground(new Color(245, 246, 250));

        // ---- Header ----
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 75, 160));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        // ---- Form ----
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 14));
        form.setBackground(new Color(245, 246, 250));

        JLabel lblUN = new JLabel("Tên đăng nhập:");
        lblUN.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUN = new JTextField();
        txtUN.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblPW = new JLabel("Mật khẩu:");
        lblPW.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPW = new JPasswordField();
        txtPW.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPW.addActionListener(this); // Enter để đăng nhập

        form.add(lblUN); form.add(txtUN);
        form.add(lblPW); form.add(txtPW);

        // ---- Button ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        btnPanel.setBackground(new Color(245, 246, 250));
        btnLogin = new JButton("  Đăng nhập  ");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(new Color(180, 200, 240));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setPreferredSize(new Dimension(150, 36));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this);
        btnPanel.add(btnLogin);

        main.add(lblTitle,  BorderLayout.NORTH);
        main.add(form,      BorderLayout.CENTER);
        main.add(btnPanel,  BorderLayout.SOUTH);
        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = txtUN.getText().trim();
        String password = new String(txtPW.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account user = new Account();
        user.setUsername(username);
        user.setPassword(password);

        AccountDAO dao = new AccountDAO();
        if (dao.checkLogin(user)) {  // checkLogin trả về boolean, cập nhật role vào user
            this.dispose();
            // Phân quyền theo role
            switch (user.getRole()) {
                case Account.ROLE_ADMIN:
                    new AdminHomeFrm(user).setVisible(true);
                    break;
                case Account.ROLE_MANAGER:
                    new ManagerHomeFrm(user).setVisible(true);
                    break;
                default: // Employee
                    new EmployeeHomeFrm(user).setVisible(true);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Tên đăng nhập hoặc mật khẩu không đúng,\nhoặc tài khoản đã bị khóa.",
                "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            txtPW.setText("");
            txtPW.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new LoginFrm().setVisible(true);
        });
    }
}
