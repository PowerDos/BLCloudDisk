package bldisk.ui;

import bldisk.client.ClientToServer;
import bldisk.entity.UserInfo;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RegisterJFrame extends JDialog{
    
    private JLabel labAccount;
    private JLabel labPassword;
    private JLabel labPasswordConfirm;
    private JTextField txtAccount;
    private JPasswordField pwd;
    private JPasswordField pwdConfirm;
    private JButton btnRegister;
    
    private ClientToServer cts;
    private UserInfo user;
    
    public RegisterJFrame(JFrame jf,String title,boolean model){
        super(jf,title,model);
        cts = new ClientToServer();
        user = new UserInfo();
        
        init();
    }
    
    void init(){
        
        labAccount = new JLabel("账号：");
        labPassword = new JLabel("密码：");
        labPasswordConfirm = new JLabel("确认密码：");
        txtAccount = new JTextField();
        pwd = new JPasswordField("");
        pwdConfirm = new JPasswordField("");
        btnRegister = new JButton("注册");
        
        this.setLayout(null);
        
        labAccount.setBounds(90,40,60,30);
        labPassword.setBounds(90,90,60,30);
        labPasswordConfirm.setBounds(90,140,80,30);
        txtAccount.setBounds(170,40,120,30);
        pwd.setBounds(170,90,120,30);
        pwdConfirm.setBounds(170,140,120,30);
        btnRegister.setBounds(155,200,100,30);
        
        txtAccount.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               btnRegisterActionPerformed(e);
           } 
        });
        pwd.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               btnRegisterActionPerformed(e);
           } 
        });
        pwdConfirm.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               btnRegisterActionPerformed(e);
           } 
        });
        btnRegister.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnRegisterActionPerformed(e);
            }
        });
        
        this.add(labPasswordConfirm);
        this.add(labAccount);
        this.add(labPassword);
        this.add(txtAccount);
        this.add(pwd);
        this.add(pwdConfirm);
        this.add(btnRegister);
        
        this.setSize(400,300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    public void btnRegisterActionPerformed(ActionEvent e){
//        if (!txtAccount.getText().equals("") && !){
//            user.setUserID(txtAccount.getText());
//            user
//        }
//        user.setUserPass("123");
//        cts.SignUp(user);
//        dispose();
        if (txtAccount.getText().equals("")){
            JOptionPane.showMessageDialog(null,"账号不能为空，请重新设置。","系统提示",JOptionPane.INFORMATION_MESSAGE);
        }else if (pwd.getText().equals("")){
            JOptionPane.showMessageDialog(null,"密码不能为空，请重新设置。","系统提示",JOptionPane.INFORMATION_MESSAGE);
        }else if (!pwdConfirm.getText().equals(pwd.getText())){
            JOptionPane.showMessageDialog(null,"两次密码不一致，请重新设置。","系统提示",JOptionPane.INFORMATION_MESSAGE);
        }else{
            user.setUserID(txtAccount.getText());
            user.setUserPass(pwd.getText());
            //设置为注册操作
            user.setType(2);
            cts.SignUp(user);
            JOptionPane.showMessageDialog(null,"恭喜您，注册成功！","系统提示",JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        
    }
    
}
