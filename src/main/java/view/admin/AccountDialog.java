package view.admin;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog dùng chung để Thêm mới hoặc Sửa tài khoản.
 * Cho phép Admin đặt Role (phân quyền) và Status cho tài khoản.
 */
public class AccountDialog extends JDialog {

    private JTextField    txtUsername, txtPassword, txtStaffId;
    private JComboBox<String> cmbRole, cmbStatus;
    private JButton       btnSave, btnCancel;
    private Account       result = null;
    private final boolean isNew;
    private final AccountDAO accountDAO;
    private final String  currentId;

    private static final String[] ROLES    = {Account.ROLE_EMPLOYEE, Account.ROLE_MANAGER, Account.ROLE_ADMIN};
    private static final String[] STATUSES = {"active", "inactive"};

    public AccountDialog(Frame parent, String title, Account account, AccountDAO dao) {
        super(parent, title, true);
        this.isNew      = (account == null);
        this.accountDAO = dao;
        this.currentId  = account != null ? account.getIdAcc() : null;
        initComponents(account);
        setSize(420, 330);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents(Account acc) {
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 10, 20));
        form.setBackground(new Color(245, 246, 250));

        form.add(lbl("Username (*)"));
        txtUsername = new JTextField(acc != null ? acc.getUsername() : "");
        form.add(txtUsername);

        form.add(lbl(isNew ? "Mật khẩu (*)" : "Mật khẩu mới (để trống = giữ nguyên)"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);

        form.add(lbl("Vai trò (*)"));
        cmbRole = new JComboBox<>(ROLES);
        if (acc != null) cmbRole.setSelectedItem(acc.getRole());
        form.add(cmbRole);

        form.add(lbl("Trạng thái (*)"));
        cmbStatus = new JComboBox<>(STATUSES);
        if (acc != null) cmbStatus.setSelectedItem(acc.getStatus());
        form.add(cmbStatus);

        form.add(lbl("Staff ID (liên kết, để trống nếu không có)"));
        txtStaffId = new JTextField(acc != null && acc.getStaffId() != null
            ? acc.getStaffId().toString() : "");
        form.add(txtStaffId);

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
        String username = txtUsername.getText().trim();
        String password = new String(((JPasswordField) txtPassword).getPassword()).trim();
        String role     = (String) cmbRole.getSelectedItem();
        String status   = (String) cmbStatus.getSelectedItem();
        String staffIdStr = txtStaffId.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập username.");
            return;
        }
        if (isNew && password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu.");
            return;
        }

        // Kiểm tra username trùng
        if (accountDAO.isUsernameExists(username, currentId)) {
            JOptionPane.showMessageDialog(this, "Username đã tồn tại, vui lòng chọn tên khác.");
            return;
        }

        Integer staffId = null;
        if (!staffIdStr.isEmpty()) {
            try { staffId = Integer.parseInt(staffIdStr); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Staff ID phải là số nguyên.");
                return;
            }
        }

        result = new Account();
        result.setUsername(username);
        result.setPassword(password.isEmpty() ? null : password);
        result.setRole(role);
        result.setStatus(status);
        result.setStaffId(staffId);
        dispose();
    }

    /** Trả về Account đã nhập, null nếu người dùng bấm Hủy. */
    public Account getResult() { return result; }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
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
}
