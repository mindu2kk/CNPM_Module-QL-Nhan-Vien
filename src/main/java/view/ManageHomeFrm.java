package view;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ManageHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageStaff;   // Manager
    private JButton btnManageAccount; // Admin
    private JButton btnLogout;        // Tất cả
    private Account user;

    // Bảng tài khoản dùng nội bộ cho Admin (không cần file riêng)
    private JTable            tblAccount;
    private DefaultTableModel tblModel;
    private ArrayList<Account> accountList = new ArrayList<>();

    public ManageHomeFrm(Account u) {
        this.user = u;
        setTitle("Trang chủ - " + u.getUsername() + " (" + u.getRole() + ")");
        setSize(500, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        main.setBackground(new Color(245, 246, 250));

        // Manager → Quản lý nhân viên
        if (user.hasPermission("MANAGE_STAFF")) {
            btnManageStaff = new JButton("Quản lý nhân viên");
            style(btnManageStaff, new Color(160, 210, 240));
            btnManageStaff.addActionListener(this);
            main.add(btnManageStaff);
        }

        // Admin → Quản lý tài khoản (mở dialog tích hợp ngay trong frame này)
        if (user.hasPermission("MANAGE_ACCOUNT")) {
            btnManageAccount = new JButton("Quản lý tài khoản");
            style(btnManageAccount, new Color(210, 175, 230));
            btnManageAccount.addActionListener(this);
            main.add(btnManageAccount);
        }

        // Tất cả → Đăng xuất
        btnLogout = new JButton("Đăng xuất");
        style(btnLogout, new Color(240, 180, 170));
        btnLogout.addActionListener(this);
        main.add(btnLogout);

        // Employee: lời chào
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
            showManageAccountDialog();

        } else if (e.getSource() == btnLogout) {
            int ok = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrm().setVisible(true);
            }
        }
    }

    // ================================================================
    // Quản lý tài khoản – tích hợp trực tiếp, không cần file riêng
    // ================================================================
    private void showManageAccountDialog() {
        JDialog dlg = new JDialog(this, "Quản lý tài khoản", true);
        dlg.setSize(780, 460);
        dlg.setLocationRelativeTo(this);

        // ---- Toolbar ----
        JTextField txtKey = new JTextField(16);
        JButton btnSearch = btn("🔍 Tìm",  new Color(160, 210, 240));
        JButton btnAdd    = btn("➕ Thêm",  new Color(160, 220, 180));
        JButton btnEdit   = btn("✏ Sửa",   new Color(250, 200, 140));
        JButton btnDelete = btn("🗑 Xóa",  new Color(240, 160, 150));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(new Color(235, 225, 245));
        top.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        top.add(new JLabel("Username:")); top.add(txtKey);
        top.add(btnSearch);
        top.add(Box.createHorizontalStrut(15));
        top.add(btnAdd); top.add(btnEdit); top.add(btnDelete);

        // ---- Bảng ----
        tblModel = new DefaultTableModel(
            new Object[]{"ID", "Username", "Role", "Status", "Ngày tạo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAccount = new JTable(tblModel);
        tblAccount.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAccount.setRowHeight(24);
        tblAccount.getColumnModel().getColumn(0).setMaxWidth(60);
        tblAccount.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) doEditAccount(dlg);
            }
        });

        JPanel body = new JPanel(new BorderLayout(6, 6));
        body.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(tblAccount), BorderLayout.CENTER);
        dlg.add(body);

        // ---- Load dữ liệu ----
        loadAccounts("");

        // ---- Sự kiện ----
        btnSearch.addActionListener(ev -> loadAccounts(txtKey.getText().trim()));
        txtKey.addActionListener(ev  -> loadAccounts(txtKey.getText().trim()));
        btnAdd.addActionListener(ev    -> { doAddAccount(dlg);    loadAccounts(txtKey.getText().trim()); });
        btnEdit.addActionListener(ev   -> { doEditAccount(dlg);   loadAccounts(txtKey.getText().trim()); });
        btnDelete.addActionListener(ev -> { doDeleteAccount(dlg); loadAccounts(txtKey.getText().trim()); });

        dlg.setVisible(true);
    }

    private void loadAccounts(String key) {
        AccountDAO dao = new AccountDAO();
        accountList = key.isEmpty() ? dao.getAllAccounts() : dao.searchAccounts(key);
        tblModel.setRowCount(0);
        for (Account a : accountList) {
            tblModel.addRow(new Object[]{
                a.getIdAcc(), a.getUsername(), a.getIdRole(), a.getStatus(),
                a.getCreateDate() != null ? a.getCreateDate().toString().substring(0, 10) : ""
            });
        }
    }

    private void doAddAccount(JDialog parent) {
        String[] roles    = {"ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN"};
        String[] statuses = {"active", "inactive"};

        JTextField     fUser   = new JTextField();
        JPasswordField fPass   = new JPasswordField();
        JComboBox<String> fRole   = new JComboBox<>(roles);
        JComboBox<String> fStatus = new JComboBox<>(statuses);

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Username (*):")); p.add(fUser);
        p.add(new JLabel("Mật khẩu (*):")); p.add(fPass);
        p.add(new JLabel("Vai trò (*):")); p.add(fRole);
        p.add(new JLabel("Trạng thái:")); p.add(fStatus);

        if (JOptionPane.showConfirmDialog(parent, p, "Thêm tài khoản",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        String username = fUser.getText().trim();
        String password = new String(fPass.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Vui lòng nhập username và mật khẩu.");
            return;
        }
        Account a = new Account();
        a.setUsername(username);
        a.setPassword(password);
        a.setIdRole((String) fRole.getSelectedItem());
        a.setStatus((String) fStatus.getSelectedItem());

        if (new AccountDAO().addAccount(a))
            JOptionPane.showMessageDialog(parent, "Thêm tài khoản thành công!");
        else
            JOptionPane.showMessageDialog(parent, "Thêm thất bại! (Username đã tồn tại?)",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void doEditAccount(JDialog parent) {
        int row = tblAccount.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(parent, "Chọn tài khoản cần sửa."); return; }

        Account sel = accountList.get(row);
        if (sel.getIdAcc() == user.getIdAcc()) {
            JOptionPane.showMessageDialog(parent,
                "Không thể sửa tài khoản đang đăng nhập.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] roles    = {"ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN"};
        String[] statuses = {"active", "inactive"};

        JTextField     fUser   = new JTextField(sel.getUsername());
        JPasswordField fPass   = new JPasswordField();
        JComboBox<String> fRole   = new JComboBox<>(roles);
        JComboBox<String> fStatus = new JComboBox<>(statuses);
        fRole.setSelectedItem(sel.getIdRole());
        fStatus.setSelectedItem(sel.getStatus());

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Username:")); p.add(fUser);
        p.add(new JLabel("Mật khẩu mới (trống = giữ):")); p.add(fPass);
        p.add(new JLabel("Vai trò (phân quyền):")); p.add(fRole);
        p.add(new JLabel("Trạng thái:")); p.add(fStatus);

        if (JOptionPane.showConfirmDialog(parent, p, "Sửa: " + sel.getUsername(),
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        String pass = new String(fPass.getPassword()).trim();
        sel.setUsername(fUser.getText().trim());
        if (!pass.isEmpty()) sel.setPassword(pass);
        sel.setIdRole((String) fRole.getSelectedItem());
        sel.setStatus((String) fStatus.getSelectedItem());

        if (new AccountDAO().updateAccount(sel))
            JOptionPane.showMessageDialog(parent, "Cập nhật thành công!");
        else
            JOptionPane.showMessageDialog(parent, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void doDeleteAccount(JDialog parent) {
        int row = tblAccount.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(parent, "Chọn tài khoản cần xóa."); return; }

        Account sel = accountList.get(row);
        if (sel.getIdAcc() == user.getIdAcc()) {
            JOptionPane.showMessageDialog(parent,
                "Không thể xóa tài khoản đang đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(parent,
                "Xác nhận xóa: " + sel.getUsername() + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;

        if (new AccountDAO().deleteAccount(sel.getIdAcc()))
            JOptionPane.showMessageDialog(parent, "Xóa thành công!");
        else
            JOptionPane.showMessageDialog(parent, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // ---- Helpers ----
    private void style(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
