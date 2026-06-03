package view.user;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrm extends JFrame implements ActionListener {

    private JTextField txtUN;
    private JPasswordField txtPW;
    private JButton btnLogin;

    public LoginFrm() {
        initComponents();
        setTitle("Đăng nhập - Quản lý nhân viên");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 80, 160));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setBackground(new Color(245, 245, 250));

        JLabel lblUN = new JLabel("Tên đăng nhập:");
        lblUN.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUN = new JTextField();
        txtUN.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblPW = new JLabel("Mật khẩu:");
        lblPW.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPW = new JPasswordField();
        txtPW.setFont(new Font("Arial", Font.PLAIN, 13));

        formPanel.add(lblUN);
        formPanel.add(txtUN);
        formPanel.add(lblPW);
        formPanel.add(txtPW);

        // Panel nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(new Color(245, 245, 250));

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(new Color(30, 80, 160));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(130, 35));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this);

        // Cho phép nhấn Enter để đăng nhập
        txtPW.addActionListener(this);

        btnPanel.add(btnLogin);

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = txtUN.getText().trim();
        String password = new String(txtPW.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account user = new Account();
        user.setUsername(username);
        user.setPassword(password);

        AccountDAO accountDAO = new AccountDAO();
        if (accountDAO.checkLogin(user)) {
            JOptionPane.showMessageDialog(this,
                "Đăng nhập thành công! Chào mừng " + username,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new ManageHomeFrm(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Tên đăng nhập hoặc mật khẩu không đúng!",
                "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            txtPW.setText("");
            txtPW.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginFrm().setVisible(true);
        });
    }
}
