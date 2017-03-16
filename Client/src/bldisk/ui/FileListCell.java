package bldisk.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import bldisk.entity.*;

public class FileListCell extends DefaultListCellRenderer{
    
    private FileList[] data;
    
    public FileListCell(FileList[] data){
        this.data = data;
    }
    
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
        Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        if (index >= 0 && index < data.length - 1){
            FileList file = data[index];
            int pointIndex = file.getFileName().indexOf(".");
            if (pointIndex != -1){
                String fileType = file.getFileName().substring(file.getFileName().indexOf(".")+1);
                if (fileType.equals("txt") || fileType.equals("doc") || fileType.equals("xls") || fileType.equals("ppt") || fileType.equals("sql") || fileType.equals("java") || fileType.equals("c")){
                    setIcon(new ImageIcon("src/image/txtFile.png"));
                }else if (fileType.equals("mp4") || fileType.equals("rmvb") | fileType.equals("flv") | fileType.equals("avi")){
                    setIcon(new ImageIcon("src/image/videoFile.png"));
                }else if (fileType.equals("zip") || fileType.equals("rar")){
                    setIcon(new ImageIcon("src/image/rarFile.png"));
                }else if (fileType.equals("mp3") || fileType.equals("m4a")){
                    setIcon(new ImageIcon("src/image/musicFile.png"));
                }else if (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("jpeg") || fileType.equals("ico")){
                    setIcon(new ImageIcon("src/image/photoFile.png"));
                }else{
                    setIcon(new ImageIcon("src/image/blankFile.png"));
                }
            }else{
                setIcon(new ImageIcon("src/image/folder.png"));
            }
            setText(file.getFileName());
//            setText(file.getFileName() + "\r" +  file.getFileSize() + "\r" + file.getLastModified());
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