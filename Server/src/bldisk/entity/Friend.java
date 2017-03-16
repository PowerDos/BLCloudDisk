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
public class Friend implements Serializable{
    private String FriendID;
    private String FriendName;
    private String FriendType;
    private String UserID;
    private String Content;
    private int Request;
    public Friend(){
        FriendID=null;
        FriendName=null;
        FriendType=null;
        UserID=null;
        Content=null;
        Request=0;
    }

    /**
     * @return the FriendID
     */
    public String getFriendID() {
        return FriendID;
    }

    /**
     * @param FriendID the FriendID to set
     */
    public void setFriendID(String FriendID) {
        this.FriendID = FriendID;
    }

    /**
     * @return the FriendName
     */
    public String getFriendName() {
        return FriendName;
    }

    /**
     * @param FriendName the FriendName to set
     */
    public void setFriendName(String FriendName) {
        this.FriendName = FriendName;
    }

    /**
     * @return the FriendType
     */
    public String getFriendType() {
        return FriendType;
    }

    /**
     * @param FriendType the FriendType to set
     */
    public void setFriendType(String FriendType) {
        this.FriendType = FriendType;
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
     * @return the Content
     */
    public String getContent() {
        return Content;
    }

    /**
     * @param Content the Content to set
     */
    public void setContent(String Content) {
        this.Content = Content;
    }

    /**
     * @return the Request
     */
    public int getRequest() {
        return Request;
    }

    /**
     * @param Request the Request to set
     */
    public void setRequest(int Request) {
        this.Request = Request;
    }
    
}
