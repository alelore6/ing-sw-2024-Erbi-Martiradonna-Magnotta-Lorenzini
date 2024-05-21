package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Events.StartGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MainFrame extends JFrame {

    int x;
    int y;
    double scale;
    private String fileToPrintPath = null;
    private ActionListener ActionEvent;
    private JLabel viewLabel;
    private JMenuBar menuBar;
    private JPanel initialPanel;
    private JPanel mainPanel;
    private JPanel tableCenterPanel;
    private JPanel handPanel;
    private JPanel positionedCardPanel;




    public MainFrame(String s) {
        super(s);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); //16:9 proporzione
        setLayout(new BorderLayout());
        ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/rulebook/01.png"));
        int width = 800;
        int height = 720;
        Image imgResized = img.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT);
        ImageIcon centerImgIcon = new ImageIcon(imgResized);
        this.add(new JLabel(img), BorderLayout.CENTER);

        initialPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponents(g);

                int x =( getWidth()-width)/2;
                int y = (getHeight()-height)/2;
                g.drawImage(imgResized, x, y, this);
            }
        };
        add(initialPanel, BorderLayout.CENTER);
        setVisible(true);
        initialPanel.revalidate();
        initialPanel.repaint();

        GenerationPanels(4);
    }



    public void paint(Graphics g) {
        //TODO paint method is called automatically when the frame is istantiated
        // we have to understand how and when to call it correctly. There is the "repaint()" method
        // that will be used to "refresh" the GUI accordingly, recalling the paint() method.

       /* printRectangle(g);
        printCard(g);
*/
    }

    private void printRectangle(Graphics g) {
        g.drawString("Hello", 900, 50);
        int X = 800;
        int Y = 100;
        int rectwidth = 50;
        int rectheight = 100;
        g.setColor(Color.red);
        g.drawRect(X, Y, rectwidth, rectheight);
    }

    private void printCard(Graphics g) {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream(fileToPrintPath);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int width = (int) (993 * scale);
        int height = (int) (width / 1.5); // 1.5 is the aspect ratio of a card
        g.drawImage(img, x, y, width, height, null);
    }

    public void setPrintPath(String s) {
        this.fileToPrintPath = s;
    }

    public void setCoord(int x, int y, double scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }


    public void GenerationPanels(int NumberOfPLayers){

        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        tableCenterPanel= new JPanel();
        tableCenterPanel.setBackground(Color.RED);
        handPanel = new JPanel();
        handPanel.setBackground(Color.CYAN);
        positionedCardPanel = new JPanel();
        positionedCardPanel.setBackground(Color.YELLOW);

        menuBar = new JMenuBar();
        JMenuItem tableCenter = new JMenuItem("Table Center");
        JMenuItem hand = new JMenuItem("Hand");

        menuBar.add(tableCenter);
        menuBar.add(hand);

        for (int i = 0; i < NumberOfPLayers; i++) {
            JMenuItem positionedCards = new JMenuItem("Positioned Cards Player " + (i+1));
            menuBar.add(positionedCards);
            positionedCards.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switchPanel(positionedCardPanel);


                }
            });
        }
        tableCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchPanel(tableCenterPanel);

            }
        });

        hand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e ) {
                switchPanel(handPanel);
            }
        });

        setJMenuBar(menuBar);
        setVisible(true);
    }

    private void switchPanel(JPanel panel) {

        Container currentPanel = getContentPane();
        mainPanel.remove(currentPanel.getComponent(0));
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void reactstartGame(StartGame ev){
        GenerationPanels(ev.model.numPlayers);
        remove (initialPanel);
        add(mainPanel , BorderLayout.CENTER);
        setVisible(true);
        initialPanel.revalidate();
        initialPanel.repaint();
    }

}
