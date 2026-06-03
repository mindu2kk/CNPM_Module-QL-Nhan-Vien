package view;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Admin: Quản lý tài khoản – Thêm, Sửa, Xóa, Phân quyền
 */
public class ManageAccountFrm extends JFrame implements ActionListener {

    private JTextField        txtKey;
    private JButton           btnSearch, btnAdd, btnEdit, btnDelete;
    private JTable            tblAccount;
    private DefaultTableModel tableModel;
    private Account           adminUser;
    private ArrayList<Account> result = new ArrayList<>();

    private static final String[] ROLES    = {"Employee", "Manager", "Admin"};
    private static final String[] STATUSES = {"active", "inactive"};

    public ManageAccountFrm(Account admin) {
        this.adminUser = admin;
        setTitle("Quản lý tài khoản");
        setSize(780, 440);
        setLocationRelativeTo(null);
        initComponents();
        loadAll();
    }

    private void initComponents() {
        // ---- Toolbar ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(new Color(235, 225, 245));
        top.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        top.add(new JLabel("Username / ID:"));
        txtKey = new JTextField(16);
        top.add(txtKey);

        btnSearch = btn("🔍 Tìm",   new Color(160, 210, 240));
        btnAdd    = btn("➕ Thêm",   new Color(160, 220, 180));
        btnEdit   = btn("✏ Sửa",    new Color(250, 200, 140));
        btnDelete = btn("🗑 Xóa",   new Color(240, 160, 150));

        top.add(btnSearch);
        top.add(Box.createHorizontalStrut(15));
        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDelete);

        txtKey.addActionListener(this);
        btnSearch.addActionListener(this);
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);

        // ---- Bảng ----
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Username", "Vai trò", "Trạng thái", "Ngày tạo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAccount = new JTable(tableModel);
        tblAccount.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAccount.setRowHeight(24);
        tblAccount.getColumnModel().getColumn(0).setMaxWidth(75);
        tblAccount.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) doEdit();
            }
        });

        JPanel main = new JPanel(new BorderLayout(6, 6));
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(tblAccount), BorderLayout.CENTER);
        add(main);
    }

    private void loadAll() {
        AccountDAO dao = new AccountDAO();
        result = dao.getAllAccounts();
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (Account a : result) {
            tableModel.addRow(new Object[]{
                a.getIdAcc(), a.getUsername(), a.getRole(), a.getStatus(),
                a.getCreateDate() != null ? a.getCreateDate().toString().substring(0, 10) : ""
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnSearch || src == txtKey) doSearch();
        else if (src == btnAdd)    doAdd();
        else if (src == btnEdit)   doEdit();
        else if (src == btnDelete) doDelete();
    }

    private void doSearch() {
        AccountDAO dao = new AccountDAO();
        result = dao.searchAccounts(txtKey.getText().trim());
        refresh();
    }

    private void doAdd() {
        JTextField fId   = new JTextField();
        JTextField fUser = new JTextField();
        JPasswordField fPass = new JPasswordField();
        JComboBox<String> fRole   = new JComboBox<>(ROLES);
        JComboBox<String> fStatus = new JComboBox<>(STATUSES);

        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("ID tài khoản (*):")); p.add(fId);
        p.add(new JLabel("Username (*):")); p.add(fUser);
        p.add(new JLabel("Mật khẩu (*):")); p.add(fPass);
        p.add(new JLabel("Vai trò (*):")); p.add(fRole);
        p.add(new JLabel("Trạng thái:")); p.add(fStatus);

        int ok = JOptionPane.showConfirmDialog(this, p,
            "Thêm tài khoản mới", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        String id       = fId.getText().trim();
        String username = fUser.getText().trim();
        String password = new String(fPass.getPassword()).trim();

        if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ ID, username và mật khẩu.");
            return;
        }

        Account a = new Account();
        a.setIdAcc(id);
        a.setUsername(username);
        a.setPassword(password);
        a.setRole((String) fRole.getSelectedItem());
        a.setStatus((String) fStatus.getSelectedItem());

        AccountDAO dao = new AccountDAO();
        if (dao.addAccount(a)) {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadAll();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!\n(ID hoặc username đã tồn tại.)",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doEdit() {
        int row = tblAccount.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn tài khoản cần sửa."); return; }

        Account selected = result.get(row);
        if (selected.getIdAcc().equals(adminUser.getIdAcc())) {
            JOptionPane.showMessageDialog(this, "Không thể sửa tài khoản đang đăng nhập.",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField fUser    = new JTextField(selected.getUsername());
        JPasswordField fPass = new JPasswordField();
        JComboBox<String> fRole   = new JComboBox<>(ROLES);
        JComboBox<String> fStatus = new JComboBox<>(STATUSES);
        fRole.setSelectedItem(selected.getRole());
        fStatus.setSelectedItem(selected.getStatus());

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Username:")); p.add(fUser);
        p.add(new JLabel("Mật khẩu mới (trống = giữ nguyên):")); p.add(fPass);
        p.add(new JLabel("Vai trò (phân quyền):")); p.add(fRole);
        p.add(new JLabel("Trạng thái:")); p.add(fStatus);

        int ok = JOptionPane.showConfirmDialog(this, p,
            "Sửa tài khoản: " + selected.getUsername(), JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        String pass = new String(fPass.getPassword()).trim();
        selected.setUsername(fUser.getText().trim());
        if (!pass.isEmpty()) selected.setPassword(pass);
        selected.setRole((String) fRole.getSelectedItem());
        selected.setStatus((String) fStatus.getSelectedItem());

        AccountDAO dao = new AccountDAO();
        if (dao.updateAccount(selected)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadAll();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDelete() {
        int row = tblAccount.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn tài khoản cần xóa."); return; }

        Account selected = result.get(row);
        if (selected.getIdAcc().equals(adminUser.getIdAcc())) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản đang đăng nhập!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa tài khoản: " + selected.getUsername() + "?",
            "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        AccountDAO dao = new AccountDAO();
        if (dao.deleteAccount(selected.getIdAcc())) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadAll();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
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
