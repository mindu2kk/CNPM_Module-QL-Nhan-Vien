package view.staff;

import dao.StaffDAO;
import model.Account;
import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditStaffFrm extends JFrame implements ActionListener {

    private JTextField txtFullName, txtRole, txtTel, txtEmail;
    private JButton    btnEdit;
    private Staff      s;
    private Account    user;

    public EditStaffFrm(Account u, Staff staff) {
        this.user = u;
        this.s    = staff;

        setTitle("Sửa thông tin nhân viên - ID " + staff.getId());
        setSize(420, 280);
        setLocationRelativeTo(null);

        JPanel pn = new JPanel(new GridLayout(5, 2, 8, 8));
        pn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pn.add(new JLabel("Fullname:"));
        txtFullName = new JTextField(s.getFullname());
        pn.add(txtFullName);

        pn.add(new JLabel("Role:"));
        txtRole = new JTextField(s.getRole());
        pn.add(txtRole);

        pn.add(new JLabel("Tel:"));
        txtTel = new JTextField(s.getTel());
        pn.add(txtTel);

        pn.add(new JLabel("Email:"));
        txtEmail = new JTextField(s.getEmail());
        pn.add(txtEmail);

        btnEdit = new JButton("Update");
        btnEdit.addActionListener(this);
        pn.add(new JLabel());
        pn.add(btnEdit);

        add(pn);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEdit) {
            s.setFullname(txtFullName.getText().trim());
            s.setRole(txtRole.getText().trim());
            s.setTel(txtTel.getText().trim());
            s.setEmail(txtEmail.getText().trim());

            StaffDAO dao = new StaffDAO();
            if (dao.updateStaff(s)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
