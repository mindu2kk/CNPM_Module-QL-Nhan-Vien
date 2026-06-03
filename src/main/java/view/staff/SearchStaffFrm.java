package view.staff;

import dao.StaffDAO;
import model.Account;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchStaffFrm extends JFrame implements ActionListener {

    private JTextField       txtKey;
    private JButton          btnSearch;
    private JTable           tblStaff;
    private DefaultTableModel model;
    private Account          user;
    private Staff[]          result;

    public SearchStaffFrm(Account u) {
        this.user = u;
        setTitle("Tìm kiếm nhân viên");
        setSize(700, 420);
        setLocationRelativeTo(null);

        JPanel pnTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnTop.add(new JLabel("Từ khóa:"));
        txtKey = new JTextField(20);
        pnTop.add(txtKey);
        btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(this);
        pnTop.add(btnSearch);

        model = new DefaultTableModel(
            new Object[]{"ID", "Fullname", "Role", "Tel", "Email"}, 0);
        tblStaff = new JTable(model);

        tblStaff.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblStaff.getSelectedRow();
                if (row >= 0 && result != null && row < result.length) {
                    new EditStaffFrm(user, result[row]);
                }
            }
        });

        add(pnTop, BorderLayout.NORTH);
        add(new JScrollPane(tblStaff), BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            StaffDAO dao = new StaffDAO();
            result = dao.searchStaff(txtKey.getText().trim());
            model.setRowCount(0);
            for (Staff s : result) {
                model.addRow(new Object[]{
                    s.getId(), s.getFullname(), s.getRole(),
                    s.getTel(), s.getEmail()
                });
            }
        }
    }
}
