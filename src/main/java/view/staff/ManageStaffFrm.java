package view.staff;

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
        setTitle("Quản lí nhân viên");
        setSize(420, 240);
        setLocationRelativeTo(null);

        JPanel pn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        btnEditStaff = new JButton("Sửa thông tin nhân viên");
        btnEditStaff.addActionListener(this);
        pn.add(btnEditStaff);

        add(pn);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditStaff) {
            new SearchStaffFrm(user);
        }
    }
}
