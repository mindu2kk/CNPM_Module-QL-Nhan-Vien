package view.manager;

import model.Staff;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog dùng chung để Thêm mới hoặc Sửa thông tin nhân viên.
 */
public class StaffDialog extends JDialog {

    private JTextField  txtFullname, txtTel, txtEmail;
    private JComboBox<String> cmbRole;
    private JButton     btnSave, btnCancel;
    private Staff       result = null;

    private static final String[] ROLES = {"Employee", "Manager", "Admin"};

    public StaffDialog(Frame parent, String title, Staff staff) {
        super(parent, title, true);
        initComponents(staff);
        setSize(400, 290);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents(Staff staff) {
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 10, 20));
        form.setBackground(new Color(245, 246, 250));

        form.add(lbl("Họ tên (*)"));
        txtFullname = new JTextField(staff != null ? staff.getFullname() : "");
        form.add(txtFullname);

        form.add(lbl("Chức vụ (*)"));
        cmbRole = new JComboBox<>(ROLES);
        if (staff != null) cmbRole.setSelectedItem(staff.getRole());
        form.add(cmbRole);

        form.add(lbl("Điện thoại"));
        txtTel = new JTextField(staff != null ? nvl(staff.getTel()) : "");
        form.add(txtTel);

        form.add(lbl("Email"));
        txtEmail = new JTextField(staff != null ? nvl(staff.getEmail()) : "");
        form.add(txtEmail);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnPanel.setBackground(new Color(245, 246, 250));
        btnSave   = btn("💾 Lưu",  new Color(160, 220, 180));
        btnCancel = btn("✖ Hủy",   new Color(200, 200, 200));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        btnSave.addActionListener(e -> {
            String fn = txtFullname.getText().trim();
            if (fn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên nhân viên.");
                return;
            }
            result = new Staff();
            result.setFullname(fn);
            result.setRole((String) cmbRole.getSelectedItem());
            result.setTel(txtTel.getText().trim());
            result.setEmail(txtEmail.getText().trim());
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 246, 250));
        main.add(form,     BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);
        add(main);
    }

    /** Trả về Staff đã nhập, null nếu người dùng bấm Hủy. */
    public Staff getResult() { return result; }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        return l;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }

    private String nvl(String s) { return s != null ? s : ""; }
}
