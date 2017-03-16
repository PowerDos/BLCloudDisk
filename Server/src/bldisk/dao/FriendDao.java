package bldisk.dao;

import bldisk.db.DBConn;
import bldisk.entity.Friend;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class FriendDao {
    private PreparedStatement pstmt;
    private Connection Conn;
    private ResultSet rst;
    private String sql;
    //获取好友列表
    public ArrayList GetFriendList(String UserID){
        Conn=DBConn.Conn();
        ArrayList friendList=new ArrayList();
        int i=0;
        sql="select * from FriendInfo where UserID=?";
        try {
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,UserID);
            rst=pstmt.executeQuery();
            while(rst.next()){
                Friend friend=new Friend();
                friend.setFriendType(rst.getObject(2).toString());
                friend.setFriendID(rst.getObject(3).toString());
                friend.setFriendName(rst.getObject(4).toString());
                friendList.add(friend);
                i++;
            }
            return friendList;
        } catch (SQLException ex) {
            Logger.getLogger(FriendDao.class.getName()).log(Level.SEVERE, null, ex);
        }
       return friendList; 
    }
    //添加好友
    public int AddFriend(Friend friend){
        Conn=DBConn.Conn();
        //获取时间
        java.util.Date utilDate=new Date();
        java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
        try {
            sql="insert into FriendInfo values(?,?,?,?,?)";
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,friend.getUserID());
            pstmt.setString(2,"我的好友");
            pstmt.setString(3,friend.getFriendID());
            pstmt.setString(4," ");
            pstmt.setDate(5, sqlDate);
            if(pstmt.executeUpdate()==1){
                pstmt.close();
                PreparedStatement stmt2 = Conn.prepareStatement(sql);
                stmt2.setString(1,friend.getFriendID());
                stmt2.setString(2,"我的好友");
                stmt2.setString(3,friend.getUserID());
                stmt2.setString(4," ");
                stmt2.setDate(5, sqlDate);
                if(stmt2.executeUpdate()==1){
                    stmt2.close();
                    return 1;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    //由于暂时只用账号标识用户，所以不返回名称
//    public String AddFriend(Friend friend){
//        Conn=DBConn.Conn();
//        //获取时间
//        java.util.Date utilDate=new Date();
//        java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
//        try {
//            String FriendName=SearchUser(friend.getFriendID());
//            if(!FriendName.equals(" ")){
//                sql="insert into FriendInfo values(?,?,?,?)";
//                pstmt=Conn.prepareStatement(sql);
//                pstmt.setString(1,friend.getUserID());
//                pstmt.setString(2,"我的好友");
//                pstmt.setString(3,friend.getFriendID());
//                pstmt.setDate(4, sqlDate);
//                if(pstmt.executeUpdate()==1){
//                    return FriendName;
//                }
//                pstmt.close();
//            }else{
//                return " ";
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(FriendDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    //删除好友
    public boolean DelectFriend(Friend friend){
        Conn=DBConn.Conn();
        try {
            sql="delete from FriendInfo where UserID=? and FriendID=?";
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1,friend.getUserID());
            pstmt.setString(2,friend.getFriendID());
            if(pstmt.executeUpdate()==1){
                return true;
            }
            pstmt.close();
            Conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(FriendDao.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return false;
    }
    
    String SearchUser(String UserID){
         Conn=DBConn.Conn();
         sql="select UserID from UserInfo where UserID=?";
        try {
            pstmt=Conn.prepareStatement(sql);
            pstmt.setString(1, UserID);
            rst=pstmt.executeQuery();
            while(rst.next()){
                System.out.println("here1:"+rst.getObject(1).toString());
                return rst.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return " ";
    }
}
