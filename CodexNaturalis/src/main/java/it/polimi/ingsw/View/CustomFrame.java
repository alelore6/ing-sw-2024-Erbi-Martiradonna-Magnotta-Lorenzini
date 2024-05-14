package it.polimi.ingsw.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CustomFrame extends JFrame {

    int x;
    int y;
    double scale;
    private String fileToPrintPath = null;

    public CustomFrame(String s) {
        super(s);
    }

    public void paint(Graphics g) {
        //TODO paint method is called automatically when the frame is istantiated
        // we have to understand how and when to call it correctly. There is the "repaint()" method
        // that will be used to "refresh" the GUI accordingly, recalling the paint() method.

        printRectangle(g);
        printCard(g);
    }

    private void printRectangle(Graphics g){
        g.drawString("Hello", 200, 50);
        int X = 200;
        int Y = 100;
        int rectwidth = 50;
        int rectheight = 100;
        g.setColor(Color.red);
        g.drawRect(X, Y, rectwidth, rectheight);
    }

    private void printCard(Graphics g){
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream(fileToPrintPath);
        BufferedImage img= null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int width = (int) (993*scale);
        int height = (int) (width /1.5); // 1.5 is the aspect ratio of a card
        g.drawImage(img, 30,30, width, height, null);
    }
    public void setPrintPath(String s){
        this.fileToPrintPath = s;
    }

    public void setCoord(int x, int y, double scale){
        this.x = x;
        this.y = y;
        this.scale = scale;
    }
}
