package bldisk.client;

import bldisk.entity.ChatList;
import bldisk.entity.FileInfo;
import bldisk.entity.FileList;
import bldisk.entity.Friend;
import bldisk.entity.UserInfo;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

public class ClientToServer {
    private Socket socket = null;  
    private ObjectOutputStream outFileInfo = null;  //文件对象输出流
    private ObjectInputStream inFileInfo = null;    //文件对象输入流
    private ObjectOutputStream outFriendInfo = null;    //好友对象输出流
    private ObjectInputStream inFriendInfo = null;     //好友对象输入流
    private ObjectOutputStream outChatInfo = null;   //聊天列表对象输出流
    private ObjectInputStream inChatInfo = null;    //聊天列表对象输入流
    private DataOutputStream outputFile = null;   //文件输出流
    private DataInputStream inputFile= null;  	//文件输入流
    private File openFile = null;  //打开文件资源
    private byte[] Buff = null;  //写入读出文件缓存
    private int length = 0;  //读出写入长度
    private int fileSize = 0; //文件大小
    private FileInfo fileInfo = null; //文件对象
    private UserInfo userInfo = null; //用户对象 
    private PrintWriter requestInfo = null;//文本输出流
    private BufferedReader returnInfo = null; //文本输入流
    private Friend friendInfo = null;
    private ChatList chatList=null;

