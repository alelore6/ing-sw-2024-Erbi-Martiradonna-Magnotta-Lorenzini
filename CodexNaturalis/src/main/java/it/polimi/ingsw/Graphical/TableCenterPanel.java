package it.polimi.ingsw.Graphical;


import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TableCenterPanel extends JSplitPane {
    public int numPlayers;
    private final GameView gameView;
    private String nickname ="1";

    public TableCenterPanel(GameView gameView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        this.setLeftComponent(leftPanel);
        this.setRightComponent(rightPanel);

        this.setDividerLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7)); // 70% division
        leftPanel.setMinimumSize(new Dimension(800, 300));
        rightPanel.setMinimumSize(new Dimension(390, 300));
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns

        // Decks
        addDeck(leftPanel, "Gold Deck", 1);
        addDeck(leftPanel, "Resource Deck", 2);

        // Gold Cards
        for (int i = 0; i < 2; i++) {
            addCardSpot(leftPanel, "Gold Card Spot " + (i + 1), 3 + i);
        }

        // Resource Cards
        for (int i = 0; i < 2; i++) {
            addCardSpot(leftPanel, "Resource Card Spot " + (i + 1), 5 + i);
        }

        return leftPanel;
    }

    private void addDeck(JPanel panel, String title, int deckType) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        deckLabel.setHorizontalAlignment(JLabel.CENTER);
        deckLabel.setIcon(getImageIcon(GUI.getCardPath(deckType, false), 0, 0));

        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {
            // Da implementare logica di gioco
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
        cardLabel.setHorizontalAlignment(JLabel.CENTER);
        cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));

        cardPanel.add(cardLabel, BorderLayout.CENTER);

        panel.add(cardPanel);
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        // Insert the image path here
        imageLabel.setIcon(getImageIcon("assets/images/plateau/plateau.png", 0, 0));

        rightPanel.add(imageLabel, BorderLayout.CENTER);

        return rightPanel;
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
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
/*
        TableCenterPanel panel = new TableCenterPanel(new GameView(new Game(numPlayers, "1",)));

        frame.add(panel);*/
        frame.setVisible(true);
    }

    public void update(GameView gameView) {
    }
}
