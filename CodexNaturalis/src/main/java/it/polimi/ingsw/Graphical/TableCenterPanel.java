package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class TableCenterPanel extends JSplitPane {
    private GameView gameView;
    private Map<Integer, JLabel> cardLabels;
    public ResourceDeck resourceDeck;
    public GoldDeck goldDeck;
    private Map<Integer, Card> cardsOnTable;
    private int drawchoice;
    private boolean drawing = false;
    private JButton[] spots;
    private int[] spotXCoords = {/* coordinate*/};
    private int[] spotYCoords = { /*coordinate*/};
    private ArrayList<JButton> drawButtons;
    public int spotID;
    private JLabel cardLabel;

    public TableCenterPanel(GameView gameView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;
        this.cardLabels = new HashMap<>();
        this.cardsOnTable = new HashMap<>();
        this.spots = new JButton[27];
        this.drawButtons = new ArrayList<>();

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

        setVisible(true);
        return rightPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Decks
        addDeck(leftPanel, "Gold Deck", 6);
        addDeck(leftPanel, "Resource Deck", 5);

        for (int i = 0; i < 2; i++) {
            addCardSpot(leftPanel, "Gold Card " + (i + 1), (i + 41));
        }

        for (int i = 1; i <= 2; i++) {
            addCardSpot(leftPanel, "Resource Card " + i, i);
        }
        addObjectiveCardSpot(leftPanel, "Objective Card ", 89);
        addObjectiveCardSpot(leftPanel, "Objective Card ", 88);

        return leftPanel;
    }

    private void addObjectiveCardSpot(JPanel panel, String title, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel cardLabel = new JLabel();
        cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 500, 300));
        cardLabels.put(cardID, cardLabel);
        cardPanel.add(cardLabel, BorderLayout.CENTER);

        panel.add(cardPanel);
    }

    public void addDeck(JPanel panel, String title, int cardID) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        deckLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));
        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {
            System.out.println("Draw from " + title);
            try {
                drawCardFromDeck(cardID);
            } catch (isEmptyException ex) {
                throw new RuntimeException(ex);
            }
        });
        drawButtons.add(drawButton);
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
            try {
                drawCardFromCardSpot(cardID);
            } catch (isEmptyException ex) {
                throw new RuntimeException(ex);
            }
        });
        drawButtons.add(drawButton);
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
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_SMOOTH));
    }

    private void drawCardFromDeck(int deckID) throws isEmptyException {
        Card drawnCard = null;
        if (deckID == 5) {
            drawnCard = resourceDeck.draw();
            if (drawnCard != null) {
                updateDeckImage(deckID);
            }
        } else if (deckID == 6) {
            drawnCard = goldDeck.draw();
            if (drawnCard != null) {
                updateDeckImage(deckID);
            }
        }
    }

    public void drawCardFromCardSpot(int cardID) throws isEmptyException {
        Card drawnCard = null;
        if (cardID <= 40) {
            drawnCard = resourceDeck.draw();
            if (drawnCard != null) {
                cardsOnTable.put(cardID, drawnCard);
                updateSpotImage(cardID);
                updateDeckImage(5); // Update Resource Deck Image
            }
        } else if (cardID > 40 && cardID <= 80) {
            drawnCard = goldDeck.draw();
            if (drawnCard != null) {
                cardsOnTable.put(cardID, drawnCard);
                updateSpotImage(cardID);
                updateDeckImage(6); // Update Gold Deck Image
            }
        }
    }

    private void updateSpotImage(int cardID) {
        JLabel cardLabel = cardLabels.get(cardID);
        if (cardLabel != null) {
            Card card = cardsOnTable.get(cardID);
            if (card != null) {
                cardLabel.setIcon(getImageIcon(GUI.getCardPath(card.getID(), false), 300, 180));
            } else {
                cardLabel.setIcon(null);
            }
        }
    }

    private void updateDeckImage(int deckID) {
        try {
            if (deckID == 5) {
                ResourceCard nextCard = resourceDeck.peekNextCard();
                JLabel deckLabel = cardLabels.get(deckID);
                if (deckLabel != null) {
                    deckLabel.setIcon(getImageIcon(GUI.getCardPath(nextCard.getID(), false), 300, 180));
                }
            } else if (deckID == 6) {
                GoldCard nextCard = goldDeck.peekNextCard();
                JLabel deckLabel = cardLabels.get(deckID);
                if (deckLabel != null) {
                    deckLabel.setIcon(getImageIcon(GUI.getCardPath(nextCard.getID(), false), 300, 180));
                }
            }
        } catch (isEmptyException ex) {
            JLabel deckLabel = cardLabels.get(deckID);
            if (deckLabel != null) {
                deckLabel.setIcon(null);
                deckLabel.setText("No more cards!");
            }
        }
    }

    public void showDrawButton() {
        for (JButton button : drawButtons) {
            button.setVisible(true);
        }
    }

    public void hideDrawButton() {
        for (JButton button : drawButtons) {
            button.setVisible(false);
        }
    }

    public void update(GameView gameView) {
        this.gameView = gameView;

        if (drawing) {
            showDrawButton();
        } else {
            hideDrawButton();
        }
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
