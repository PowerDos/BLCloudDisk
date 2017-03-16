/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bldisk.db;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *连接数据库
 * @author 林智杰
 */
public class DBConn {
    private static final String url="jdbc:sqlserver://localhost:1433;DatabaseName=BLDisk";
    private static final String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String username="sa";
    private static final String password="zhbit";
    public static final Connection Conn(){
        Connection Conn=null;
        try {
            Class.forName(driver);
            Conn=DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return Conn;
    } 
}
