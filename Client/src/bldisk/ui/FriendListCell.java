package bldisk.ui;

import bldisk.entity.Friend;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class FriendListCell extends DefaultListCellRenderer{
    
    private Friend[] data;
    
    public FriendListCell(Friend[] data){
        this.data = data;
    }
    
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
        Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        if (index >= 0 && index < data.length){
            Friend friend = data[index];
            setIcon(new ImageIcon("src/image/user.png"));
            setText(friend.getFriendID());
        }
        if (isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
    
}
