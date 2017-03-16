/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bldisk.dao;
import bldisk.db.DBConn;
import bldisk.entity.UserInfo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @author Administrator
 */
public class LoginDao {
    private PreparedStatement pstmt;
    private Connection Conn;
    private ResultSet rst;
    private String sql;
    public UserInfo CheckLogin(String UserID,String UserPass){
        sql="select * from UserInfo where UserID=? and UserPass=?";
        Conn=DBConn.Conn();
        UserInfo user=new UserInfo();
        user.setCheck(0);
        try {
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,UserID);
            pstmt.setString(2,UserPass);
            rst=pstmt.executeQuery();
            if(rst.next()){
                user.setUserID(rst.getObject(1).toString());
//                user.setUserName(rst.getObject(2).toString());
                user.setUserHead(rst.getObject(4).toString());
                user.setUserLevel(Integer.parseInt(rst.getObject(5).toString()));
                user.setUserMemorySize(rst.getObject(6).toString());
                user.setUserUsedSize(rst.getObject(7).toString());
                user.setIsAdmin(Integer.parseInt(rst.getObject(8).toString()));
                user.setPrivateStatus(Integer.parseInt(rst.getObject(12).toString()));
                user.setCheck(1);
                Conn.close();
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    public UserInfo GetInfo(String UserID){
        sql="select * from UserInfo where UserID=?";
        Conn=DBConn.Conn();
        UserInfo user=new UserInfo();
        user.setCheck(0);
        try {
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,UserID);
            rst=pstmt.executeQuery();
            if(rst.next()){
                user.setUserID(rst.getObject(1).toString());
                user.setUserMemorySize(rst.getObject(6).toString());
                user.setUserUsedSize(rst.getObject(7).toString());
                user.setCheck(1);
                Conn.close();
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    public boolean ExistUser(String UserID){
        Conn=DBConn.Conn();
        sql="select * from UserInfo where UserID=?";
        try {
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,UserID);
            rst=pstmt.executeQuery();
            if(rst.next()){
                Conn.close();
                return false; 
            }else{
                Conn.close();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public UserInfo SignUp(String UserID,String UserName,String UserPass,String UserMail){
        Conn=DBConn.Conn();
        sql="insert into UserInfo(UserID,UserName,UserPass,UserMail,CTime) values(?,?,?,?,?)";
        UserInfo user=new UserInfo();
        user.setCheck(0);
        try {
            //获取日期
            java.util.Date utilDate=new Date();
            java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,UserID);
            pstmt.setString(2,UserName);
            pstmt.setString(3,UserPass);
            pstmt.setString(4,UserMail);
            pstmt.setDate(5,sqlDate);
            int i=pstmt.executeUpdate();
            if(i==1){
                System.out.println("注册成功");
                user.setUserID(UserID);
                user.setUserName(UserName);
                user.setUserHead("defaultHead.jpg");
                user.setUserLevel(1);
                user.setUserMemorySize("1024M");
                user.setUserUsedSize("0");
                user.setIsAdmin(0);
                user.setPrivateStatus(0);
                user.setCheck(1);
                Conn.close();
                return user;
            }else{
                System.out.println("插入失败");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
//    private String PassToMD5(String Pass){
//        try {
//            // 获取MD5算法实例
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            //开始加密
//            md.update(Pass.getBytes());
//            //得到加密结果
//            return md.digest().toString();
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
}
