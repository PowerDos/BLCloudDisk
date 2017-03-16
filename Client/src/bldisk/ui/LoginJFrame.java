package bldisk.ui;

import bldisk.client.ClientToServer;
import com.sun.awt.AWTUtilities;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginJFrame extends JFrame{
    
    static Point mouseOrigin = new Point();  //用于保存鼠标的起源点，以实现窗体的移动
    
    private JTextField txtAccount;  //账号输入框
    private JPasswordField txtPassword; //密码输入框
    private JLabel labAccount;      //账号标签
    private JLabel labPassword;     //密码标签
    private JButton btnClose;       //关闭按钮
    private JLabel labBG;           //背景图片
    private JButton btnLogin;       //登陆按钮
    private JButton btnReg;         //注册按钮
    
    //初始化数据
    private ClientToServer cts;
    
    public LoginJFrame(){}
    public LoginJFrame(String title){
        super(title);
        init();
        
        cts = new ClientToServer();
        
    }
    
    private void init(){
        //初始化组件
        txtAccount = new JTextField("1001");
        txtPassword = new JPasswordField("123");
        labAccount = new JLabel("账号:");
        labPassword = new JLabel("密码:");
        btnClose = new JButton("");
        btnLogin = new JButton("");
        btnReg = new JButton("");
        
        this.setLayout(null);   //设置窗体布局为空

        //把关闭按钮添加到窗体上
        btnClose.setBounds(650,0,50,40);
        btnClose.setIcon(new ImageIcon("src/image/btnClose.png"));
        
        //让按钮透明化
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        this.add(btnClose);
        
        btnClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCloseActionPerformed(e);
            }
        });
        
        //把账号密码组件添加到窗体上
        labAccount.setBounds(267,220,60,30);
        labPassword.setBounds(267,270,60,30);
        txtAccount.setBounds(307,220,120,30);
        txtPassword.setBounds(307,270,120,30);
        //设置圆角
        txtAccount.setBorder(new CircleBorder(5));
        txtPassword.setBorder(new CircleBorder(5));
        //设置按钮的大小、位置
        btnLogin.setBounds(300,330,100,30);
        btnReg.setBounds(255,365,200,30);
        //设置按钮图标
        btnReg.setContentAreaFilled(false);
        btnLogin.setIcon(new ImageIcon("src/image/btnLogin.png"));
        btnReg.setIcon(new ImageIcon("src/image/btnRegister.png"));
        btnLogin.setBorder(new CircleBorder(5));
        btnReg.setBorder(new CircleBorder(5));
        this.add(btnLogin);
        this.add(btnReg);
        
        txtAccount.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnLoginActionPerformed(e);
            }
        });
        txtPassword.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnLoginActionPerformed(e);
            }
        });
        btnLogin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnLoginActionPerformed(e);
            }
        });
        btnReg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnRegActionPerformed(e);
            }
        });
        
        this.add(labAccount);
        this.add(labPassword);
        this.add(txtAccount);
        this.add(txtPassword);
        
        //设置窗体背景图片
        Icon BG = new ImageIcon("src/image/bgLogin.png"); 
        labBG = new JLabel(BG);
        labBG.setBounds(0,0,700,500);
        this.add(labBG);
        
        //添加鼠标监听器，以实现窗口的移动
        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                mouseOrigin.x = e.getX();
                mouseOrigin.y = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                Point p = getLocation();    //获取窗体的当前坐标
                setLocation(p.x + e.getX() - mouseOrigin.x,p.y + e.getY() - mouseOrigin.y);   //根据鼠标的移动更新坐标
            }
        });
        
        //设置窗体参数
        this.setSize(700,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        //将窗口圆角化
        AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(  
            0.0D, 0.0D, this.getWidth(), this.getHeight(), 10.0D,  
            10.0D));  
        this.setVisible(true);  
    }
    
    //登陆点击事件
    public void btnLoginActionPerformed(ActionEvent e){
        if (txtAccount.equals("")){
            JOptionPane.showMessageDialog(null,"请输入您的账号！","系统提示",JOptionPane.INFORMATION_MESSAGE);
        }else if (txtPassword.getText().equals("")){
            JOptionPane.showMessageDialog(null,"密码错误！请重新输入。","系统提示",JOptionPane.INFORMATION_MESSAGE);
        }else{
            
            int request = cts.Login(txtAccount.getText(),txtPassword.getText());
            if (request == 1){
                dispose();
                new MainJFrame("BLDisk",txtAccount.getText());
            }else if(request == 2){
                JOptionPane.showMessageDialog(null,"密码错误！请重新输入。","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }else if(request == 3){
                JOptionPane.showMessageDialog(null,"账号不存在，请检查输入。","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }else if(request == 4){
                JOptionPane.showMessageDialog(null,"连接服务器失败，请检查电脑是否已接通北京理工大学珠海学院内网。","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    }
    
    //注册点击事件
    public void btnRegActionPerformed(ActionEvent e){
        new RegisterJFrame(this,"注册",true);
    }
    
    //关闭点击事件
    public void btnCloseActionPerformed(ActionEvent e){
        System.exit(0);
    }
    
}
