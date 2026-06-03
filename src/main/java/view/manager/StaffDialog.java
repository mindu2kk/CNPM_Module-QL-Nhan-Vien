package view.manager;

import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Dialog thêm/sửa thông tin nhân viên.
 * Các field theo diagram EditStaffFrm:
 *   txtFullName, txtRole, txtEmail, txtTel, txtCreateDate, txtStatus
 */
public class StaffDialog extends JDialog {

    // Theo diagram EditStaffFrm
    private JTextField    txtFullName;
    private JTextField    txtRole;        // dùng JComboBox nhưng giữ tên theo diagram
    private JTextField    txtEmail;
    private JTextField    txtTel;
    private JTextField    txtCreateDate;  // chỉ đọc – hiển thị ngày tạo
    private JTextField    txtStatus;      // JComboBox nhưng tên theo diagram

    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbStatus;

    private JButton btnSave, btnCancel;
    private Staff   result = null;

    private static final String[] ROLES    = {"Employee", "Manager", "Admin"};
    private static final String[] STATUSES = {"active", "inactive"};
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public StaffDialog(Frame parent, String title, Staff staff) {
        super(parent, title, true);
        initComponents(staff);
        setSize(420, 340);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents(Staff staff) {
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        form.setBackground(new Color(245, 246, 250));

        // txtFullName
        form.add(lbl("Họ tên (*):"));
        txtFullName = new JTextField(staff != null ? staff.getFullname() : "");
        form.add(txtFullName);

        // txtRole → hiển thị bằng JComboBox
        form.add(lbl("Chức vụ (*):"));
        cmbRole = new JComboBox<>(ROLES);
        if (staff != null && staff.getRole() != null) cmbRole.setSelectedItem(staff.getRole());
        form.add(cmbRole);

        // txtTel
        form.add(lbl("Điện thoại:"));
        txtTel = new JTextField(staff != null ? nvl(staff.getTel()) : "");
        form.add(txtTel);

        // txtEmail
        form.add(lbl("Email:"));
        txtEmail = new JTextField(staff != null ? nvl(staff.getEmail()) : "");
        form.add(txtEmail);

        // txtCreateDate – chỉ đọc
        form.add(lbl("Ngày tạo:"));
        txtCreateDate = new JTextField(
            staff != null && staff.getCreateDate() != null
                ? SDF.format(staff.getCreateDate()) : "Tự động");
        txtCreateDate.setEditable(false);
        txtCreateDate.setBackground(new Color(230, 230, 230));
        form.add(txtCreateDate);

        // txtStatus → hiển thị bằng JComboBox
        form.add(lbl("Trạng thái:"));
        cmbStatus = new JComboBox<>(STATUSES);
        if (staff != null && staff.getStatus() != null) cmbStatus.setSelectedItem(staff.getStatus());
        form.add(cmbStatus);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnPanel.setBackground(new Color(245, 246, 250));
        btnSave   = btn("💾 Lưu", new Color(160, 220, 180));
        btnCancel = btn("✖ Hủy",  new Color(200, 200, 200));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 246, 250));
        main.add(form,     BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);
        add(main);
    }

    private void save() {
        String fn = txtFullName.getText().trim();
        if (fn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên nhân viên.");
            return;
        }
        result = new Staff();
        result.setFullname(fn);
        result.setRole((String) cmbRole.getSelectedItem());
        result.setTel(txtTel.getText().trim());
        result.setEmail(txtEmail.getText().trim());
        result.setStatus((String) cmbStatus.getSelectedItem());
        dispose();
    }

    /** Trả về Staff đã nhập, null nếu người dùng bấm Hủy */
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
