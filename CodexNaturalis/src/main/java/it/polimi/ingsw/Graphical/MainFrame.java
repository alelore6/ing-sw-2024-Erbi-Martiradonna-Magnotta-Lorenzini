package it.polimi.ingsw.Graphical;

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
   int numberOfPLayers;
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
        initialPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponents(g);
                ImageIcon photo = new ImageIcon("assets/image/rulebook/01.png");
                Image image = photo.getImage();
                g.drawImage(image, x, y, getWidth(), getHeight(), this);
            }
        };


    }
    public void Ui(){
        numberOfPLayers=getNumberOfPLayers();
        GenerationPanels();
    }

    public int getNumberOfPLayers() {
        String input= JOptionPane.showInputDialog("Enter number of players: ", numberOfPLayers);
        int players=0;
        try {
            players=Integer.parseInt(input);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and "+numberOfPLayers);
        }
        return numberOfPLayers;
    }

    public void paint(Graphics g) {
        //TODO paint method is called automatically when the frame is istantiated
        // we have to understand how and when to call it correctly. There is the "repaint()" method
        // that will be used to "refresh" the GUI accordingly, recalling the paint() method.

        printRectangle(g);
        printCard(g);

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




    public void GenerationPanels(){
    /**
        mainPanel = new JPanel(new BorderLayout());

    tableCenterPanel= new JPanel();
    handPanel = new JPanel();
    positionedCardPanel = new JPanel();

       menuBar = new JMenuBar();
       JMenu tableCenterMenu = new JMenu("Table Center");
       JMenuItem tableCenter = new JMenuItem("Show Table Center");
       tableCenterMenu.add(tableCenter);

       JMenu positionedCardsMenu = new JMenu("Positioned Cards");
       JMenuItem positionedCards = new JMenuItem("Positioned Cards");
       for (int i = 0; i < numberOfPLayers + 2; i++) {
           JMenuItem hand = new JMenuItem("Hand " + (i + 1));
           menuBar.add(hand);
       }

       menuBar.add(tableCenter);
       menuBar.add(positionedCards);

       tableCenter.addActionListener( new ActionEvent) {
          public void actionPerformed(ActionEvent ){
        switchPanel(tableCenterPanel);
         }
       }

       positionedCards.addActionListener(ActionEvent) {
           public void actionPerformed(ActionEvent ){
               switchPanel(positionedCardPanel);
         }
       }
       setJMenuBar(menuBar);

      setVisible(true);
**/
   }

   private void switchPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
   }

public void reactstartGame(){
        remove (initialPanel);
        setVisible(true);
        revalidate();
        repaint();
}

}
