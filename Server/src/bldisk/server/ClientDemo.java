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
import java.net.InetSocketAddress;
import java.net.Socket;  
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;  
import java.util.logging.Logger; 
import java.io.Serializable;
import bldisk.entity.*;
public class ClientDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int select=5; //这里设置你想要测试的功能
		ClientToServer cts=new ClientToServer();
		switch(select){
		case 1:
			cts.Login();
			break;
		case 2:
			cts.SignUp();
			break;
		case 3:
			cts.Upload();
			break;
		case 4:
			cts.Download();
			break;
		case 5:
			cts.GetFileList();
			break;
		}
    } 
	
}

class ClientToServer{
	private Socket socket = null;  
	private ObjectOutputStream outFileInfo = null;  //对象输出流
	private ObjectInputStream inFileInfo = null;    //对象输入流
	private DataOutputStream outputFile = null;   //文件输出流
	private DataInputStream inputFile=null;  	//文件输入流
	private File openFile=null;  //打开文件资源
	private byte[] Buff=null;  //写入读出文件缓存
	private int length=0;  //读出写入长度
	private int fileSize=0; //文件大小
	private FileInfo fileInfo=null; //文件对象
	private UserInfo userInfo=null; //用户对象 
	private PrintWriter requestInfo=null;//文本输出流
	private BufferedReader returnInfo=null; //文本输入流
	
    
	//登录方法
	void Login(){ 
        try {  
        	//服务请求
        	if(Request(1)){
	            //读取服务器发过来的数据
	            outFileInfo = new ObjectOutputStream(socket.getOutputStream());
	            //创建User对象
	            userInfo=new UserInfo();
	            userInfo.setUserID("150202102365");
	            userInfo.setUserPass("123");
	            //设置为登录操作
	            userInfo.setType(1);
	            //发送对象到服务器
	            outFileInfo.writeObject(userInfo);  
	            outFileInfo.flush();             
	            System.out.println("1");
	            inFileInfo = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
	            Object obj = inFileInfo.readObject();
	            System.out.println("2");
	            if (obj != null) {  
	                userInfo = (UserInfo)obj;  
	                System.out.println(userInfo.toString());
	                if(userInfo.getCheck()==1){
	                	//如果登录成功就先获取下根目录信息
	                	FileList[] fileList=(FileList[])inFileInfo.readObject();
	                	for (int i = 0; i < fileList.length-1; i++) {
							System.out.println("文件名："+fileList[i].getFileName());
							System.out.println("文件大小："+fileList[i].getFileSize());
							System.out.println("文件最后修改日期："+fileList[i].getLastModified());
							System.out.println("是否是文件夹："+fileList[i].isIsDirectory());
							System.out.println("-------------------------------");
						}
	                	System.out.println("登录成功");
	                }else{
	                	System.out.println("登录失败，您的用户账号错误或登录密码错误");
	                }
	            }else{
	            	System.out.println("登录失败，您的用户账号错误或登录密码错误");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//注册方法
	void SignUp(){
        try {  
        	//服务请求
        	if(Request(1)){
	            //读取服务器发过来的数据
	            outFileInfo = new ObjectOutputStream(socket.getOutputStream());
	            //创建User对象
	            userInfo=new UserInfo();
	            userInfo.setUserID("1001");
	            userInfo.setUserName("林智杰");
	            userInfo.setUserPass("123");
	            userInfo.setUserMail("543541941@qq.com");
	            //设置为注册操作
	            userInfo.setType(2);
	            //发送对象到服务器
	            outFileInfo.writeObject(userInfo);  
	            outFileInfo.flush();  
	            //接收服务器返回的数据
	            inFileInfo = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
	            Object obj = inFileInfo.readObject();  
	            if (obj != null) {  
	                userInfo = (UserInfo)obj;  
	                System.out.println(userInfo.toString());
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
	void Upload(){    
        try {
        	//服务请求
        	if(Request(2)){
	        	//打开文件资源
	            openFile = new File("C:/Users/Administrator/Desktop/417作业.zip"); 
	            //获取文件长度，最后可以判断文件传输是否完整
	            long l = openFile.length();            
	            //发送文件信息
	            outFileInfo=new ObjectOutputStream(socket.getOutputStream());
	            //设置文件信息
	            fileInfo=new FileInfo();
	            fileInfo.setUserID("150202102365");
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
	            System.out.println(fileInfo.toString());
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
	void Download(){
		 try { 
        	 //服务请求
        	 if(Request(2)){
				 //创建输出流
				 outFileInfo=new ObjectOutputStream(socket.getOutputStream());
				 //设置请求文件信息
		         fileInfo=new FileInfo();
				 fileInfo.setFileName("test.jpg");
				 //设置文件目录，要精确到文件名
				 fileInfo.setFilePath("/test.jpg");
				 //设置为2表示下载请求
				 fileInfo.setFileRequest(2);
				 fileInfo.setRuturnInfo(0);
				 fileInfo.setUserID("150202102365");
				 //向服务器发送请求文件信息
				 outFileInfo.writeObject(fileInfo);
				 outFileInfo.flush();
				 //创建接收服务器发送过来的文件信息的接收流
				 inFileInfo=new ObjectInputStream(socket.getInputStream());
				 fileInfo=(FileInfo)inFileInfo.readObject();
				 //创建下载接收流
				 inputFile=new DataInputStream(socket.getInputStream());
				 //打开文件资源
				 outputFile = new DataOutputStream(new FileOutputStream("C:/Users/Administrator/Desktop/test.jpg"));      
		         Buff = new byte[1024];
		         //开始传输文件
		         length=inputFile.read(Buff,0,Buff.length);
		         while(length>0){
		        	 outputFile.write(Buff, 0,length);
		        	 outputFile.flush();
		        	 fileSize+=length;
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
	
	//获取文件列表
	void GetFileList(){
		if(Request(2)){
			//创建输出流
			 try {
				outFileInfo=new ObjectOutputStream(socket.getOutputStream());
				 //设置请求文件信息
		         fileInfo=new FileInfo();
				 //设置请求的文件目录
				 fileInfo.setFilePath("/");
				 //设置为3表示获取文件目录
				 fileInfo.setFileRequest(3);
				 fileInfo.setRuturnInfo(0);
				 fileInfo.setUserID("150202102365");
				 //向服务器发送请求文件信息
				 outFileInfo.writeObject(fileInfo);
				 outFileInfo.flush();
				 //接收服务器发过来的文件列表
				 inFileInfo=new ObjectInputStream(socket.getInputStream());
				 FileList[] fileList=(FileList[])inFileInfo.readObject();
             	 for (int i = 0; i < fileList.length-1; i++) {
						System.out.println("文件名："+fileList[i].getFileName());
						System.out.println("文件大小："+fileList[i].getFileSize());
						System.out.println("文件最后修改日期："+fileList[i].getLastModified());
						System.out.println("是否是文件夹："+fileList[i].isIsDirectory());
						System.out.println("-------------------------------");
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
	
	//操作请求
	boolean Request(int select){
		try {
			socket=new Socket("localhost",6666);
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
			