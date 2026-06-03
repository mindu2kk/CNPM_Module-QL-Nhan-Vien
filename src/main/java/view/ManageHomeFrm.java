package view;

import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageStaff;
    private JButton btnLogout;
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

        String role = user.getRole() != null ? user.getRole() : "Employee";

        // Nút quản lý nhân viên — chỉ Manager
        if ("Manager".equalsIgnoreCase(role)) {
            btnManageStaff = new JButton("Quản lý nhân viên");
            btnManageStaff.setPreferredSize(new Dimension(180, 40));
            btnManageStaff.setBackground(new Color(160, 210, 240));
            btnManageStaff.setForeground(Color.BLACK);
            btnManageStaff.setFocusPainted(false);
            btnManageStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnManageStaff.addActionListener(this);
            main.add(btnManageStaff);
        }

        // Nút đăng xuất — tất cả
        btnLogout = new JButton("Đăng xuất");
        btnLogout.setPreferredSize(new Dimension(180, 40));
        btnLogout.setBackground(new Color(240, 180, 170));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(this);
        main.add(btnLogout);

        // Thông báo cho Employee / Admin không có chức năng đặc biệt
        if (!"Manager".equalsIgnoreCase(role)) {
            JLabel lbl = new JLabel("<html>Xin chào, <b>" + user.getUsername() +
                "</b>!&nbsp;&nbsp;Vai trò: " + role + "</html>");
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            main.add(lbl);
        }

        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnManageStaff) {
            new ManageStaffFrm(user).setVisible(true);
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
