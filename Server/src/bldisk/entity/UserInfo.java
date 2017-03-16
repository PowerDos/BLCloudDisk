package bldisk.entity;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class UserInfo implements Serializable{
	private String UserID;
    private String UserName;
    private String UserPass;
    private int UserLevel;
    private String UserHead;
    private String UserMemorySize;
    private String UserUsedSize;
    private int PrivateStatus;
    private String PrivatePass;
    private String UserMail;
    private int IsAdmin;
    private int Type;
    private int Check;

    public UserInfo(){
    	UserID=null;
    	UserName=null;
    	UserPass=null;
    	UserLevel=1;
    	UserHead=null;
    	UserMemorySize=null;
    	UserUsedSize=null;
    	PrivateStatus=0;
    	PrivatePass=null;
    	UserMail=null;
    	IsAdmin=0;
    	Type=0;
    	Check=0;
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

    /**
     * @return the UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * @param UserName the UserName to set
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    /**
     * @return the UserPass
     */
    public String getUserPass() {
        return UserPass;
    }

    /**
     * @param UserPass the UserPass to set
     */
    public void setUserPass(String UserPass) {
        this.UserPass = UserPass;
    }

    /**
     * @return the UserLevel
     */
    public int getUserLevel() {
        return UserLevel;
    }

    /**
     * @param UserLevel the UserLevel to set
     */
    public void setUserLevel(int UserLevel) {
        this.UserLevel = UserLevel;
    }

    /**
     * @return the UserUsedSize
     */
    public String getUserUsedSize() {
        return UserUsedSize;
    }

    /**
     * @param UserUsedSize the UserUsedSize to set
     */
    public void setUserUsedSize(String UserUsedSize) {
        this.UserUsedSize = UserUsedSize;
    }

    /**
     * @return the PrivateStatus
     */
    public int getPrivateStatus() {
        return PrivateStatus;
    }

    /**
     * @param PrivateStatus the PrivateStatus to set
     */
    public void setPrivateStatus(int PrivateStatus) {
        this.PrivateStatus = PrivateStatus;
    }

    /**
     * @return the PrivatePass
     */
    public String getPrivatePass() {
        return PrivatePass;
    }

    /**
     * @param PrivatePass the PrivatePass to set
     */
    public void setPrivatePass(String PrivatePass) {
        this.PrivatePass = PrivatePass;
    }

    /**
     * @return the UserMail
     */
    public String getUserMail() {
        return UserMail;
    }

    /**
     * @param UserMail the UserMail to set
     */
    public void setUserMail(String UserMail) {
        this.UserMail = UserMail;
    }

    /**
     * @return the IsAdmin
     */
    public int getIsAdmin() {
        return IsAdmin;
    }

    /**
     * @param IsAdmin the IsAdmin to set
     */
    public void setIsAdmin(int IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    /**
     * @return the Type
     */
    public int getType() {
        return Type;
    }

    /**
     * @param Type the Type to set
     */
    public void setType(int Type) {
        this.Type = Type;
    }

    /**
     * @return the Check
     */
    public int getCheck() {
        return Check;
    }

    /**
     * @param Check the Check to set
     */
    public void setCheck(int Check) {
        this.Check = Check;
    }

    /**
     * @return the UserHead
     */
    public String getUserHead() {
        return UserHead;
    }

    /**
     * @param UserHead the UserHead to set
     */
    public void setUserHead(String UserHead) {
        this.UserHead = UserHead;
    }

    /**
     * @return the UserMemorySize
     */
    public String getUserMemorySize() {
        return UserMemorySize;
    }

    /**
     * @param UserMemorySize the UserMemorySize to set
     */
    public void setUserMemorySize(String UserMemorySize) {
        this.UserMemorySize = UserMemorySize;
    }
    public String toString(){
    	String str="UserID："+this.UserID+"\nUserName："+this.UserName+"\nUserMemorySize："+this.UserMemorySize;
    	str+="\nUserHead："+this.UserHead+"\nUserLevel："+this.UserLevel+"\nUserUsedSize："+this.UserUsedSize;
    	str+="\nIsAdmin："+this.IsAdmin+"\nUserMail："+this.UserMail+"\nPrivatePass："+this.PrivatePass;
    	str+="\nPrivateSatus"+this.PrivateStatus;
    	return str;
    }
    
}