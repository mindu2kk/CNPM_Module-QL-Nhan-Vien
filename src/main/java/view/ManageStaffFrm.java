package view;

import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageStaffFrm extends JFrame implements ActionListener {

    private JButton btnEditStaff;
    private Account user;

    public ManageStaffFrm(Account u) {
        this.user = u;
        setTitle("Quản lý nhân viên");
        setSize(420, 240);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        main.setBackground(new Color(245, 246, 250));

        btnEditStaff = new JButton("Sửa thông tin nhân viên");
        btnEditStaff.setPreferredSize(new Dimension(200, 40));
        btnEditStaff.setBackground(new Color(250, 200, 140));
        btnEditStaff.setForeground(Color.BLACK);
        btnEditStaff.setFocusPainted(false);
        btnEditStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditStaff.addActionListener(this);

        main.add(btnEditStaff);
        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditStaff) {
            new SearchStaffFrm(user).setVisible(true);
        }
    }
}