    //登录方法
    public int Login(String userAccount,String userPassword){ 
        try {
            //服务请求
            if(Request(1)){
                userInfo = new UserInfo();
                userInfo.setUserID(userAccount);
                userInfo.setUserPass(userPassword);
                //设置为登录操作
                userInfo.setType(1);
                //发送对象到服务器
                outFileInfo = new ObjectOutputStream(socket.getOutputStream());
                outFileInfo.writeObject(userInfo);  
                outFileInfo.flush();
                inFileInfo = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                Object obj = inFileInfo.readObject();
                if (obj != null) {  
                    userInfo = (UserInfo)obj;  
                    if(userInfo.getCheck()==1){
                        //如果登录成功就返回1
                        return 1;   //返回1代表登陆成功
                    }else{
                            return 2;  //返回2代表密码错误
                    }
                }else{
                    return 3;   //返回3代表账号不存在
                }
            }else{
                    System.out.println("连接服务器失败");
                    return 4;   //返回4代表没有网络，连接服务器失败
            }
        } catch(IOException ex) {  
            ex.printStackTrace(); 
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                if (inFileInfo != null)
                    inFileInfo.close();
                if (outFileInfo != null)
                    outFileInfo.close();
                if (socket != null)
                    socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientToServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    //注册方法
    public void SignUp(UserInfo userInfo){
        try {
            //服务请求
            if(Request(1)){
                //读取服务器发过来的数据
                outFileInfo = new ObjectOutputStream(socket.getOutputStream());
                //发送对象到服务器
                outFileInfo.writeObject(userInfo);
                outFileInfo.flush();  
                //接收服务器返回的数据
                inFileInfo = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                Object obj = inFileInfo.readObject();  
                if (obj != null) {  
                    userInfo = (UserInfo)obj;  
                    if(userInfo.getCheck()==1){
                            System.out.println("注册成功");
                    }else{
                            System.out.println("注册失败，您的账号已存在");
                    }
                }else{
                    System.out.println("注册失败，请重新再试！");
                }
                inFileInfo.close();  
                outFileInfo.close();
            }else{
                    System.out.println("连接服务器失败");
            }
        socket.close(); 
    } catch(IOException ex) {  
        ex.printStackTrace(); 
    } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
    }

    //上传文件
    public synchronized void Upload(String userID,String filePath){    
    try { 
            //服务请求
            if(Request(2)){
                    //打开文件资源
                openFile = new File(filePath); 
                //获取文件长度，最后可以判断文件传输是否完整
                long l = openFile.length();            
                //发送文件信息
                outFileInfo=new ObjectOutputStream(socket.getOutputStream());
                //设置文件信息
                fileInfo=new FileInfo();
                fileInfo.setUserID(userID);
                fileInfo.setFileName(openFile.getName());
                fileInfo.setFilePath(openFile.getPath());
                fileInfo.setFileSize(openFile.length());
                //设置为1表示上传文件
                fileInfo.setFileRequest(1);
                fileInfo.setRuturnInfo(0);
                //发送给服务端文件信息
                outFileInfo.writeObject(fileInfo);	            
                //发送文件
                outputFile = new DataOutputStream(socket.getOutputStream());  
                inputFile = new DataInputStream(new FileInputStream(openFile));        
                Buff = new byte[1024];    
                while ((length = inputFile.read(Buff, 0, Buff.length)) > 0) {  
                    fileSize += length;    
                    System.out.println("已传输："+((fileSize/l)*100)+"%");  
                    outputFile.write(Buff, 0, length);  
                    outputFile.flush();  
                }   
                //判断文件是否上传完整  
                if(fileSize==l){  
                    System.out.println("传输完成"); 
                }
                System.out.println("发送完毕");
                //接收文件上传情况
                inFileInfo=new ObjectInputStream(socket.getInputStream()); 
                fileInfo=(FileInfo)inFileInfo.readObject();
                //关闭资源
                outputFile.close();
                inputFile.close();
                outFileInfo.close();
                inFileInfo.close();
            }else{
                    System.out.println("连接服务器失败");
            }
        socket.close();
    }catch (Exception e) {  
        System.out.println("客户端文件传输异常");  
        e.printStackTrace();    
    }
} 

    //下载文件
    public synchronized void Download(String fileName,String filePath,String userID,String saveFilePath,JProgressBar pgb){
        
        try { 
            //服务请求
            if(Request(2)){
                //向服务器发送请求文件信息
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(fileName);    //设置要下载的文件的名称
                fileInfo.setFilePath(filePath);   //设置要下载的文件的目录
                fileInfo.setUserID(userID);
                //设置为2表示下载请求
                fileInfo.setFileRequest(2);
                fileInfo.setRuturnInfo(0);
                //创建输出流
                outFileInfo=new ObjectOutputStream(socket.getOutputStream());
                outFileInfo.writeObject(fileInfo);
                outFileInfo.flush();
                
                //创建接收服务器发送过来的文件信息的接收流
                inFileInfo=new ObjectInputStream(socket.getInputStream());
                fileInfo=(FileInfo)inFileInfo.readObject();
                //创建下载接收流
                inputFile=new DataInputStream(socket.getInputStream());
                //打开文件资源
                outputFile = new DataOutputStream(new FileOutputStream(new File(saveFilePath))); 
                Buff = new byte[1024];
                //开始传输文件
                length=inputFile.read(Buff,0,Buff.length);
                pgb.setMaximum((int)fileInfo.getFileSize());
                while(length>0){
                        outputFile.write(Buff, 0,length);
                        outputFile.flush();
                        fileSize+=length;
                        pgb.setValue(fileSize);
                        System.out.println("下载了："+((float)(fileSize)/(float)(fileInfo.getFileSize()))*100+"%");
                        if(fileInfo.getFileSize()==fileSize){
                            System.out.println("下载完毕");
                            break;
                        }else{
                            length=inputFile.read(Buff,0,Buff.length);
                        }
                }
                outputFile.close();
                inputFile.close();
                outFileInfo.close();
                inFileInfo.close();
            }else{
                    System.out.println("连接服务器失败");
            }
                socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
        }
    }
    
    //重命名文件
    public int FileRename(String userID,String filePath,String newName){
        if (Request(2)){
            PrintWriter pw = null;
            BufferedReader br = null;
            try{
                FileInfo file = new FileInfo();
                file.setUserID(userID);
                file.setFileRequest(4);
                file.setRuturnInfo(0);
                
                outFileInfo = new ObjectOutputStream(socket.getOutputStream());
                //向服务器发送请求文件信息
                outFileInfo.writeObject(file);
                outFileInfo.flush();
                
                inFileInfo=new ObjectInputStream(socket.getInputStream());
                fileInfo=(FileInfo)inFileInfo.readObject();
                
                if (fileInfo.getRuturnInfo() == 1){
                    //创建打印流，把原名和修改名发送到客户端
                    pw  = new PrintWriter(socket.getOutputStream());
                    pw.println(filePath);
                    pw.println(newName);
                    pw.flush();

                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int request = br.read();
                    return request;
                }
                return 0;
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientToServer.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    if (pw != null)
                        pw.close();
                    if (br != null)
                        br.close();
                    outFileInfo.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }
    
    //删除文件
    public int DeleteFile(String userID,String filePath){
        if (Request(2)){
            PrintWriter pw = null;
            BufferedReader br = null;
            try{
                FileInfo file = new FileInfo();
                file.setUserID(userID);
                file.setFileRequest(5);
                file.setRuturnInfo(0);
                
                outFileInfo = new ObjectOutputStream(socket.getOutputStream());
                //向服务器发送请求文件信息
                outFileInfo.writeObject(file);
                outFileInfo.flush();
                
                inFileInfo=new ObjectInputStream(socket.getInputStream());
                fileInfo=(FileInfo)inFileInfo.readObject();
                
                if (fileInfo.getRuturnInfo() == 1){
                    pw  = new PrintWriter(socket.getOutputStream());
                    pw.println(filePath);
                    pw.flush();

                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int request = br.read();
                    return request;
                }
                
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }finally{
                try {
                    if (pw != null)
                        pw.close();
                    if (br != null)
                        br.close();
                    outFileInfo.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }
    
    //创建文件夹
    public int Mkdir(String userID,String filePath){
        if (Request(2)){
            PrintWriter pw = null;
            BufferedReader br = null;
            try{
                FileInfo file = new FileInfo();
                file.setUserID(userID);
                file.setFileRequest(6);
                file.setRuturnInfo(0);
                
                outFileInfo = new ObjectOutputStream(socket.getOutputStream());
                //向服务器发送请求文件信息
                outFileInfo.writeObject(file);
                outFileInfo.flush();
                
                inFileInfo=new ObjectInputStream(socket.getInputStream());
                fileInfo=(FileInfo)inFileInfo.readObject();
                
                if (fileInfo.getRuturnInfo() == 1){
                    //创建打印流，把原名和修改名发送到客户端
                    pw  = new PrintWriter(socket.getOutputStream());
                    pw.println(filePath);
                    pw.flush();

                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int request = br.read();
                    return request;
                }
                
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }finally{
                try {
                    if (pw != null)
                        pw.close();
                    if (br != null)
                        br.close();
                    outFileInfo.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }

    //获取文件列表
    public FileList[] GetFileList(FileInfo fileInfo){
        if(Request(2)){
            
            //创建输出流
            try { 
                outFileInfo=new ObjectOutputStream(socket.getOutputStream());
                //向服务器发送请求文件信息
                outFileInfo.writeObject(fileInfo);
                outFileInfo.flush();
                //接收服务器发过来的文件列表
                inFileInfo=new ObjectInputStream(socket.getInputStream());
                FileList[] fileList=(FileList[])inFileInfo.readObject();
                return fileList;
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
            }else{
                    System.out.println("连接服务器失败");
            }
                return null;
    }
    
    //获取好友列表
    public Friend[] GetFriendList(String userID){
            if(Request(3)){
                try {
                        //设置获取好友列表
                        friendInfo = new Friend();
                        friendInfo.setUserID(userID);
                        //设为1表示请求获取好友列表
                        friendInfo.setRequest(1);
                        //获取好友信息输出流,将好友信息发送到服务端
                        outFriendInfo=new ObjectOutputStream(socket.getOutputStream());
                        outFriendInfo.writeObject(friendInfo);
                        outFriendInfo.flush();
                        //获取好友列表
                        inFriendInfo = new ObjectInputStream(socket.getInputStream());
                        Friend[] friendList = (Friend[])inFriendInfo.readObject();
                        outFriendInfo.close();
                        inFriendInfo.close();
                        socket.close();
                        return friendList;
                } catch (IOException e) {
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                }			
            }else{
                    System.out.println("连接服务器失败");
            }
            return null;
    }

    //添加好友
    public int AddFriend(String userID,String friendID){
        if(Request(3)){
            try {
                    //获取好友信息输出流
                    outFriendInfo=new ObjectOutputStream(socket.getOutputStream());
                    //设置获取好友列表
                    friendInfo=new Friend();
                    friendInfo.setUserID(userID);
                    friendInfo.setFriendID(friendID);
                    //设置分组
                    friendInfo.setFriendType("我的好友");
                    //设为2表示添加好友请求
                    friendInfo.setRequest(2);
                    //发送好友列表请求信息
                    outFriendInfo.writeObject(friendInfo);
                    outFriendInfo.flush();
                    //接收添加情况
                    inFriendInfo=new ObjectInputStream(socket.getInputStream());
                    friendInfo=(Friend)inFriendInfo.readObject();
                    if(friendInfo.getFriendName().equals("1")){
                            inFriendInfo.close();
                            outFriendInfo.close();
                            return 1;
                    }
                    inFriendInfo.close();
                    outFriendInfo.close();
                    return 0;
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }			
        }else{
                System.out.println("连接服务器失败");
        }
        return 0;
    }

    //删除好友
    public int DeleteFriend(String userID,String friendID){
        if(Request(3)){
                try {
                        //获取好友信息输出流
                        outFriendInfo=new ObjectOutputStream(socket.getOutputStream());
                        //设置获取好友列表
                        friendInfo=new Friend();
                        friendInfo.setUserID(userID);
                        friendInfo.setFriendID(friendID);
                        //设为3表示删除好友请求
                        friendInfo.setRequest(3);
                        //发送好友列表请求信息
                        outFriendInfo.writeObject(friendInfo);
                        outFriendInfo.flush();
                        //接收添加情况
                        BufferedReader getReturnInfo=new BufferedReader(new InputStreamReader(socket.getInputStream()));          
                        if((int)getReturnInfo.read()==1){
                                return 1;
                        }
                        outFriendInfo.close();
                        return 0;
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }else{
                System.out.println("连接服务器失败");
        }
        return 0;
    }

    //发送信息
    public int SentChat(String userID,String friendID,String content){
        if(Request(4)){
            try {
                //获取好友信息输出流
                outChatInfo=new ObjectOutputStream(socket.getOutputStream());
                //设置聊天请求服务
                chatList=new ChatList();
                        //设置用户的账号
                chatList.setUserID(userID);
                        //设置发送好友的账号
                chatList.setFriendID(friendID);
                        //这里设置发送的信息
                chatList.setMessages(content);
                        //这里设置请求类型，1表示发送信息
                chatList.setRequest(1);
                //发送请求信息
                outChatInfo.writeObject(chatList);
                outChatInfo.flush();
                //接收传过来的信息
                inChatInfo=new ObjectInputStream(socket.getInputStream());
                chatList=(ChatList)inChatInfo.readObject();
                if(chatList.getRequest()==666){
                    return 1;
                }
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }		
        }else{
                System.out.println("连接服务器失败");
        }
        return 0;
    }
	//接收信息
	//获取新信息只需要获取好友发来的信息就好，本地发送的自动存储下来。
	void ReceiveNewChat(){
            if(Request(4)){
                try {
                    //获取好友信息输出流
                    outChatInfo=new ObjectOutputStream(socket.getOutputStream());
                    //设置聊天请求服务
                    chatList=new ChatList();
                            //设置用户的账号
                    chatList.setUserID("150202102365");
                            //设置发送好友的账号
                    chatList.setFriendID("150202102399");
                            //这里设置请求类型，2获取新信息
                    chatList.setRequest(2);
                    //发送请求信息
                    outChatInfo.writeObject(chatList);
                    outChatInfo.flush();
                    //接收传过来的信息
                    inChatInfo=new ObjectInputStream(socket.getInputStream());
                    ChatList[] receiveChat=(ChatList[])inChatInfo.readObject();
                    //接收信息，判断是否为真实信息还是拒绝请求
                    for (int i = 0; i < receiveChat.length; i++) {
                        if(receiveChat[i].getRequest()==666){
                            System.out.println("好友ID："+receiveChat[i].getFriendID());
                            System.out.println("发送来的信息："+receiveChat[i].getMessages());
                            System.out.println("发送时间："+receiveChat[i].getCharTime());
                            System.out.println("-----------------------");
                        }
                    }
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }		
            }else{
                    System.out.println("连接服务器失败");
            }
	}
	//获取整个聊天列表信息
	//用于开启软件时，先把聊天记录加载下来
	public ChatList[] ReceiveAllChat(String userID,String friendID){
            if(Request(4)){
                try {
                    //获取好友信息输出流
                    outChatInfo=new ObjectOutputStream(socket.getOutputStream());
                    //设置聊天请求服务
                    chatList=new ChatList();
                            //设置用户的账号
                    chatList.setUserID(userID);
                            //设置发送好友的账号
                    chatList.setFriendID(friendID);
                            //这里设置请求类型，3表示获取全部聊天列表
                    chatList.setRequest(3);
                    //发送请求信息
                    outChatInfo.writeObject(chatList);
                    outChatInfo.flush();
                    //接收传过来的信息
                    inChatInfo=new ObjectInputStream(socket.getInputStream());
                    ChatList[] receiveChat=(ChatList[])inChatInfo.readObject();
                    if (receiveChat != null){
                        return receiveChat;
                    }
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }		
            }else{
                System.out.println("连接服务器失败");
            }
            return null;
	}

    //操作请求
    public boolean Request(int select){
            try {
                socket = new Socket("localhost",6666);
//                socket = new Socket("172.18.184.2",6666);
                //创建输出流
                requestInfo=new PrintWriter(socket.getOutputStream());
                //创建输入流
                returnInfo=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //向服务端发出请求
                requestInfo.write(select);
                requestInfo.flush();
                //接收服务器返回的信息
                int Return=returnInfo.read();
                if(Return==66){
                        return true;
                }
            } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
            return false;
    }
    
}
