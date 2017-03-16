package bldisk.dao;

import bldisk.db.DBConn;
import bldisk.entity.ChatList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class ChatDao {
    private PreparedStatement pstmt;
    private Connection Conn;
    private ResultSet rst;
    private String Sql;
    //查看未读信息
    private int UnreadNum(String UserID,String FriendID){
        try {
            Conn=DBConn.Conn();
            Sql="select * from UnreadMess where UserID=? and FriendID=?";
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1, UserID);
            pstmt.setString(2, FriendID);
            rst=pstmt.executeQuery();
            if(rst.next()){
                return Integer.parseInt(rst.getObject(3).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    //查看所有聊天列表
    public ArrayList AllChatList(String UserID,String FriendID){
        try {
            Conn=DBConn.Conn();
            Sql="select * from ChatList where (UserID = ? and FriendID = ?) or (UserID = ? and FriendID = ?) order by ChatTime";
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1,UserID);
            pstmt.setString(2,FriendID);
            pstmt.setString(3,FriendID);
            pstmt.setString(4,UserID);
            rst=pstmt.executeQuery();
            ArrayList chatList=new ArrayList();
            while(rst.next()){
                ChatList temp=new ChatList();
                temp.setUserID(rst.getObject(1).toString());
                temp.setFriendID(rst.getObject(2).toString());
                temp.setMessages(rst.getObject(3).toString());
                temp.setCharTime(rst.getObject(4).toString());
                temp.setRequest(666);
                chatList.add(temp);
            }
            return chatList;
        } catch (SQLException ex) {
            Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //获取新信息
    //因为客户端已经存在发送的信息，所以获取的新信息只获取朋友发过来的信息
    public ArrayList NewChatList(String UserID,String FriendID){
        int unreadNum=UnreadNum(UserID,FriendID);
        if(unreadNum>0){
            try {
                Sql="select top "+unreadNum+" * from ChatList Where UserID=? and FriendID=? order by ChatTime desc";
                Conn=DBConn.Conn();
                pstmt=Conn.prepareStatement(Sql);
                pstmt.setString(1, UserID);
                pstmt.setString(2, FriendID);
                rst=pstmt.executeQuery();
                ArrayList chatList=new ArrayList();
                while(rst.next()){
                    ChatList temp=new ChatList();
                    temp.setUserID(rst.getObject(1).toString());
                    temp.setFriendID(rst.getObject(2).toString());
                    temp.setMessages(rst.getObject(3).toString());
                    temp.setCharTime(rst.getObject(4).toString());
                    temp.setRequest(666);
                    chatList.add(temp);
                }
                //更新未读信息
                SetZeroUnreadMess(UserID,FriendID);
                return chatList;
            } catch (SQLException ex) {
                Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    //更新发送的信息
    public boolean UpdateChatList(String UserID,String FriendID,String ChatMess){
        Sql="insert into ChatList values(?,?,?,?)";
        Conn=DBConn.Conn();
        try {
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1, UserID);
            pstmt.setString(2, FriendID);
            pstmt.setString(3, ChatMess);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            if(pstmt.executeUpdate()==1){
                UpdateUnreadMess(UserID,FriendID);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    //更新未读信息表
    private boolean UpdateUnreadMess(String UserID,String FriendID){
        Sql="update UnreadMess set UnreadNum=UnreadNum+1 where UserID=? and FriendID=?";
        Conn=DBConn.Conn();
        try {
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1, UserID);
            pstmt.setString(2, FriendID);
            if(pstmt.executeUpdate()==1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    //置零未读信息表
    private boolean SetZeroUnreadMess(String UserID,String FriendID){
        Sql="update UnreadMess set UnreadNum=0 where UserID=? and FriendID=?";
        Conn=DBConn.Conn();
        try {
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1, UserID);
            pstmt.setString(2, FriendID);
            if(pstmt.executeUpdate()==1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
