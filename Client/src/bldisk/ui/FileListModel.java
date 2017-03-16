package bldisk.ui;

import java.util.Vector;
import javax.swing.AbstractListModel;
import bldisk.entity.*;

public class FileListModel extends AbstractListModel{

    private FileList[] data;
    
    public FileListModel(FileList[] data){
        this.data = data;
    }
    
    public int getSize() {
        return data.length;
    }
    
    public FileList getElementAt(int index) {
        FileList file = data[index];
        return file;
    }
    
}
