package it.polimi.ingsw.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CustomFrame extends JFrame {

    public CustomFrame(String s) {
        super(s);
    }

    public void paint(Graphics g) {
        //TODO paint method is called automatically when the frame is istantiated
        // we have to understand how and when to call it correctly, for instance:
        // to print a specific card and when to print it. There is the "repaint()" method
        // that will be used to "refresh" the GUI accordingly, recalling the paint() method.

        g.drawString("Hello", 200, 50);
        int x = 200;
        int y = 100;
        int rectwidth = 50;
        int rectheight = 100;
        g.setColor(Color.red);
        g.drawRect(x, y, rectwidth, rectheight);

        //PLACEHOLDER TEST
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream("assets/images/cards/front/012.png"); //PLACEHOLDER TEST
        BufferedImage img= null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        double scale = 0.10;
        int widthCard = (int) (993*scale);
        int heightCard = (widthCard*2)/3;
        g.drawImage(img, 30,30, widthCard,heightCard, null);
        //PLACEHOLDER TEST
    }
}
