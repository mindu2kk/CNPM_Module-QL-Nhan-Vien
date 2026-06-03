package view;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrm extends JFrame implements ActionListener {

    private JTextField     txtUN;
    private JTextField     txtPW;
    private JButton        btnLogin;

    public LoginFrm() {
        setTitle("Đăng nhập - Quản lý nhân viên");
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        main.setBackground(new Color(245, 246, 250));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(25, 75, 160));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 10));
        form.setBackground(new Color(245, 246, 250));

        form.add(new JLabel("Tên đăng nhập:"));
        txtUN = new JTextField();
        form.add(txtUN);

        form.add(new JLabel("Mật khẩu:"));
        txtPW = new JPasswordField();
        txtPW.addActionListener(this);
        form.add(txtPW);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(180, 200, 240));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this);
        form.add(new JLabel());
        form.add(btnLogin);

        main.add(lblTitle, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = txtUN.getText().trim();
        String password = new String(((JPasswordField) txtPW).getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account user = new Account();
        user.setUsername(username);
        user.setPassword(password);

        AccountDAO dao = new AccountDAO();
        if (dao.checkLogin(user)) {
            this.dispose();
            new ManageHomeFrm(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Sai tên đăng nhập hoặc mật khẩu!",
                "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            ((JPasswordField) txtPW).setText("");
            txtPW.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new LoginFrm().setVisible(true);
        });
    }
}
