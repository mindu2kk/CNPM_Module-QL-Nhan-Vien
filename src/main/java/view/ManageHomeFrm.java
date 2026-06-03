package view;

import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageStaff;    // Manager: quản lý nhân viên
    private JButton btnManageAccount;  // Admin:   quản lý tài khoản
    private JButton btnLogout;         // Tất cả:  đăng xuất
    private Account user;

    public ManageHomeFrm(Account u) {
        this.user = u;
        setTitle("Trang chủ - " + u.getUsername() + " (" + u.getRole() + ")");
        setSize(460, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        main.setBackground(new Color(245, 246, 250));

        // Manager: có quyền MANAGE_STAFF → hiện nút quản lý nhân viên
        if (user.hasPermission("MANAGE_STAFF")) {
            btnManageStaff = new JButton("Quản lý nhân viên");
            btnManageStaff.setPreferredSize(new Dimension(180, 40));
            btnManageStaff.setBackground(new Color(160, 210, 240));
            btnManageStaff.setForeground(Color.BLACK);
            btnManageStaff.setFocusPainted(false);
            btnManageStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnManageStaff.addActionListener(this);
            main.add(btnManageStaff);
        }

        // Admin: có quyền MANAGE_ACCOUNT → hiện nút quản lý tài khoản
        if (user.hasPermission("MANAGE_ACCOUNT")) {
            btnManageAccount = new JButton("Quản lý tài khoản");
            btnManageAccount.setPreferredSize(new Dimension(180, 40));
            btnManageAccount.setBackground(new Color(210, 175, 230));
            btnManageAccount.setForeground(Color.BLACK);
            btnManageAccount.setFocusPainted(false);
            btnManageAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnManageAccount.addActionListener(this);
            main.add(btnManageAccount);
        }

        // Tất cả: đăng xuất
        btnLogout = new JButton("Đăng xuất");
        btnLogout.setPreferredSize(new Dimension(180, 40));
        btnLogout.setBackground(new Color(240, 180, 170));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(this);
        main.add(btnLogout);

        // Employee / Admin không có MANAGE_STAFF: hiện lời chào
        if (!user.hasPermission("MANAGE_STAFF") && !user.hasPermission("MANAGE_ACCOUNT")) {
            JLabel lbl = new JLabel("<html>Xin chào, <b>" + user.getUsername() +
                "</b>!&nbsp;&nbsp;Vai trò: " + user.getRole() + "</html>");
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            main.add(lbl);
        }

        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnManageStaff) {
            new ManageStaffFrm(user).setVisible(true);
        } else if (e.getSource() == btnManageAccount) {
            new ManageAccountFrm(user).setVisible(true);
        } else if (e.getSource() == btnLogout) {
            int ok = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrm().setVisible(true);
            }
        }
    }
}
