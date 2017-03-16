package bldisk.ui;

import bldisk.client.*;
import bldisk.entity.*;
import bldisk.entity.Friend;
import com.sun.awt.AWTUtilities;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainJFrame extends JFrame{
        private JPanel panDecorate;     //最小化、关闭界面按钮面板
        private JPanel panTitle;	//上部分头像、功能选择栏面板
	private JPanel panInfor;	//用户信息面板
	private JPanel panHead;		//用户头像面板
	private JPanel panTags;		//功能选择面板
	private JPanel panCard;		//卡片式面板，用于存放四个不同的面板
	private JPanel panDisk;		//网盘功能面板
	private JPanel panShare;	//分享功能面板
	private JPanel panSecret;	//隐私空间功能面板
	private JPanel panMy;         //设置功能面板
        private JLabel labHead;         //头像标签
        private JLabel labLogo;         //程序logo
	private JLabel labName;		//用户名称标签
	private JProgressBar pgbMemory;		//用户内存状态标签
        private JButton btnMinimal;	//最小化界面按钮
	private JButton btnClose;	//关闭界面按钮
	private JButton btnDiskTag;     //切换到网盘面板的按钮
	private JButton btnShareTag;    //切换到分享面板的按钮
	private JButton btnSecretTag;   //切换到隐私空间面板的按钮
	private JButton btnMyTag;       //切换到用户个人面板的按钮
        
        static Point mouseOrigin = new Point();  //用于保存鼠标的起源点，以实现窗体的移动

        private CardLayout card;        //选择功能界面时需要卡片布局全局变量，方便点击事件调用
        
        //定义Disk面板的组件
        private JPanel panFileOpera;    //文件操作面板
        private JPanel panFileGuide;    //目录指导面板
        private JButton btnDownload;    //下载按钮
        private JButton btnUpload;      //上传按钮
        private JButton btnShare;       //分享按钮
        private JButton btnDelete;      //删除按钮
        private JButton btnMkdir;       //新建文件夹按钮
        private JButton btnReturnDirectory;   //返回上级目录按钮
        private JButton btnRefreshDirectory;    //刷新目录按钮
        private JTextField txtDirectory;    //当前目录提示
        private JTextField txtSearchFile;   //查找文件栏
        private JButton btnSearchFile;  //查找文件按钮
        private JList fileList;         //文件列表
        private FileListModel flm;      //文件列表模式
        private FileListCell flc;       //文件列表单元格渲染器
        private JList friendList;       //好友列表
        private FriendListModel frilm;  //好友列表模式
        private FriendListCell frilc;   //好友列表单元格渲染器
        private JPopupMenu popFile;     //文件列表右键选项
        private JMenuItem itemFileRename;     //重命名文件选项
        
        //定义Share面板的组件
        private JPanel panFriend;       //好友面板
        private JTextField txtSearchFriend;     //查找好友文本框
        private JButton btnSearchFriend;        //查找好友按钮
        private JButton btnAddFriend;   //添加好友按钮
        private JTextPane panChat;      //聊天内容面板
        private JTextField panSend;     //发送的文本框
        private JButton btnSend;        //发送按钮
        private JPopupMenu popFriend;         //好友列表右键选项
        private JMenuItem itemDeleteFriend;     //删除好友选项
        
        //定义Secret面板的组件
        private JPasswordField pwdSecret;   //隐私空间密码框
        private JPasswordField pwdConfirm;  //确认密码框
        private JButton btnEnterSecret;     //进入隐私空间的按钮
        private JPanel panWithoutSecret;    //没有设置密码的隐私空间面板
        private JPanel panCheck;            //请求用户输入密码的隐私空间面板
        private JPanel panSecretFile;       //隐私文件的隐私空间面板
        private JLabel labTips;             //提示文字标签
        private JLabel labSecretPwd;        //密码提示标签
        private JLabel labSecretPwdConfirm; //确认密码提示标签
        private boolean hasSecretFile;      //判断用户是否有设置隐私空间密码
        
        //定义My面板的组件
        private JButton btnMyDown;          //进入我的下载面板的按钮
        private JButton btnHistory;         //进入历史记录面板的按钮
        private JPanel panMyChoose;         //选择功能面板
        private JPanel panHistory;          //历史记录面板
        private JPanel panMyDown;           //我的下载面板
        private DefaultTableModel dtm;      //表格模式
        private JTable tbl;                 //表格
        private JTableHeader th;            //表头
        private Vector headData;            //表头显示内容
        private Vector tableData;           //表格内容
        
        //定义面板显示的数据
        private UserInfo user;          //用户属性，用于获取用户的数据，如账号，所剩内存，最大内存等等。
        private FileInfo fileInfo;      //文件属性，用于上传下载是获取文件属性
        private FileList[] fileData;    //文件列表的数据，用于显示到面板的列表中
        private Friend[] friendData;    //好友列表的数据，用于显示到面板的列表中
        private Friend friend;          //用户的好友数据，用户获取好友列表的数据
        private ClientToServer cts;             //用于实现客户端连接到服务端
        private int oldFriendIndex = 0;
        private int newFriendIndex = 0;
	
	public MainJFrame(){}
	
	public MainJFrame(String title,String userID){
		super(title);
                card = new CardLayout();        //在构造方法初始化此布局，点击事件方法才能正常访问此属性
                
                //先实例化数据，以在组件加载时能传入数据
                //实例化用户
                user = new UserInfo();
                user.setUserID(userID);
                hasSecretFile = false;
                if (user.getPrivatePass() == null){
                    hasSecretFile = false;
                }else{
                    hasSecretFile = true;
                }
                cts = new ClientToServer();     //实例化客户端连接到服务端
                
                //获取用户的文件目录
                fileInfo = new FileInfo();
                fileInfo.setFilePath("/");
                fileInfo.setFileRequest(3);     //设置为3表示获取文件目录
                fileInfo.setRuturnInfo(0);
                fileInfo.setUserID(userID);
                fileData = cts.GetFileList(fileInfo);

                //获取用户的好友数据
                friend = new Friend();
                friend.setUserID(userID);
                friendData = cts.GetFriendList(userID);
                
		init();
                labName.setText(userID);
	}
	
	void init(){
		//初始化程序大致面板组件
                panDecorate = new JPanel();
		panTitle = new JPanel();
		panInfor = new JPanel();
		panTags = new JPanel();
		panCard = new JPanel();
		panDisk = new JPanel();
		panShare = new JPanel();
		panMy = new JPanel();
		panSecret = new JPanel();
		
		//初始化用户信息相关组件
                labLogo = new JLabel("BLDisk");
		panHead = new JPanel();
                Icon headImg = new ImageIcon("src/image/head.png");
                labHead = new JLabel(headImg);
		labName = new JLabel("wjh");
		pgbMemory = new JProgressBar();
		pgbMemory.setMaximum(1000);
		pgbMemory.setMinimum(0);
		pgbMemory.setValue(200);
		
		//初始化功能选择按钮
		btnDiskTag = new JButton();
		btnShareTag = new JButton();
		btnSecretTag = new JButton();
		btnMyTag = new JButton();
                
                //初始化最小化、关闭界面按钮
		btnMinimal = new JButton("-");
		btnClose = new JButton("X");
                
                //初始化Disk面板的组件
                panFileOpera = new JPanel();
                panFileGuide = new JPanel();
                btnDownload = new JButton("下载");
                btnUpload = new JButton("上传");
                btnShare = new JButton("分享");
                btnDelete = new JButton("删除");
                btnMkdir = new JButton("新建文件夹");
                btnReturnDirectory = new JButton("");
                btnRefreshDirectory = new JButton("");
                txtDirectory = new JTextField("   我的网盘");
                txtSearchFile = new JTextField("搜索我的网盘文件");
                btnSearchFile = new JButton("");
                popFile = new JPopupMenu();
                itemFileRename = new JMenuItem("重命名");
                popFile.add(itemFileRename);
		
                //初始化Share面板的组件
                panFriend = new JPanel();
                txtSearchFriend = new JTextField("查找好友");
                btnSearchFriend = new JButton();
                btnAddFriend = new JButton();
                panChat = new JTextPane();
                panSend = new JTextField();
                btnSend = new JButton("发送");
                popFriend = new JPopupMenu();
                itemDeleteFriend = new JMenuItem("删除");
                popFriend.add(itemDeleteFriend);
                
                //初始化Secret面板的组件
                labTips = new JLabel();
                labSecretPwd = new JLabel("密码");
                labSecretPwdConfirm = new JLabel("确认密码");
                pwdSecret = new JPasswordField();
                pwdConfirm = new JPasswordField();
                btnEnterSecret = new JButton("进入隐私空间");
                panCheck = new JPanel();
                panSecretFile = new JPanel();
                panWithoutSecret = new JPanel();
                
                //初始化My面板的组件
                btnMyDown = new JButton("传输列表");
                btnHistory = new JButton("历史记录");
                panMyDown = new JPanel();
                panHistory = new JPanel();
                panMyChoose = new JPanel();
                dtm = new DefaultTableModel();
                tbl = new JTable(dtm);
                th = tbl.getTableHeader();
                headData = new Vector();
                tableData = new Vector();
                
		//设置各个Panel的布局方式
                this.setLayout(null);
		panTitle.setLayout(null);
		panInfor.setLayout(null);
                panHead.setLayout(null);
		panTags.setLayout(new GridLayout(1,4));
                panDecorate.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
		panCard.setLayout(card);
		panDisk.setLayout(null);
		panShare.setLayout(null);
                panSecret.setLayout(card);
		panMy.setLayout(card);
                panFriend.setLayout(null);
                panFileOpera.setLayout(new FlowLayout(FlowLayout.LEFT,8,8));
                panFileGuide.setLayout(null);
                panWithoutSecret.setLayout(null);
                panCheck.setLayout(null);
                panSecretFile.setLayout(null);
                panMyDown.setLayout(new GridLayout(200,3));
                panHistory.setLayout(null);
                panMyChoose.setLayout(null);
		
		//将title面板添加到窗体中
		panTitle.setBounds(0,0,900,108);
		this.add(panTitle);
		
		//将用户信息面板添加到title面板上
		panInfor.setBounds(0,0,280,108);
		panTitle.add(panInfor);
		
		//将用户的各种信息组件添加到信息面板上
		panHead.setBackground(Color.WHITE);
		panHead.setBounds(20,30,65,65);
                labHead.setBounds(0,0,65,65);
                labLogo.setBounds(15,10,65,10);
		labName.setBounds(95,30,100,35);
		pgbMemory.setBounds(95,65,120,10);
                panHead.add(labHead);
                panInfor.add(labLogo);
		panInfor.add(panHead);
		panInfor.add(labName);
		panInfor.add(pgbMemory);
		
		//将title面板上的所有组件背景颜色设置为蓝色
                panTitle.setBackground(new Color(57,176,244));
                panInfor.setBackground(new Color(57,176,244));
                panDecorate.setBackground(new Color(57,176,244));
                btnDiskTag.setBackground(new Color(57,176,244));
                btnShareTag.setBackground(new Color(57,176,244));
                btnSecretTag.setBackground(new Color(57,176,244));
                btnMyTag.setBackground(new Color(57,176,244));

                //将最小化、关闭界面的按钮添加到Decorate面板上
                btnClose.setContentAreaFilled(false);
                btnMinimal.setContentAreaFilled(false);
                btnClose.setForeground(Color.WHITE);
                btnMinimal.setForeground(Color.WHITE);
		panDecorate.add(btnMinimal);
		panDecorate.add(btnClose);
                btnMinimal.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnMinimalActionPerformed(e);
                    }
                });
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				btnCloseActionPerformed(e);
			}
		});
		
		//将Decorate面板添加到title面板上
		panDecorate.setBounds(650,0,250,30);
		panTitle.add(panDecorate);
		
		//将功能选择面板添加到title面板上
		panTags.setBounds(280,0,370,108);
		panTitle.add(panTags);
		
		//设置四个切换面板按钮图标，并将四个切换面板按钮添加到功能选择面板上
		Icon bgDisk = new ImageIcon("src/image/btnDisk.png");
		Icon bgShare = new ImageIcon("src/image/btnShare.png");
		Icon bgSecret = new ImageIcon("src/image/btnSecret.png");
		Icon bgMy = new ImageIcon("src/image/btnMy.png");
		btnDiskTag.setIcon(bgDisk);
		btnShareTag.setIcon(bgShare);
		btnSecretTag.setIcon(bgSecret);
		btnMyTag.setIcon(bgMy);
		panTags.add(btnDiskTag);
		panTags.add(btnShareTag);
		panTags.add(btnSecretTag);
		panTags.add(btnMyTag);
                
                //添加四个切换面板的按钮事件
                btnDiskTag.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panCard,"disk");
                    }
                });
                btnShareTag.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panCard,"share");
                    }
                });
                btnSecretTag.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panCard,"secret");
                    }
                });
                btnMyTag.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panCard,"my");
                    }
                });
		
		//将存放功能面板的卡片面板放到窗体中
		panCard.setBounds(0,108,900,542);
		this.add(panCard);
                
		//把四个功能面板添加到卡片面板当中
		panCard.add("disk",panDisk);
		panCard.add("share",panShare);
		panCard.add("secret",panSecret);
		panCard.add("my",panMy);

                //添加组件到Disk面板当中
                panDisk.setBackground(Color.white);
                panFileOpera.setBackground(new Color(223,245,245));
                btnReturnDirectory.setBackground(Color.WHITE);
                btnRefreshDirectory.setBackground(Color.WHITE);
                txtDirectory.setBackground(Color.WHITE);
                btnSearchFile.setBackground(Color.WHITE);
                panFileOpera.setBounds(0,0,900,40);
                btnDownload.setBounds(0,0,100,40);
                panFileGuide.setBounds(0,40,900,40);
                btnReturnDirectory.setBounds(0,0,50,40);
                btnRefreshDirectory.setBounds(50,0,50,40);
                txtDirectory.setBounds(100,0,540,40);
                txtSearchFile.setBounds(640,0,200,40);
                btnSearchFile.setBounds(840,0,60,40);
                btnReturnDirectory.setIcon(new ImageIcon("src/image/btnReturnDirectory.png"));
                btnRefreshDirectory.setIcon(new ImageIcon("src/image/btnRefreshDirectory.png"));
                btnSearchFile.setIcon(new ImageIcon("src/image/btnSearchFile.png"));
                txtDirectory.setEditable(false);
                panFileOpera.add(btnUpload);
                panFileOpera.add(btnDownload);
                panFileOpera.add(btnShare);
                panFileOpera.add(btnDelete);
                panFileOpera.add(btnMkdir);
                panFileGuide.add(btnReturnDirectory);
                panFileGuide.add(btnRefreshDirectory);
                panFileGuide.add(txtDirectory);
                panFileGuide.add(txtSearchFile);
                panFileGuide.add(btnSearchFile);
                panDisk.add(panFileOpera);
                panDisk.add(panFileGuide);
                
                //添加文件列表到Disk面板当中
                flm = new FileListModel(fileData);
                flc = new FileListCell(fileData);
                fileList = new JList(flm);
                fileList.setCellRenderer(flc);
                JScrollPane scroolPane = new JScrollPane(fileList);
                scroolPane.setBounds(0,80,900,462);
                panDisk.add(scroolPane);
                
                fileList.addMouseListener(new MouseAdapter(){
                     public void mouseClicked(MouseEvent e){
                        listDoubleClicked(e);
                    }
                });
                
                //添加Disk面板各个按钮的点击事件
                btnUpload.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnUploadActionPerformed(e);
                    }
                });
                btnDownload.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnDownloadActionPerformed(e);
                    }
                });
                btnShare.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnShareActionPerformed(e);
                    }
                });
                btnDelete.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnDeleteActionPerformed(e);
                    }
                });
                btnMkdir.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnMkdirActionPerformed(e);
                    }
                });
                txtSearchFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSearchFileActionPerformed(e);
                    }
                });
                txtSearchFile.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent  e){
                        txtSearchFile.setText("");
                    }
                });
                btnReturnDirectory.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnReturnDirectoryActionPerformed(e);
                    }
                });
                btnRefreshDirectory.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnRefreshDirectoryActionPerformed(e);
                    }
                });
                btnSearchFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSearchFileActionPerformed(e);
                    }
                });
                fileList.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if (fileList.getSelectedIndex() >= 0 && e.getButton() == MouseEvent.BUTTON3)
                            popFile.show(fileList,e.getX(),e.getY());
                    }
                });
                itemFileRename.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        itemFileRenameActionPerformed(e);
                    }
                });
                
                //添加组件到share面板当中
                txtSearchFriend.setBounds(0,0,120,30);
                btnSearchFriend.setBounds(120,0,40,30);
                btnAddFriend.setBounds(160,0,38,30);
                panFriend.setBounds(0,30,200,512);
                panSend.setBounds(200,500,630,42);
                btnSend.setBounds(830,500,70,42);
                panChat.setEditable(false);
                panShare.add(txtSearchFriend);
                panShare.add(btnSearchFriend);
                panShare.add(btnAddFriend);
                panShare.add(panFriend);
                panShare.add(panSend);
                panShare.add(btnSend);
                btnAddFriend.setIcon(new ImageIcon("src/image/btnAddFriend.png"));
                btnSearchFriend.setIcon(new ImageIcon("src/image/btnSearchFriend.png"));
                //添加好友列表到share面板当中
                frilm = new FriendListModel(friendData);
                frilc = new FriendListCell(friendData);
                friendList = new JList(frilm);
                friendList.setCellRenderer(frilc);
                //添加好友列表滚动条
                JScrollPane friScr = new JScrollPane(friendList);
                friScr.setBounds(0,0,200,512);
                panFriend.add(friScr);
                //添加聊天内容滚动条
                JScrollPane chatScr = new JScrollPane(panChat);
                chatScr.setBounds(200,0,700,500);
                panShare.add(chatScr);
                
                txtSearchFriend.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        txtSearchFriend.setText("");
                    }
                });
                txtSearchFriend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSearchFriendActionPerformed(e);
                    }
                });
                btnSearchFriend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSearchFriendActionPerformed(e);
                    }
                });
                btnAddFriend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnAddFriendActionPerformed(e);
                    }
                });
                friendList.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if (friendList.getSelectedIndex() >= 0 && e.getButton() == MouseEvent.BUTTON3)
                            popFriend.show(friendList,e.getX(),e.getY());
                    }
                });
                itemDeleteFriend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        itemDeleteFriendActionPerformed(e);
                    }
                });
                friendList.addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent e) {
                        if (friendList.getValueIsAdjusting()){
                            return;
                        }
                        refreshChatPane();
                    }
                });
                panSend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSendActionPerformed(e);
                    }
                });
                btnSend.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnSendActionPerformed(e);
                    }
                });
                
                //添加组件到Secret面板当中
                panSecret.add("withoutSecret",panWithoutSecret);
                panSecret.add("check",panCheck);
                panSecret.add("secretFile",panSecretFile);
                
                //判断用户是否有隐私文件
                if (!hasSecretFile){
                    labTips.setBounds(380,150,400,30);
                    labSecretPwd.setBounds(339,210,75,30);
                    pwdSecret.setBounds(405,210,150,30);
                    btnEnterSecret.setBounds(410,280,80,30);
                    labTips.setFont(new Font("黑体",Font.BOLD,18));
                    labTips.setText("请输入您的密码。");
                    panCheck.add(labTips);
                    panCheck.add(labSecretPwd);
                    panCheck.add(pwdSecret);
                    panCheck.add(btnEnterSecret);
                    card.show(panSecret,"check");
                }else{
                    labTips.setBounds(280,120,400,30);
                    labSecretPwd.setBounds(339,180,75,30);
                    labSecretPwdConfirm.setBounds(330,230,75,30);
                    pwdSecret.setBounds(405,180,150,30);
                    pwdConfirm.setBounds(405,230,150,30);
                    btnEnterSecret.setBounds(410,280,80,30);
                    labTips.setFont(new Font("黑体",Font.BOLD,18));
                    labTips.setText("您还没有使用过隐私空间，请设置您的密码。");
                    panWithoutSecret.add(labTips);
                    panWithoutSecret.add(labSecretPwd);
                    panWithoutSecret.add(labSecretPwdConfirm);
                    panWithoutSecret.add(pwdSecret);
                    panWithoutSecret.add(pwdConfirm);
                    panWithoutSecret.add(btnEnterSecret);
                    card.show(panSecret,"withoutSecret");
                }
                
                //添加进入隐私文件按钮事件
                btnEnterSecret.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        btnEnterSecretActionPerformed(e);
                    }
                });
                
                //添加组件到隐私空间文件面板上
                
                //添加组件到My面板当中
                panMyChoose.setBackground(Color.WHITE);
                btnMyDown.setBounds(150,100,200,200);
                btnHistory.setBounds(550,100,200,200);
                th.setBounds(new Rectangle(0,0,900,30));
                tbl.setBounds(new Rectangle(0,30,900,512));
                
                headData.add("文件名称");
                headData.add("文件大小");
                headData.add("修改时间");
                headData.add("传输状态");
                dtm.setDataVector(tableData,headData);
                panHistory.add(th);
                panHistory.add(tbl);
                panMyChoose.add(btnMyDown);
                panMyChoose.add(btnHistory);
                panMy.add("myChoose",panMyChoose);
                panMy.add("history",panHistory);
                panMy.add("myDown",panMyDown);
                
                //添加历史记录和当前下载选择功能的按钮事件
                btnMyDown.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panMy,"myDown");
                    }
                });
                btnHistory.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        card.show(panMy,"history");
                    }
                });
                
                //设置窗体风格为系统风格
                try{
                    UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                }catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e){
                    e.printStackTrace();
                }
                
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
                
                //设置窗体的界面大小位置
		this.setSize(900,650);
		this.setLocationRelativeTo(null);
                this.setUndecorated(true);
                //设置窗体圆角
                AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(
                0.0D, 0.0D, this.getWidth(), this.getHeight(), 5.0D,  
                5.0D));
		this.setVisible(true);
	}

        //最小化按钮事件
        public void btnMinimalActionPerformed(ActionEvent e){
            setExtendedState(JFrame.ICONIFIED);
        }
        //关闭按钮事件
        public void btnCloseActionPerformed(ActionEvent e){
            System.exit(0);
        }
        
        //文件列表双击事件
        public void listDoubleClicked(MouseEvent e){
            if (e.getClickCount() == 2){
                FileList getFileList = fileData[fileList.getSelectedIndex()];
                if (getFileList != null){
                    if (getFileList.isIsDirectory()){
                        fileInfo.setFilePath(fileInfo.getFilePath() + getFileList.getFileName() + "/");
                        fileInfo.setFileRequest(3);     //设置为3表示获取文件目录
                        fileInfo.setRuturnInfo(0);
                        fileData = cts.GetFileList(fileInfo);
                        refreshFileData();  //刷新列表数据
                        if (fileInfo.getFilePath().equals("/"))
                            txtDirectory.setText((txtDirectory.getText() + fileInfo.getFilePath()).replaceAll("/","  ▶ "));
                        txtDirectory.setText(("   我的网盘" + fileInfo.getFilePath()).replaceAll("/","  ▶ "));
                    }
                }
            }
        }

        //上传按钮事件
        public void btnUploadActionPerformed(ActionEvent e){
            if (fileList.getSelectedIndex() >= fileData.length - 1)
                return;
            FileDialog fdUpload = new FileDialog(this,"上传到云盘",FileDialog.LOAD);
            fdUpload.setVisible(true);
            
            String filePath = fdUpload.getDirectory() + fdUpload.getFile();
            if (filePath.equals("nullnull"))
                return;
            
            new Thread(new Runnable(){
                public void run(){
                    cts.Upload(user.getUserID(),filePath);
                    //上传完成后刷新文件列表 
                    fileData = cts.GetFileList(fileInfo);
                    refreshFileData();
                }
            }).start();
        }
        
        //下载按钮事件
        public void btnDownloadActionPerformed(ActionEvent e){
            //当索引操作文件列表返回，直接返回以免数组越界
            if (fileList.getSelectedIndex() >= fileData.length - 1)
                return;
            FileList getFileList = fileData[fileList.getSelectedIndex()];
            if (getFileList.isIsDirectory()){
                JOptionPane.showMessageDialog(null,"暂时无法下载目录，该功能将会后续开发。。。","系统提示",JOptionPane.INFORMATION_MESSAGE);
                    return;
            }

            JFileChooser fdSave = new JFileChooser();  
            fdSave.setFileSelectionMode(JFileChooser.FILES_ONLY);   //设置文件选择器只加载文件不加载文件夹
            fdSave.setSelectedFile(new File(getFileList.getFileName()));    //设置文件选择器的默认名称为所下载文件的名称
            int result = fdSave.showDialog(new JLabel(), "下载文件");  

            File file = fdSave.getSelectedFile();
            if ( result == JOptionPane.NO_OPTION)
                return;

            String fileName,filePath,userID,saveFilePath;
            fileName = getFileList.getFileName();
            filePath = fileInfo.getFilePath() + getFileList.getFileName();
            userID = user.getUserID();
            saveFilePath = file.getPath();

            //创建下载线程
            new Thread(new Runnable(){
                public void run(){
                    JLabel lab = new JLabel(fileName);
                    JProgressBar pgb = new JProgressBar();
                    JLabel statu = new JLabel("正在等待");
                    panMyDown.add(lab);
                    panMyDown.add(pgb);
                    panMyDown.add(statu);
                    cts.Download(fileName,filePath,userID,saveFilePath,pgb);
                }
            },"download").start();
            
        }

        
        //分享按钮事件
        public void btnShareActionPerformed(ActionEvent e){
            
        }
        
        //删除文件按钮事件
        public void btnDeleteActionPerformed(ActionEvent e){
            if (fileList.getSelectedIndex() >= fileData.length - 1)
                return;
            int confirm = JOptionPane.showConfirmDialog(null,"文件删除后将无法恢复！是否确定删除？","系统提示",JOptionPane.OK_CANCEL_OPTION);
            if (confirm != 0){
               return; 
            }
            int result = cts.DeleteFile(user.getUserID(),fileInfo.getFilePath() + fileData[fileList.getSelectedIndex()].getFileName());
            if (result == 1){
                refreshFileData();
            }else{
                JOptionPane.showMessageDialog(null,"删除失败，请重试。","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        //创建文件夹按钮事件
        public void btnMkdirActionPerformed(ActionEvent e){
            String dirName = JOptionPane.showInputDialog("请输入新的文件夹的名称：");
            if (!dirName.equals("")){
                String filePath = fileInfo.getFilePath() + dirName;
                int result = cts.Mkdir(user.getUserID(),filePath);
                if (result == 1){
                    refreshFileData();
                }else{
                JOptionPane.showMessageDialog(null,"创建失败，请重试。","系统提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        //返回上级目录按钮事件
        public void btnReturnDirectoryActionPerformed(ActionEvent e){
            String getFilePath = fileInfo.getFilePath();
            if (!getFilePath.equals("/")){
                int local = getFilePath.lastIndexOf("/",getFilePath.length() - 2);  //定位倒数第二个“/”所在位置，以获取上级目录
                if (local == 0){
                    fileInfo.setFilePath("/");
                    fileInfo.setFileRequest(3);     //设置为3表示获取文件目录
                    fileInfo.setRuturnInfo(0);
                    fileData = cts.GetFileList(fileInfo);
                    refreshFileData();  //刷新列表数据
                    txtDirectory.setText(("   我的网盘" + fileInfo.getFilePath()).replaceAll("/","  ▶ "));
                }else{
                    fileInfo.setFilePath(getFilePath.substring(0,local) + "/");
                    fileInfo.setFileRequest(3);     //设置为3表示获取文件目录
                    fileInfo.setRuturnInfo(0);
                    fileData = cts.GetFileList(fileInfo);
                    refreshFileData();  //刷新列表数据
                    txtDirectory.setText(("   我的网盘" + fileInfo.getFilePath()).replaceAll("/","  ▶ "));
                }
            }
        }
        
        //刷新目录按钮事件
        public void btnRefreshDirectoryActionPerformed(ActionEvent e){
            refreshFileData();  //刷新列表数据
        }
        
        //查找文件按钮事件
        public void btnSearchFileActionPerformed(ActionEvent e){
            refreshFileData();
        }
        
        //重命名文件按钮事件
        public void itemFileRenameActionPerformed(ActionEvent e){
            if (fileList.getSelectedIndex() >= fileData.length - 1)
                return;
            String newName = JOptionPane.showInputDialog("请输入新的文件名:");
            int request = cts.FileRename(user.getUserID(),fileInfo.getFilePath() + fileData[fileList.getSelectedIndex()].getFileName(), newName);
            if (request == 1){
                refreshFileData();
            }else{
                JOptionPane.showMessageDialog(null,"重命名失败，请重试。","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        //查找好友按钮事件
        public void btnSearchFriendActionPerformed(ActionEvent e){
            if (txtSearchFriend.getText().equals("")){
                refreshFriendData();
            }
        }
        
        //添加好友按钮事件
        public void btnAddFriendActionPerformed(ActionEvent e){
            String friendID = JOptionPane.showInputDialog("请输入您想添加的用户ID：");
            if (friendID == null)
                return;
            int result = cts.AddFriend(user.getUserID(), friendID);
            if (result == 1){
                refreshFriendData();
                JOptionPane.showMessageDialog(null,"添加成功！","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null,"该用户不存在！请检查输入的用户账号是否正确","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        //删除好友事件
        public void itemDeleteFriendActionPerformed(ActionEvent e){
            int confirm = JOptionPane.showConfirmDialog(null,"确认删除好友吗？","系统提示",JOptionPane.OK_CANCEL_OPTION);
            if (confirm != 0){
               return; 
            }
            String friendID = friendData[friendList.getSelectedIndex()].getFriendID();
            int result = cts.DeleteFriend(user.getUserID(),friendID);
            if (result == 1){
                JOptionPane.showMessageDialog(null,"删除成功！","系统提示",JOptionPane.INFORMATION_MESSAGE);
                refreshFriendData();
            }else{
                JOptionPane.showMessageDialog(null,"删除失败！","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        //进入隐私文件按钮事件
        public void btnEnterSecretActionPerformed(ActionEvent e){
            String secretPwd = new String(pwdSecret.getPassword());
            if (hasSecretFile == false){
                String setPwd = new String(pwdSecret.getPassword());
                String confirmPwd = new String(pwdConfirm.getPassword());
                if (setPwd.equals("")){
                    JOptionPane.showMessageDialog(null,"密码不能为空，请重新设置。","系统提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (!setPwd.equals(confirmPwd)){
                    JOptionPane.showMessageDialog(null,"两次密码不相同，请确保正确输入相同密码。","系统提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (setPwd.equals(confirmPwd)){
                    user.setPrivatePass(secretPwd);
                    hasSecretFile = true;
                    card.show(panSecret,"secretFile");
                }
            }else{
                if (secretPwd.equals(user.getPrivatePass())){
                    card.show(panSecret,"secretFile"); 
                }else if(secretPwd.equals(user.getPrivatePass())){
                    JOptionPane.showMessageDialog(null,"密码错误，请重新输入。","系统提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        //发送消息按钮点击事件
        public void btnSendActionPerformed(ActionEvent e){
            String text = panSend.getText();
            if (friendList.getSelectedIndex() >= friendData.length || friendList.isSelectionEmpty() || text.equals(""))
                return;
            Friend getFriend = (Friend) friendList.getSelectedValue();
            int result = cts.SentChat(user.getUserID(),getFriend.getFriendID(),text);
            if (result == 1){
                refreshChatPane();
                panSend.setText("");
            }else{
                JOptionPane.showMessageDialog(null,"发送失败！","系统提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        //好友面板通过点击好友列表刷新聊天面板内容
        public void refreshChatPane(){
            if (friendList.getSelectedIndex() >= friendData.length)
                return;
            panChat.setText("");
            Friend getFriend = (Friend) friendList.getSelectedValue();
            newFriendIndex = friendList.getSelectedIndex();
            if (getFriend != null){
                ChatList[] message = cts.ReceiveAllChat(user.getUserID(), getFriend.getFriendID());
                if (message != null){
                    for (ChatList message1 : message) {
                        if (message1.getRequest() == 666) {
                            try {
                                String text = message1.getCharTime().substring(0,message1.getCharTime().indexOf(".")) + "\n" + message1.getMessages() + "\n";
                                StyledDocument docStyle = panChat.getStyledDocument();
                                SimpleAttributeSet leftAttribut = new SimpleAttributeSet();
                                SimpleAttributeSet rightAttribut = new SimpleAttributeSet();
                                
                                if (user.getUserID().equals(message1.getUserID())) {
                                    StyleConstants.setAlignment(rightAttribut,StyleConstants.ALIGN_RIGHT);
                                    docStyle.setParagraphAttributes(docStyle.getLength(),text.length(),rightAttribut,false);
                                    docStyle.insertString(docStyle.getLength(),text,rightAttribut);
                                    panChat.setCaretPosition(docStyle.getLength());
                                    oldFriendIndex = newFriendIndex;
                                } else {
                                    StyleConstants.setAlignment(leftAttribut,StyleConstants.ALIGN_LEFT);
                                    docStyle.setParagraphAttributes(docStyle.getLength(),text.length(),leftAttribut,false);
                                    docStyle.insertString(docStyle.getLength(),text,leftAttribut);
                                    panChat.setCaretPosition(docStyle.getLength());
                                    oldFriendIndex = newFriendIndex;
                                }
                            }catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        
        public void refreshChatThread() throws InterruptedException{
            while(true){
                Thread.currentThread().sleep(3000);
                refreshChatPane();
                if (oldFriendIndex != newFriendIndex){
                    Thread.currentThread().stop();
                }
            }
        }
        
        //刷新列表数据
        public void refreshFileData(){
            fileData = cts.GetFileList(fileInfo);
            fileList.setListData(fileData);
            FileListCell newFlc = new FileListCell(fileData);
            fileList.setCellRenderer(newFlc);
        }
        //刷新好友数据
        public void refreshFriendData(){
            friendData = cts.GetFriendList(user.getUserID());
            friendList.setListData(friendData);
            FriendListCell newFrilc = new FriendListCell(friendData);
            friendList.setCellRenderer(newFrilc);
        }


}

class refreshMeeageThread extends Thread{
    
    private int oldIndex;
    private int newIndex;
    private MainJFrame m;
    
    refreshMeeageThread(int oldIndex,int newIndex,MainJFrame m){
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.m = m;
    }
    
    public void run(){
        while(true){
            try {
                m.refreshChatPane();
                sleep(3000);
                System.out.println(Thread.currentThread().getId());
                if (oldIndex != newIndex){
                    stop();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(refreshMeeageThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}