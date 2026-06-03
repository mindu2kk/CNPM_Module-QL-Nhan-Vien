package view.login;

import dao.AccountDAO;
import model.Account;
import view.staff.ManageHomeFrm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrm extends JFrame implements ActionListener {

    private JTextField    txtUN;
    private JPasswordField txtPW;
    private JButton       btnLogin;

    public LoginFrm() {
        setTitle("Đăng nhập hệ thống Thư viện Quốc gia");
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pn = new JPanel(new GridLayout(3, 2, 8, 8));
        pn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pn.add(new JLabel("Tên đăng nhập:"));
        txtUN = new JTextField();
        pn.add(txtUN);

        pn.add(new JLabel("Mật khẩu:"));
        txtPW = new JPasswordField();
        pn.add(txtPW);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.addActionListener(this);
        pn.add(new JLabel());
        pn.add(btnLogin);

        add(pn);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            Account user = new Account();
            user.setUsername(txtUN.getText().trim());
            user.setPassword(new String(txtPW.getPassword()));

            AccountDAO dao = new AccountDAO();
            if (dao.checkLogin(user)) {
                new ManageHomeFrm(user);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrm();
    }
}
