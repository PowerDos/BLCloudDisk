package bldisk.server;

import bldisk.dao.*;
import bldisk.entity.*;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
   private static final int PORT=6666;
   public static void main(String[] args){
       try {
           ServerSocket s=new ServerSocket(PORT);
           int select=0;
           while(true){
               Socket socket=s.accept();
               //接收用户请求操作类型
               BufferedReader getReturnInfo=new BufferedReader(new InputStreamReader(socket.getInputStream()));
               select=(int)getReturnInfo.read();
               //返回请求接收与否的信息
               PrintWriter returnInfo=new PrintWriter(socket.getOutputStream());
               if(select>0&&select<=4){
                   //如果操作在功能范围内就返回成功并执行相应操作
                   returnInfo.write(66);
                   returnInfo.flush();
                   switch(select){
                       case 1:
                           //进入用户相关操作线程
                          new UserThread(socket);
                          break;
                       case 2:
                          //进入文件相关操作线程
                          new FileThread(socket);
                           break;
                       case 3:
                          //进入好友处理线程
                          new FriendThread(socket);
                          break;
                       case 4:
                          //进入好友处理线程
                          new ChatThread(socket);
                          break;
                   }
               }else{
                   //如果请求不在范围内就返回错误
                   returnInfo.write(0);
                   returnInfo.flush();                   
               }
           }
       } catch (IOException ex) {
          ex.printStackTrace();
       }  
   } 
}

