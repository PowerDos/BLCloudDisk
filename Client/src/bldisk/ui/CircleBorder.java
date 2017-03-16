package bldisk.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author XR_HUI
 */
public class CircleBorder implements Border{
    
    private int a;
    
    public CircleBorder(int a){
        this.a = a;
    }
    
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }
    public boolean isBorderOpaque() {
        return false;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y,
        int width, int height) {
            //使用黑色在组件的外边缘绘制一个圆角矩形
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, a,a);
    }
}
