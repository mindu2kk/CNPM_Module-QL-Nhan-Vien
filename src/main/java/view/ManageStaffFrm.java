package view;

import dao.StaffDAO;
import model.Account;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Manager: Quản lý nhân viên – Thêm, Sửa, Xóa, Tìm kiếm
 */
public class ManageStaffFrm extends JFrame implements ActionListener {

    private JTextField        txtKey;
    private JButton           btnSearch, btnAdd, btnEdit, btnDelete;
    private JTable            tblStaff;
    private DefaultTableModel tableModel;
    private Account           user;
    private Staff[]           result = new Staff[0];

    public ManageStaffFrm(Account u) {
        this.user = u;
        setTitle("Quản lý nhân viên");
        setSize(750, 460);
        setLocationRelativeTo(null);
        initComponents();
        loadAll();
    }

    private void initComponents() {
        // ---- Toolbar ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(new Color(230, 235, 245));
        top.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        top.add(new JLabel("Tên / ID:"));
        txtKey = new JTextField(18);
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
            new Object[]{"ID", "Họ tên", "Chức vụ", "Điện thoại", "Email"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStaff = new JTable(tableModel);
        tblStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStaff.setRowHeight(24);
        tblStaff.getColumnModel().getColumn(0).setMaxWidth(55);
        tblStaff.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) doEdit();
            }
        });

        JPanel main = new JPanel(new BorderLayout(6, 6));
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(tblStaff), BorderLayout.CENTER);
        add(main);
    }

    private void loadAll() {
        StaffDAO dao = new StaffDAO();
        result = dao.searchStaff("");
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (Staff s : result) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getFullname(), s.getRole(), s.getTel(), s.getEmail()
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
        StaffDAO dao = new StaffDAO();
        result = dao.searchStaff(txtKey.getText().trim());
        refresh();
    }

    private void doAdd() {
        // Dialog nhập thông tin nhân viên mới
        JTextField fName  = new JTextField();
        JTextField fRole  = new JTextField();
        JTextField fTel   = new JTextField();
        JTextField fEmail = new JTextField();

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Họ tên (*):"));  p.add(fName);
        p.add(new JLabel("Chức vụ (*):"));  p.add(fRole);
        p.add(new JLabel("Điện thoại:")); p.add(fTel);
        p.add(new JLabel("Email:"));      p.add(fEmail);

        int ok = JOptionPane.showConfirmDialog(this, p,
            "Thêm nhân viên mới", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        String name = fName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên.");
            return;
        }

        Staff s = new Staff(0, name, fRole.getText().trim(),
                            fTel.getText().trim(), fEmail.getText().trim());
        StaffDAO dao = new StaffDAO();
        if (dao.addStaff(s) > -1) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadAll();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doEdit() {
        int row = tblStaff.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn nhân viên cần sửa."); return; }
        new EditStaffFrm(user, result[row]).setVisible(true);
        // Làm mới sau khi sửa
        doSearch();
    }

    private void doDelete() {
        int row = tblStaff.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn nhân viên cần xóa."); return; }

        Staff s = result[row];
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa nhân viên: " + s.getFullname() + "?",
            "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        StaffDAO dao = new StaffDAO();
        if (dao.deleteStaff(s.getId())) {
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
