package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Dimension;

public class TableCenterPanel extends JSplitPane {
    private GameView gameView;
    private ArrayList<JLabel> cardLabels;
    private JLabel[] deckLabels;
    private int[] cardsID;
    private int drawChoice;
    private boolean drawing = false;
    private JButton[] spots;
    private int[] spotXCoords = {/* coordinate */};
    private int[] spotYCoords = { /* coordinate */};
    private ArrayList<JButton> drawButtons;
    private JLabel cardLabel;
    private ImageIcon possiblePlayImage = null;
    private final Object drawLock;

    public TableCenterPanel(GameView gameView, Object drawLock) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;
        this.drawLock = drawLock;
        this.cardLabels = new ArrayList<>();
        this.deckLabels = new JLabel[2];
        this.cardsID = new int[4];
        this.spots = new JButton[27];
        this.drawButtons = new ArrayList<>();
        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
            possiblePlayImage = new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());

        this.setDividerLocation(860);
    }

    private JPanel createRightPanel() {
        ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/plateau/plateau.png"));
        int originalWidth = img.getIconWidth();
        int originalHeight = img.getIconHeight();
        int width = 900;
        int height = 700;
        double widthProportion = (double) width / originalWidth;
        double heightProportion = (double) height / originalHeight;
        double proportion = Math.min(widthProportion, heightProportion);

        double finalWidth = proportion * originalWidth;
        double finalHeight = proportion * originalHeight;

        Image imgResized = img.getImage().getScaledInstance((int) finalWidth, (int) finalHeight, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(imgResized);
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (int) ((getWidth() - finalWidth) / 2);
                int y = (int) ((getHeight() - finalHeight) / 2);
                g.drawImage(resizedIcon.getImage(), x, y, this);
            }
        };
        rightPanel.setLayout(new BorderLayout());

        setVisible(true);
        rightPanel.setMinimumSize(new Dimension(350, 300));

        return rightPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Decks
        addDeck(leftPanel, "Gold Deck", gameView.tableCenterView.topGoldCardColor, 0);
        addDeck(leftPanel, "Resource Deck", gameView.tableCenterView.topResourceCardColor, 1);

        for (int i = 0; i < 4; i++) {
            if (gameView.tableCenterView.centerCards[i] != null) {
                addCardSpot(leftPanel, i, gameView.tableCenterView.centerCards[i].getID());
            } else {
                addCardSpot(leftPanel, i, -1);
            }
        }

        if (gameView.tableCenterView.objCards[0] != null) {
            addObjectiveCardSpot(leftPanel, "Objective Card 1", gameView.tableCenterView.objCards[0].getID());
        } else {
            addObjectiveCardSpot(leftPanel, "Objective Card 1", -1);
        }

        if (gameView.tableCenterView.objCards[1] != null) {
            addObjectiveCardSpot(leftPanel, "Objective Card 2", gameView.tableCenterView.objCards[1].getID());
        } else {
            addObjectiveCardSpot(leftPanel, "Objective Card 2", -1);
        }
        hideDrawButton();
        return leftPanel;
    }

    private void addObjectiveCardSpot(JPanel panel, String title, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel cardLabel = new JLabel();
        if (cardID > 0) {
            cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));
        } else {
            cardLabel.setIcon(possiblePlayImage);
        }
        cardPanel.add(cardLabel, BorderLayout.CENTER);


        panel.add(cardPanel);
    }

    public void addDeck(JPanel panel, String title, CardColor cardColor, int deckIndex) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        try{
            int colorId = getCardColorId(cardColor,deckIndex==0);
            deckLabel.setIcon(getImageIcon(GUI.getCardPath(colorId, true), 0, 0));
        }
        catch(IllegalArgumentException e){
            deckLabel.setIcon(possiblePlayImage);
        }
        deckLabels[deckIndex] = deckLabel;
        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.setPreferredSize(new Dimension(100, 10));
        drawButton.setMargin(new Insets(2, 2, 2, 2));
        final int index=deckIndex==0? 6: 5;

        drawButton.addActionListener(e -> {
            System.out.println("Draw from " + title);
            drawCard(index);
        });
        drawButtons.add(drawButton);
        deckPanel.add(drawButton, BorderLayout.EAST);
        panel.add(deckPanel);
    }
    private void addCardSpot(JPanel panel, int index, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder("Card " + (index + 1)));

        JLabel cardLabel = new JLabel();
        if (cardID > 0) {
            cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));
        } else {
            cardLabel.setIcon(possiblePlayImage);
        }
        cardLabels.add(cardLabel);
        cardsID[index] = cardID;
        cardPanel.add(cardLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.setPreferredSize(new Dimension(100, 1));
        drawButton.setMargin(new Insets(2, 1, 5, 1));
        drawButton.addActionListener(e -> {
            System.out.println("Draw card " + (index + 1));
            drawCard(index + 1);
        });
        drawButtons.add(drawButton);
        cardPanel.add(drawButton, BorderLayout.EAST);
        panel.add(cardPanel);
    }

    private ImageIcon getImageIcon(String path, int scaleX, int scaleY) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_SMOOTH));
    }

    private void drawCard(int spotID) {
        if (drawing) {
            this.drawChoice = spotID;
            hideDrawButton();
            synchronized (drawLock) {
                drawLock.notifyAll();
            }
        }
    }

    public int getDrawChoice() {
        return drawChoice;
    }

    private void updateCards() {
        for (int i = 0; i < 4; i++) {
            if (gameView.tableCenterView.centerCards[i] == null) {
                cardLabels.get(i).setIcon(possiblePlayImage);
                cardsID[i] = -1;
            } else if (cardsID[i] != gameView.tableCenterView.centerCards[i].getID()) {
                cardsID[i] = gameView.tableCenterView.centerCards[i].getID();
                cardLabels.get(i).setIcon(getImageIcon(GUI.getCardPath(cardsID[i], false), 0, 0));
            }
        }
    }

    private void updateDeck() {
        for (int i = 0; i < 2; i++) {
            CardColor deckCardColor;
            if (i == 0) {
                deckCardColor = gameView.tableCenterView.topGoldCardColor;
            } else {
                deckCardColor = gameView.tableCenterView.topResourceCardColor;
            }
            try{
                int colorId = getCardColorId(deckCardColor, i==0);
                deckLabels[i].setIcon(getImageIcon(GUI.getCardPath(colorId, true), 0, 0));
            }
            catch (IllegalArgumentException e ){
                deckLabels[i].setIcon(possiblePlayImage);
            }
        }
    }

    public void showDrawButton() {
        int i=0;
        for (JButton button : drawButtons) {
            if(i<2 || cardsID[i-2]>0) button.setVisible(true);
            i++;
        }
    }

    private void hideDrawButton() {
        for (JButton button : drawButtons) {
            button.setVisible(false);
        }
    }
    public void update(GameView gameView, boolean drawing) {
        this.gameView = gameView;
        this.drawing = drawing;
        updateDeck();
        updateCards();
        if (drawing) {
            showDrawButton();
        }
    }

    public static int getCardColorId(CardColor color,boolean gold) {
        int id=0;
        switch (color) {
            case RED:
                id=1; break;
            case GREEN:
                id=11; break;
            case PURPLE:
                id=31; break;
            case BLUE:
                id=21; break;
            default:
                throw new IllegalArgumentException("Unknown CardColor: " + color);
        }
        if (gold) return id+40;
        else return id;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        String[] playerNames = {"1", "2", "3", "4"};
        Game game = new Game(4, playerNames, null);
        GameView gameView = new GameView(game);
        TableCenterPanel panel = new TableCenterPanel(gameView, new Object());

        frame.add(panel);
        frame.setVisible(true);
    }
}

