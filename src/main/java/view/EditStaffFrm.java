package view;

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
    private Staff      staff;
    private Account    user;

    public EditStaffFrm(Account u, Staff s) {
        this.user  = u;
        this.staff = s;
        setTitle("Sửa thông tin nhân viên - ID " + s.getId());
        setSize(420, 280);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        form.setBackground(new Color(245, 246, 250));

        form.add(new JLabel("Fullname:"));
        txtFullName = new JTextField(staff.getFullname());
        form.add(txtFullName);

        form.add(new JLabel("Role:"));
        txtRole = new JTextField(staff.getRole());
        form.add(txtRole);

        form.add(new JLabel("Tel:"));
        txtTel = new JTextField(staff.getTel());
        form.add(txtTel);

        form.add(new JLabel("Email:"));
        txtEmail = new JTextField(staff.getEmail());
        form.add(txtEmail);

        btnEdit = new JButton("Update");
        btnEdit.setBackground(new Color(160, 220, 180));
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(this);
        form.add(new JLabel());
        form.add(btnEdit);

        add(form);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEdit) {
            staff.setFullname(txtFullName.getText().trim());
            staff.setRole(txtRole.getText().trim());
            staff.setTel(txtTel.getText().trim());
            staff.setEmail(txtEmail.getText().trim());

            StaffDAO dao = new StaffDAO();
            if (dao.updateStaff(staff)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
