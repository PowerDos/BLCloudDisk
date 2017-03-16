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
public class FileList implements Serializable{
    private String fileName; //文件名
    private long fileSize; //文件大小
    private String lastModified; //文件最后修改日期
    private boolean isDirectory; //是否为目录
    public FileList(){
        fileName=null;
        fileSize=0;
        lastModified=null;
        isDirectory=false;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the lastModified
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return the isDirectory
     */
    public boolean isIsDirectory() {
        return isDirectory;
    }

    /**
     * @param isDirectory the isDirectory to set
     */
    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

}
