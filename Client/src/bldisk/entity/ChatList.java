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
public class ChatList implements Serializable{
    private String UserID;
    private String FriendID;
    private String Messages;
    private String ChatTime;
    private int Request;

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
     * @return the Messages
     */
    public String getMessages() {
        return Messages;
    }

    /**
     * @param Messages the Messages to set
     */
    public void setMessages(String Messages) {
        this.Messages = Messages;
    }

    /**
     * @return the CharTime
     */
    public String getCharTime() {
        return ChatTime;
    }

    /**
     * @param CharTime the CharTime to set
     */
    public void setCharTime(String CharTime) {
        this.ChatTime = CharTime;
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