class UserThread extends Thread{
    private Socket client;
    private ObjectOutputStream outFileInfo;
    private ObjectInputStream inFileInfo;
    private UserInfo userInfo;
    public UserThread(Socket s){
        this.client=s;
        try {
            this.outFileInfo=new ObjectOutputStream(client.getOutputStream());
            this.inFileInfo=new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            this.userInfo=(UserInfo)this.inFileInfo.readObject();
            start();
        } catch (IOException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        System.out.println(userInfo.getType());
        switch(userInfo.getType()){
            case 1:
                //验证登录
                CheckLogin();
                break;
            case 2:
                //注册
                SignUpUser();
                break;
        }
    }
    
    //验证登录信息
    private void CheckLogin(){
        LoginDao login=new LoginDao();
        UserInfo sentUserInfo=login.CheckLogin(userInfo.getUserID(),userInfo.getUserPass());
         try {
            if(sentUserInfo.getCheck()==1){
                outFileInfo.writeObject(sentUserInfo);
            }else{
                sentUserInfo.setCheck(0);
                outFileInfo.writeObject(sentUserInfo);
            }
            outFileInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //注册
    private void SignUpUser(){
        LoginDao signUp=new LoginDao();
        UserInfo sentUserInfo=new UserInfo();
        //判断用户是否存在
        if(signUp.ExistUser(userInfo.getUserID())){
            sentUserInfo=signUp.SignUp(userInfo.getUserID(), userInfo.getUserName(), userInfo.getUserPass(), userInfo.getUserMail());
        }else{
            System.out.println("用户存在");
        }
         try {
            if(sentUserInfo.getCheck()==1){
                //注册成功
                outFileInfo.writeObject(sentUserInfo);
                
                //注册成功后为用户创建用户的文件夹
                File defaultFile  = new File("file/" + userInfo.getUserID() + "/我的文档");
                defaultFile.mkdirs();
                defaultFile = new File("file/" + userInfo.getUserID() + "/我的视频");
                defaultFile.mkdirs();
            }else{
                sentUserInfo.setCheck(0);
                outFileInfo.writeObject(sentUserInfo);
            }
            outFileInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

class FileThread extends Thread{
    private Socket client;
    private DataInputStream inputFile;
    private DataOutputStream outputFile;
    private ObjectOutputStream outFileInfo;
    private ObjectInputStream inFileInfo;
    private byte[] Buff=null;
    private int length;
    private final String filePath="file/";
    private FileInfo fileInfo;
    private int fileSize;
    
    public FileThread(Socket s){
        this.client=s;
        start();
    } 
    public void run(){
        try {
            inFileInfo=new ObjectInputStream(client.getInputStream());
            fileInfo=new FileInfo();
            fileInfo=(FileInfo)inFileInfo.readObject();
            switch(fileInfo.getFileRequest()){
                case 1:
                    UploadFile();
                    break;
                case 2:
                    DownLoad();
                    break;
                case 3:
                    ReturnFileList();
                    break;
                case 4:
                    FileRename();
                    break;
                case 5:
                    DeleteFile();
                    break;
                case 6:
                    Mkdir();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //上传文件
    void UploadFile(){
        try { 
            //接收文件信息类
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            //输出文件信息
            System.out.println(this.fileInfo.toString());
            //接收文件         
            inputFile = new DataInputStream(client.getInputStream()); 
            //打开文件资源
            File f = new File(filePath+fileInfo.getUserID()+"/");
            //查看文件夹是否存在
            if(!f.exists()){  
                f.mkdirs();    
            } 
            //保存文件
            outputFile = new DataOutputStream(new FileOutputStream(filePath+fileInfo.getUserID()+"/"+fileInfo.getFileName()));      
            Buff = new byte[1024];     
            System.out.println("开始接收数据...");    
            length = inputFile.read(Buff,0, Buff.length);
            while (length > 0) {  
                fileSize+=length;
                outputFile.write(Buff, 0, length);  
                outputFile.flush(); 
                //如果文件接收完毕
                if (fileSize==fileInfo.getFileSize()) {
                    //更新数据库信息
                    FileDao fileDao=new FileDao();
                    if(fileDao.UploadFile(fileInfo)){
                        //更新成功
                        fileInfo.setRuturnInfo(1);
                    }
                    //向客户端返回文件传输情况
                    outFileInfo.writeObject(fileInfo);
                    outFileInfo.flush();
                }else{
                    //如果还没接收完毕就继续接收
                    length = inputFile.read(Buff,0, Buff.length);
                }
            }  
            System.out.println("完成接收");
            //关闭资源
            outFileInfo.close();
            inFileInfo.close();
            outputFile.close();
            inputFile.close();
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //下载文件
    void DownLoad(){ 
        try {
            //打开文件
            File file = new File(filePath+fileInfo.getUserID()+"/"+fileInfo.getFilePath()); //要传输的文件路径  
            long l = file.length();
            fileInfo.setFileName(file.getName());
            fileInfo.setFileSize(file.length());
            fileInfo.setRuturnInfo(1);
            //向客户端发送文件信息
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            outFileInfo.writeObject(fileInfo);
            outFileInfo.flush();
            //创建发送文件的输出流
            outputFile=new DataOutputStream(client.getOutputStream());
            DataInputStream fisFile = new DataInputStream(new FileInputStream(file));
            //发送文件         
            Buff = new byte[1024];    
            while ((length = fisFile.read(Buff, 0, Buff.length)) > 0) {  
                fileSize += length;    
                outputFile.write(Buff, 0, length);  
                outputFile.flush();  
            }
            //更新数据库
             FileDao fileDao=new FileDao();
             fileDao.UploadFile(fileInfo);
            //判断文件是否上传完整  
            if(fileSize==l){  
               System.out.println("下载完毕");
            }
            outFileInfo.close();
            inFileInfo.close();
            outputFile.close();
            fisFile.close();
            client.close();
        }catch (Exception e) {  
            e.printStackTrace();    
        }
    }
    
    void FileRename(){
        BufferedReader br = null;
        PrintWriter pw = null;
        try{
            fileInfo.setRuturnInfo(1);
            //向客户端发送文件信息
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            outFileInfo.writeObject(fileInfo);
            outFileInfo.flush();
            
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String renameFilePath,newName;
            renameFilePath = br.readLine();
            newName = br.readLine();
            String path = filePath + fileInfo.getUserID() + renameFilePath;
            File f = new File(path);
            f.renameTo(new File(path.substring(0,path.lastIndexOf("/") + 1) + newName));
            pw = new PrintWriter(client.getOutputStream());
            pw.write(1);
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if (br != null)
                    br.close();
                outFileInfo.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //删除文件
    void DeleteFile(){
        BufferedReader br = null;
        PrintWriter pw;
        try{
            fileInfo.setRuturnInfo(1);
            //向客户端发送文件信息
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            outFileInfo.writeObject(fileInfo);
            outFileInfo.flush();
            
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String deleteFilePath;
            deleteFilePath = br.readLine();
            String path = filePath + fileInfo.getUserID() + deleteFilePath;
            File f = new File(path);
            if (f.isDirectory() && f.listFiles() != null){
                File[] fileChild = f.listFiles();
                for (File fileChild1 : fileChild) {
                    fileChild1.delete();
                }
                f.delete();
            }else{
                f.delete();
            }
            pw = new PrintWriter(client.getOutputStream());
            pw.write(1);
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if (br != null)
                    br.close();
                outFileInfo.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    void Mkdir(){
        BufferedReader br = null;
        PrintWriter pw;
        try{
            fileInfo.setRuturnInfo(1);
            //向客户端发送文件信息
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            outFileInfo.writeObject(fileInfo);
            outFileInfo.flush();
            
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String deleteFilePath;
            deleteFilePath = br.readLine();
            String path = filePath + fileInfo.getUserID() + deleteFilePath;
            File f = new File(path);
            f.mkdirs();
            pw = new PrintWriter(client.getOutputStream());
            pw.write(1);
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if (br != null)
                    br.close();
                outFileInfo.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //返回目录下文件列表
    void ReturnFileList(){
        try {
            //向客户端发送目录下文件列表信息
            outFileInfo=new ObjectOutputStream(client.getOutputStream());
            outFileInfo.writeObject(GetFileList(fileInfo.getUserID(),fileInfo.getFilePath()));
            outFileInfo.flush();
            inFileInfo.close();
            outFileInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    //获取目录下文件
    static FileList[] GetFileList(String UserID,String filePath){
        //传输过来的目录是以账户/根开始
        File rootFile=new File("file/"+UserID+filePath);
        File[] fileArray=rootFile.listFiles();
        FileList[] fileList=new FileList[fileArray.length+1];
        SimpleDateFormat cTime=new SimpleDateFormat("yyyy-MM-dd H:m:s");
        for(int i=0;i<fileArray.length;i++){
            //获取目录下的文件信息
            fileList[i]=new FileList();
            fileList[i].setFileName(fileArray[i].getName());
            fileList[i].setFileSize(fileArray[i].length());  
            fileList[i].setLastModified(cTime.format(fileArray[i].lastModified()));
            //判断是否是文件夹
            fileList[i].setIsDirectory(fileArray[i].isDirectory());
        }
        return fileList;
    }
}

class FriendThread extends Thread{
    private Socket client=null;
    private ObjectOutputStream outFriendInfo=null;
    private ObjectInputStream inFriendInfo=null;
    private Friend friendInfo=null;
    FriendThread(Socket s){
        client = s;
        start();
    }
    public void run(){
        try {
            inFriendInfo=new ObjectInputStream(client.getInputStream());
            friendInfo=(Friend)inFriendInfo.readObject();
            switch(friendInfo.getRequest()){
                case 1:
                    FriendList();
                    break;
                case 2:
                    AddFriend();
                    break;
                case 3:
                    DeleteFriend();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //添加好友
    void AddFriend(){
        try {
            FriendDao friend=new FriendDao();
            //返回请求添加好友成功与否的信息
            outFriendInfo=new ObjectOutputStream(client.getOutputStream());
            //添加好友
            int result = friend.AddFriend(friendInfo);
            if(result == 1){
                //返回好友名
                friendInfo.setFriendName("1");
                outFriendInfo.writeObject(friendInfo);
            }else{
                //返回空好友名
                friendInfo.setFriendName("");
                outFriendInfo.writeObject(friendInfo);
            }
            outFriendInfo.close();
        } catch (IOException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //返回好友列表
    void FriendList(){
        try {
            Friend[] friendList=getFriendList(friendInfo.getUserID());
            outFriendInfo=new ObjectOutputStream(client.getOutputStream());
            outFriendInfo.writeObject(friendList);
            outFriendInfo.flush();
            inFriendInfo.close();
            outFriendInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //获取好友列表
    Friend[] getFriendList(String UserID){
        FriendDao friend=new FriendDao();
        ArrayList friendArray=friend.GetFriendList(UserID);
        Friend[] friendList=new Friend[friendArray.size()];
        for(int i=0;i<friendArray.size();i++){
            friendList[i]=(Friend)friendArray.get(i);
            System.out.println("好友名:"+friendList[i].getFriendName()+"\n好友ID:"+friendList[i].getFriendID()+
                        "\n好友类型："+friendList[i].getFriendType()+"\n用户ID:"+friendList[i].getUserID()+"\n------------------------------");
        }
        return friendList;
    }
    
    //删除好友
    void DeleteFriend(){
        PrintWriter returnInfo=null;
        try {
            FriendDao friend=new FriendDao();
            //返回请求接收与否的信息
            returnInfo = new PrintWriter(client.getOutputStream());
            if(friend.DelectFriend(friendInfo)){
                returnInfo.write(1);
            }else{
                returnInfo.write(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            returnInfo.close();
        }
    }
}

class ChatThread extends Thread{
    private Socket client=null;
    private ObjectOutputStream outChatInfo=null;
    private ObjectInputStream inChatInfo=null;
    private ChatList chatList=null;
    public ChatThread(Socket s){
        client=s;
        start();
    }
    public void run(){
        try {
            inChatInfo=new ObjectInputStream(client.getInputStream());
            chatList=(ChatList)inChatInfo.readObject();
            switch(chatList.getRequest()){
                case 1:
                    SentChatMess();
                    break;
                case 2:
                    GetNewMess();
                    break;
                case 3:
                    GetAllMess();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //发送信息
    void SentChatMess(){
        try {
            ChatDao chatDao=new ChatDao();
            outChatInfo=new ObjectOutputStream(client.getOutputStream());
            if(chatDao.UpdateChatList(chatList.getUserID(), chatList.getFriendID(), chatList.getMessages())){
               chatList.setRequest(666);
               outChatInfo.writeObject(chatList);
               outChatInfo.flush();
            }else{
                chatList.setRequest(111);
                outChatInfo.writeObject(chatList);
                outChatInfo.flush();
            }
            inChatInfo.close();
            outChatInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //接收新信息
    void GetNewMess(){
         try {
            ChatDao chatDao=new ChatDao();
            outChatInfo=new ObjectOutputStream(client.getOutputStream());
            ArrayList chatArr=chatDao.NewChatList(chatList.getUserID(), chatList.getFriendID());
            ChatList[] reChatList=null;
            if(chatArr!=null){
                reChatList=new ChatList[chatArr.size()];
                for (int i = 0; i < chatArr.size(); i++) {
                    reChatList[i]=(ChatList)chatArr.get(i);
                }
            }else{
                reChatList=new ChatList[1];
                reChatList[0]=new ChatList();
                reChatList[0].setRequest(0);
            }
            outChatInfo.writeObject(reChatList);
            outChatInfo.flush();
            inChatInfo.close();
            outChatInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //接收整个信息列表
    void GetAllMess(){
        try {
            ChatDao chatDao=new ChatDao();
            outChatInfo=new ObjectOutputStream(client.getOutputStream());
            ArrayList chatArr=chatDao.AllChatList(chatList.getUserID(),chatList.getFriendID());
            ChatList[] reChatList=null;
            if(chatArr!=null){
                reChatList=new ChatList[chatArr.size()];
                for (int i = 0; i < chatArr.size(); i++) {
                    reChatList[i]=(ChatList)chatArr.get(i);
                }
            }else{
                reChatList=new ChatList[1];
                reChatList[0]=new ChatList();
                reChatList[0].setRequest(0);
            }
            outChatInfo.writeObject(reChatList);
            outChatInfo.flush();
            inChatInfo.close();
            outChatInfo.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}