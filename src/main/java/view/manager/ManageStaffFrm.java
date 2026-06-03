package view.manager;

import dao.StaffDAO;
import model.Account;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ManageStaffFrm extends JFrame {

    private final Account user;
    private final StaffDAO staffDAO = new StaffDAO();

    private JTextField txtSearch;
    private JButton    btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;
    private JTable     tblStaff;
    private DefaultTableModel tableModel;
    private Staff[]    currentList = new Staff[0];

    public ManageStaffFrm(Account u) {
        this.user = u;
        initComponents();
        loadData("");
        setTitle("Quản lý nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(245, 246, 250));

        // ---- Thanh tìm kiếm & thao tác ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        top.setBackground(new Color(230, 235, 245));
        top.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        top.add(new JLabel("Tên / ID:"));
        txtSearch = new JTextField(18);
        top.add(txtSearch);

        btnSearch  = btn("🔍 Tìm",   new Color(160, 210, 240));
        btnRefresh = btn("↺ Tất cả", new Color(200, 200, 200));
        btnAdd     = btn("➕ Thêm",   new Color(160, 220, 180));
        btnEdit    = btn("✏ Sửa",    new Color(250, 200, 140));
        btnDelete  = btn("🗑 Xóa",   new Color(240, 160, 150));

        top.add(btnSearch);
        top.add(btnRefresh);
        top.add(Box.createHorizontalStrut(20));
        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDelete);

        // ---- Bảng danh sách ----
        String[] cols = {"ID", "Họ tên", "Chức vụ", "Điện thoại", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStaff = new JTable(tableModel);
        tblStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStaff.setRowHeight(24);
        tblStaff.getColumnModel().getColumn(0).setMaxWidth(60);
        tblStaff.addMouseListener(new MouseAdapter() {
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
        main.add(new JScrollPane(tblStaff), BorderLayout.CENTER);
        add(main);
    }

    // ----------------------------------------------------------------
    // Load dữ liệu lên bảng
    // ----------------------------------------------------------------
    private void loadData(String key) {
        currentList = key.isEmpty()
            ? staffDAO.getAllStaff().toArray(new Staff[0])
            : staffDAO.searchStaff(key);
        tableModel.setRowCount(0);
        for (Staff s : currentList) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getFullname(), s.getRole(), s.getTel(), s.getEmail()
            });
        }
    }

    // ----------------------------------------------------------------
    // Thêm nhân viên
    // ----------------------------------------------------------------
    private void doAdd() {
        StaffDialog dlg = new StaffDialog(this, "Thêm nhân viên mới", null);
        dlg.setVisible(true);
        Staff newStaff = dlg.getResult();
        if (newStaff != null) {
            int id = staffDAO.addStaff(newStaff);
            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! ID = " + id);
                loadData("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------------------
    // Sửa nhân viên
    // ----------------------------------------------------------------
    private void doEdit() {
        int row = tblStaff.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa.");
            return;
        }
        Staff selected = currentList[row];
        StaffDialog dlg = new StaffDialog(this, "Sửa thông tin nhân viên", selected);
        dlg.setVisible(true);
        Staff updated = dlg.getResult();
        if (updated != null) {
            updated.setId(selected.getId());
            if (staffDAO.updateStaff(updated)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------------------
    // Xóa nhân viên
    // ----------------------------------------------------------------
    private void doDelete() {
        int row = tblStaff.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa.");
            return;
        }
        Staff selected = currentList[row];
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa nhân viên: " + selected.getFullname() + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (staffDAO.deleteStaff(selected.getId())) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                loadData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Xóa thất bại!\n(Có thể nhân viên này đang liên kết với tài khoản.)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------------------
    // Helper – tạo nút bấm màu sắc
    // ----------------------------------------------------------------
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
