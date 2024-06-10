package it.polimi.ingsw.Graphical;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ResourceCard;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class PlayedCardsPanel extends JPanel {
    private final int overlapOffset = 40;
    private final List<CardComponent> cardComponents;
    private BufferedImage possiblePlayimage=null;

    public PlayedCardsPanel(Card[][] matrix) {
        this.cardComponents = new ArrayList<>();
        int size = matrix[0].length;
        setLayout(null); // Layout manager nullo per posizionamento assoluto

        try {
            possiblePlayimage=ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != null) {
                    // mettere  matrix[i][j].getPlayOrder() al posto del numero random
                    CardComponent c=new CardComponent(matrix[i][j], i, j,rand.nextInt(100)+1);
                    c.setImage(getImage(GUI.getCardPath(matrix[i][j].getID() + 1, matrix[i][j].isFacedown)));
                    cardComponents.add(c);
                }
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                for (CardComponent cardComponent : cardComponents) {
                    int x = cardComponent.getCol() * (250 - overlapOffset);
                    int y = cardComponent.getRow() * (150 - overlapOffset);
                    Rectangle imageBounds = new Rectangle(x, y, 250, 150);
                    if (imageBounds.contains(clickPoint)) {
                        System.out.println("Card clicked: " + cardComponent.getCard().getID());
                        // Aggiungi qui l'azione personalizzata che desideri
                        break;
                    }
                }
            }
        });
    }

    private BufferedImage getImage(String path) {
        try {
            return ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Ordina i componenti per ordine di posizionamento
        cardComponents.sort(Comparator.comparingInt(CardComponent::getPositionOrder));

        for (CardComponent c : cardComponents) {
            int x = c.getCol() * (250 - overlapOffset);
            int y = c.getRow() * (150 - overlapOffset);
            if(c.getCol()*c.getRow()%5==0) g.drawImage(possiblePlayimage, x, y, 250, 150, null);
            else g.drawImage(c.getImage(), x, y, 250, 150, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (cardComponents.size() > 0) {
            int maxRow = cardComponents.stream().mapToInt(CardComponent::getRow).max().orElse(0);
            int maxCol = cardComponents.stream().mapToInt(CardComponent::getCol).max().orElse(0);
            int componentWidth = 250;
            int componentHeight = 150;
            int preferredWidth = (maxCol + 1) * (componentWidth - overlapOffset) + overlapOffset;
            int preferredHeight = (maxRow + 1) * (componentHeight - overlapOffset) + overlapOffset;
            return new Dimension(preferredWidth, preferredHeight);
        } else {
            return new Dimension(800, 600); // Default size
        }
    }



    public static void main(String[] args) {
        Card[][] matrix = new Card[10][10];

        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i % 2 == j % 2) {
                    ResourceCard card = new ResourceCard(rand.nextInt(80));
                    matrix[i][j] = card;
                }
            }
        }

        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayedCardsPanel panel = new PlayedCardsPanel(PlayerPanel.subMatrix(matrix));

        PlayerPanel scrollPane = new PlayerPanel(null);
        scrollPane.setViewportView(panel);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}