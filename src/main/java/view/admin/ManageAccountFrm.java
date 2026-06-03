package view.admin;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Màn hình Quản lý tài khoản dành cho Admin.
 * Chức năng: Thêm, Sửa (gồm phân quyền), Xóa, Tìm kiếm tài khoản.
 */
public class ManageAccountFrm extends JFrame {

    private final Account adminUser;
    private final AccountDAO accountDAO = new AccountDAO();

    private JTextField  txtSearch;
    private JButton     btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete;
    private JTable      tblAccount;
    private DefaultTableModel tableModel;
    private ArrayList<Account> currentList = new ArrayList<>();

    public ManageAccountFrm(Account admin) {
        this.adminUser = admin;
        initComponents();
        loadData("");
        setTitle("Quản lý tài khoản");
        setSize(820, 480);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(245, 246, 250));

        // ---- Toolbar ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        top.setBackground(new Color(235, 225, 245));
        top.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        top.add(new JLabel("Username / ID:"));
        txtSearch  = new JTextField(16);
        btnSearch  = btn("🔍 Tìm",   new Color(160, 210, 240));
        btnRefresh = btn("↺ Tất cả", new Color(200, 200, 200));
        btnAdd     = btn("➕ Thêm",   new Color(160, 220, 180));
        btnEdit    = btn("✏ Sửa",    new Color(250, 200, 140));
        btnDelete  = btn("🗑 Xóa",   new Color(240, 160, 150));

        top.add(txtSearch);
        top.add(btnSearch);
        top.add(btnRefresh);
        top.add(Box.createHorizontalStrut(20));
        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDelete);

        // ---- Bảng ----
        String[] cols = {"ID", "Username", "Vai trò", "Trạng thái", "Staff ID", "Ngày tạo"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAccount = new JTable(tableModel);
        tblAccount.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAccount.setRowHeight(24);
        tblAccount.getColumnModel().getColumn(0).setMaxWidth(80);
        tblAccount.getColumnModel().getColumn(4).setMaxWidth(70);
        tblAccount.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) doEdit();
            }
        });

        // ---- Sự kiện ----
        btnSearch.addActionListener(e  -> loadData(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadData(""); });
        btnAdd.addActionListener(e     -> doAdd());
        btnEdit.addActionListener(e    -> doEdit());
        btnDelete.addActionListener(e  -> doDelete());
        txtSearch.addActionListener(e  -> loadData(txtSearch.getText().trim()));

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(tblAccount), BorderLayout.CENTER);
        add(main);
    }

    // ----------------------------------------------------------------
    // Load dữ liệu lên bảng
    // ----------------------------------------------------------------
    private void loadData(String key) {
        currentList = key.isEmpty()
            ? accountDAO.getAllAccounts()
            : accountDAO.searchAccounts(key);
        tableModel.setRowCount(0);
        for (Account a : currentList) {
            tableModel.addRow(new Object[]{
                a.getIdAcc(),
                a.getUsername(),
                a.getRole(),
                a.getStatus(),
                a.getStaffId() != null ? a.getStaffId() : "",
                a.getCreateDate() != null ? a.getCreateDate().toString() : ""
            });
        }
    }

    // ----------------------------------------------------------------
    // Thêm tài khoản
    // ----------------------------------------------------------------
    private void doAdd() {
        AccountDialog dlg = new AccountDialog(this, "Thêm tài khoản mới", null, accountDAO);
        dlg.setVisible(true);
        Account newAcc = dlg.getResult();
        if (newAcc != null) {
            newAcc.setIdAcc(accountDAO.generateNextId());
            if (accountDAO.addAccount(newAcc)) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                loadData("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!\n(Username có thể đã tồn tại.)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------------------
    // Sửa tài khoản (bao gồm đổi role – phân quyền)
    // ----------------------------------------------------------------
    private void doEdit() {
        int row = tblAccount.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa.");
            return;
        }
        Account selected = currentList.get(row);

        // Không cho Admin tự xóa quyền của chính mình
        if (selected.getIdAcc().equals(adminUser.getIdAcc())) {
            JOptionPane.showMessageDialog(this,
                "Không thể sửa tài khoản đang đăng nhập.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AccountDialog dlg = new AccountDialog(this, "Sửa tài khoản", selected, accountDAO);
        dlg.setVisible(true);
        Account updated = dlg.getResult();
        if (updated != null) {
            updated.setIdAcc(selected.getIdAcc());
            if (accountDAO.updateAccount(updated)) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!");
                loadData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------------------
    // Xóa tài khoản
    // ----------------------------------------------------------------
    private void doDelete() {
        int row = tblAccount.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa.");
            return;
        }
        Account selected = currentList.get(row);

        if (selected.getIdAcc().equals(adminUser.getIdAcc())) {
            JOptionPane.showMessageDialog(this,
                "Không thể xóa tài khoản đang đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa tài khoản: " + selected.getUsername() + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (accountDAO.deleteAccount(selected.getIdAcc())) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
