package view.staff;

import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageStaff;
    private Account user;

    public ManageHomeFrm(Account u) {
        this.user = u;
        setTitle("Trang chủ Quản lí - " + u.getUsername());
        setSize(420, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        btnManageStaff = new JButton("Quản lí nhân viên");
        btnManageStaff.addActionListener(this);
        pn.add(btnManageStaff);

        add(pn);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnManageStaff) {
            new ManageStaffFrm(user);
        }
    }
}
