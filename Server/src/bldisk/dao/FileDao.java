/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bldisk.dao;
import bldisk.db.DBConn;
import bldisk.entity.FileInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Administrator
 */
public class FileDao {
    private PreparedStatement pstmt;
    private Connection Conn;
//    private ResultSet rst;
    public boolean UploadFile(FileInfo file){
        Conn=DBConn.Conn();
        String Sql="insert into FileInfo values(?,?,?,?,?,?)";
        try {
            //获取时间
            java.util.Date utilDate=new Date();
            java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1,file.getUserID());
            pstmt.setString(2, file.getFileName());
            pstmt.setString(3,file.getFilePath());
            pstmt.setDate(4,sqlDate);
            pstmt.setString(5,null);
            pstmt.setInt(6,0);
            if(pstmt.executeUpdate()==1){               
                if(UpdateLog(file.getUserID(),sqlDate+" 上传文件 "+file.getFileName()+" 到"+file.getFilePath(),sqlDate)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean deleteFile(FileInfo file){
        Conn=DBConn.Conn();
        String Sql="delete from FileInfo where UserID = ? and UserFileName = ? and FilePath = ?";
        try {
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1,file.getUserID());
            pstmt.setString(2, file.getFileName());
            pstmt.setString(3,file.getFilePath());
            pstmt.setInt(6,0);
            if(pstmt.executeUpdate()==1){               
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private boolean ExistFile(){
        int i=1;
        return false;
    }
    //更新日志表
    private boolean UpdateLog(String UserID,String DO,java.sql.Date sqlDate){
        String Sql="insert into UpdateLog values(?,?,?)";
        try {
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1,UserID);
            pstmt.setString(2,DO);
            pstmt.setDate(3,sqlDate);
            if(pstmt.executeUpdate()==1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //更新下载次数
    private boolean UpdateDownNum(String UserID){
        Conn=DBConn.Conn();
        String Sql="update FileInfo set DownNum=DownNum+1 where UserID=?";
        try {
            //获取时间
            java.util.Date utilDate=new Date();
            java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
            pstmt=Conn.prepareStatement(Sql);
            pstmt.setString(1,UserID);
            if(pstmt.executeUpdate()==1){               
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
