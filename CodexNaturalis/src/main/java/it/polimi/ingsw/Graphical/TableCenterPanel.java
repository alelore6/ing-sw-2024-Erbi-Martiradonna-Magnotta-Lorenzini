package it.polimi.ingsw.Graphical;


import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableCenterPanel extends JSplitPane {
    private final GameView gameView;


    public TableCenterPanel(GameView gameView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;

        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());

        this.setDividerLocation(1140);
        createRightPanel().setMinimumSize(new Dimension(390, 300));
        createLeftPanel().setMinimumSize(new Dimension(1000, 300));
    }

    private JPanel createRightPanel() {

        ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/plateau/plateau.png"));
        int originalWidth = img.getIconWidth();
        int originalHeight = img.getIconHeight();
        int width = 900;
        int height = 700;
        double widthproportion = (double) width / originalWidth;
        double heightproportion = (double) height / originalHeight;
        double proportion =Math.min(widthproportion,heightproportion);

        double finalwidth =  proportion * originalWidth;
        double finalheight =  proportion * originalHeight;


        Image imgResized = img.getImage().getScaledInstance((int) finalwidth, (int) finalheight,Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(imgResized);
        JPanel rightPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int  x= (int) ((getWidth()- finalwidth)/2);
                int  y  = (int) ((getHeight()- finalheight)/2);
                g.drawImage(resizedIcon.getImage(), x,y, this);
            }
        };
        rightPanel.setLayout(new BorderLayout());

        setVisible(true);

        return rightPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns

        // Decks
        addDeck(leftPanel, "Gold Deck", 40);
        addDeck(leftPanel, "Resource Deck", 5);

        // Gold Cards
        for (int i = 0; i < 2; i++) {
            addCardSpot(leftPanel, "Gold Card ", 40 + i);
        }

        // Resource Cards
        for (int i = 0; i < 2; i++) {
            addCardSpot(leftPanel, "Resource Card ", 5 + i);
        }

        return leftPanel;
    }

    private void addDeck(JPanel panel, String title,int cardID) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        deckLabel.setHorizontalAlignment(JLabel.CENTER);
        deckLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));

        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {

            System.out.println("Draw from " + title);
        });

        deckPanel.add(drawButton, BorderLayout.SOUTH);

        panel.add(deckPanel);
    }

    private void addCardSpot(JPanel panel, String title, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel cardLabel = new JLabel();
        cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));

        cardPanel.add(cardLabel, BorderLayout.CENTER);
      JButton drawButton = new JButton("Draw");
      drawButton.addActionListener(e ->{
          System.out.println("Draw from " + title);
              });
      cardPanel.add(drawButton, BorderLayout.SOUTH);

        panel.add(cardPanel);
    }


    private ImageIcon getImageIcon(String path, int scaleX, int scaleY) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// full screen
        String[] playerNames = {"1","2","3","4"};
       Game game =new Game(4,playerNames,null);
        GameView gameView= new GameView(game);
        TableCenterPanel panel= new TableCenterPanel(gameView);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void update(GameView gameView) {

    }
}
