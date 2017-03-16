package bldisk.ui;

import bldisk.entity.Friend;
import javax.swing.AbstractListModel;

public class FriendListModel extends AbstractListModel{
    
    private Friend[] data;
    
    public FriendListModel(Friend[] data){
        this.data = data;
    }
    
    public int getSize() {
        return data.length;
    }
    
    public Object getElementAt(int index) {
        Friend friend = data[index];
        return friend;
    }    
    
}
