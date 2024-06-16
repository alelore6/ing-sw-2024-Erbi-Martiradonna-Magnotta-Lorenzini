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

/**
 * the panel that contains all the played cards of the player
 */
class PlayedCardsPanel extends JPanel {
    /**
     * the constant that describes the overlapping of the images
     */
    private final int overlapOffset = 40;
    /**
     * the list of the cards info contained in the panel
     */
    private final List<CardComponent> cardComponents;
    /**
     * the possible play image
     */
    private BufferedImage possiblePlayImage =null;
    /**
     * the selected possible play where a card will be played
     */
    private CardComponent selectedCard=null;

    /**
     * Constructor where the cards are positioned for the first time
     * @param matrix the player's played cards
     * @param playing boolean that represent if the panel is contained in a personal panel or not. If true selection actions will be allowed
     */
    public PlayedCardsPanel(Card[][] matrix, boolean playing) {
        this.cardComponents = new ArrayList<>();
        setBackground(Color.decode("#d9dbc1"));
        setLayout(null); // Layout manager nullo per posizionamento assoluto

        //carico immagine per possible-plays
        try {
            possiblePlayImage =ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (matrix==null) return;
        int size = matrix.length;

        //prendo le carte nella matrice e le trasformo in card component
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != null) {
                    CardComponent c=new CardComponent(matrix[i][j], i, j,matrix[i][j].getPlayOrder());
                    c.setImage(getImage(GUI.getCardPath(matrix[i][j].getID() + 1, matrix[i][j].isFacedown)));
                    cardComponents.add(c);
                }
            }
        }
        //TODO aggiungere possible plays

        if(playing){
            //per i click per scegliere la posizione dove giocare
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    for (CardComponent c : cardComponents) { //TODO deve essere solo per possible plays
                        int x = c.getCol() * (250 - overlapOffset);
                        int y = c.getRow() * (150 - overlapOffset);
                        Rectangle imageBounds = new Rectangle(x, y, 250, 150);
                        if (imageBounds.contains(clickPoint)) {
                            selectedCard = c;
                            repaint();
                            System.out.println("Card clicked: " + c.getCardID());
                            break;
                        }
                    }
                }
            });
        }
    }

    /**
     * update the panel adding the new cards
     * @param matrix the player's played cards
     */
    protected void update(Card[][] matrix){
        int numCards = cardComponents.size();
        int size=matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //le nuove carte vengono aggiunte
                if (matrix[i][j] != null && matrix[i][j].getPlayOrder()>numCards) {
                    CardComponent c=new CardComponent(matrix[i][j], i, j,matrix[i][j].getPlayOrder());
                    c.setImage(getImage(GUI.getCardPath(matrix[i][j].getID() , matrix[i][j].isFacedown)));
                    cardComponents.add(c);
                }
            }
        }
        //TODO aggiungere possible plays
        selectedCard=null;
        repaint();
    }

    /**
     * Load the image
     * @param path the path to the image
     * @return the image
     */
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
            if(c.getCol()*c.getRow()%5==0) g.drawImage(possiblePlayImage, x, y, 250, 150, null); //TODO separare possible plays
            else g.drawImage(c.getImage(), x, y, 250, 150, null);
        }

        if (selectedCard != null) {
            g.setColor(Color.GREEN);
            g.drawRect(selectedCard.getCol()* (250 - overlapOffset), selectedCard.getRow()* (150 - overlapOffset), 250, 150);
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

    /**
     * getter for the selected possible play
     * @return the card component that describes the selected image on the panel
     */
    protected CardComponent getSelectedCard() {
        return selectedCard;
    }



    public static void main(String[] args) {
        Card[][] matrix = new Card[10][10];
        int count=0;
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i % 2 == j % 2) {
                    ResourceCard card = new ResourceCard(rand.nextInt(80)+1,count);
                    matrix[i][j] = card;
                    count++;
                }
            }
        }

        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayedCardsPanel panel = new PlayedCardsPanel(matrix, true);

        PlayerPanel scrollPane = new PlayerPanel(null, true);
        scrollPane.setViewportView(panel);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

//        int n=0;
//        while (n<1000000000) {n++;}
//        System.out.println("wait finito");
//        matrix[1][2]=new ResourceCard(rand.nextInt(80)+1,count+1);
//        panel.update(matrix);
    }
}