package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.Deck;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.ResourceDeck;
import it.polimi.ingsw.Model.GoldDeck;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableCenterPanel extends JSplitPane {
    private final GameView gameView;
    private Map<Integer, JLabel> cardLabels;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private Map<Integer, Card> cardsOnTable;
    private JButton[] spots;
    private int[] spotXCoords = {/* coordinate*/};
    private int[] spotYCoords = { /*coordinate*/};

    public TableCenterPanel(GameView gameView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;
        this.cardLabels = new HashMap<>();
        this.cardsOnTable = new HashMap<>();
        this.spots = new JButton[27];

        this.resourceDeck = new ResourceDeck();
        this.goldDeck = new GoldDeck();

        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());

        this.setDividerLocation(1000);
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

        for (int i = 0; i < 27; i++) {
            spots[i] = new JButton();
            spots[i].setBounds(spotXCoords[i], spotYCoords[i], 50, 50);
            spots[i].setContentAreaFilled(false);
            spots[i].setBorderPainted(false);
            rightPanel.add(spots[i]);
        }
        setVisible(true);
        return rightPanel;
    }


    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Decks
        addDeck(leftPanel, "Gold Deck", 6);
        addDeck(leftPanel, "Resource Deck", 5);

        for (int i = 41; i <= 42; i++) {
            addCardSpot(leftPanel, "Gold Card " + (i - 40), i);
        }

        for (int i = 1; i <= 2; i++) {
            addCardSpot(leftPanel, "Resource Card " + i, i);
        }

        return leftPanel;
    }

    private void addDeck(JPanel panel, String title, int deckID) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        deckLabel.setIcon(getImageIcon(GUI.getCardPath(deckID, false), 0, 0));
        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {

            System.out.println("Draw from " + title);
            try {
                drawCardFromDeck(deckID);
            } catch (isEmptyException ex) {
                throw new RuntimeException(ex);
            }
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
        cardLabels.put(cardID, cardLabel);
        cardPanel.add(cardLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {
            System.out.println("Draw from " + title);
            drawCardFromCardSpot(cardID);
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


    private void drawCardFromDeck(int deckID) throws isEmptyException {
        Card drawnCard;
        if (deckID == 5) {
            drawnCard = resourceDeck.draw();
        } else if (deckID == 6) {
            drawnCard = goldDeck.draw();
        } else {
            return;
        }

        if (drawnCard != null) {
            System.out.println("Drew card " + drawnCard.getID() + " from deck " + deckID);
            //TODO Update the GUI with the new card image
        } else {
            System.out.println("Deck " + deckID + " is empty!");
        }
    }

    private void drawCardFromCardSpot(int spotID)  {
        Card drawnCard = cardsOnTable.remove(spotID);
        if (drawnCard != null) {
            System.out.println("Drew card " + drawnCard.getID() + " from spot " + spotID);
            JLabel cardLabel = cardLabels.get(spotID);
            if (cardLabel != null) {
                cardLabel.setIcon(null);
            }
        } else {
            System.out.println("No card at spot " + spotID);
        }
    }
    public void updateSpot(int spotIndex, String playerIconPath) {
        ImageIcon icon = getImageIcon(playerIconPath, 0, 0);
        spots[spotIndex].setIcon(icon);
    }

    public void update(GameView gameView) {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        String[] playerNames = {"1", "2", "3", "4"};
        Game game = new Game(4, playerNames, null);
        GameView gameView = new GameView(game);
        TableCenterPanel panel = new TableCenterPanel(gameView);

        frame.add(panel);
        frame.setVisible(true);
    }
}
