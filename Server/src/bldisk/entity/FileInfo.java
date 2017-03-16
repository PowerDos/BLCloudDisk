/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bldisk.entity;
import java.io.Serializable;
/**
 *
 * @author Administrator
 */
public class FileInfo implements Serializable{
    private String FileName;
    private long FileSize;
    private String FilePath;
    private int FileRequest;
    private int RuturnInfo;
    private String UserID;

    public FileInfo(){
        FileName=null;
        FileSize=0;
        FilePath=null;
        FileRequest=0;
        RuturnInfo=0;
        UserID=null;
    }

    /**
     * @return the FileName
     */
    public String getFileName() {
        return FileName;
    }

    /**
     * @param FileName the FileName to set
     */
    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    /**
     * @return the FileSize
     */
    public long getFileSize() {
        return FileSize;
    }

    /**
     * @param FileSize the FileSize to set
     */
    public void setFileSize(long FileSize) {
        this.FileSize = FileSize;
    }

    /**
     * @return the FilePath
     */
    public String getFilePath() {
        return FilePath;
    }

    /**
     * @param FilePath the FilePath to set
     */
    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    /**
     * @return the FileRequest
     */
    public int getFileRequest() {
        return FileRequest;
    }

    /**
     * @param FileRequest the FileRequest to set
     */
    public void setFileRequest(int FileRequest) {
        this.FileRequest = FileRequest;
    }

    /**
     * @return the RuturnInfo
     */
    public int getRuturnInfo() {
        return RuturnInfo;
    }

    /**
     * @param RuturnInfo the RuturnInfo to set
     */
    public void setRuturnInfo(int RuturnInfo) {
        this.RuturnInfo = RuturnInfo;
    }

    /**
     * @return the UserID
     */
    public String getUserID() {
        return UserID;
    }

    /**
     * @param UserID the UserID to set
     */
    public void setUserID(String UserID) {
        this.UserID = UserID;
    }
  
}