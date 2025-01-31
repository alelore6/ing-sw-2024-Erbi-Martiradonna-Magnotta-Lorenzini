package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ResourceCard;
import it.polimi.ingsw.Model.StartingCard;
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
     * the value that determines the overlapping of the images
     */
    private final int overlapOffset = 50;
    /**
     * the list of the cards info contained in the panel
     */
    private final List<CardComponent> cardComponents;
    /**
     * image that indicates a position where it's possible to play a card
     */
    private BufferedImage possiblePlayImage =null;
    /**
     * the selected possible play where a card will be played
     */
    private CardComponent selectedCard=null;
    private int center_row=1;
    private int center_col=1;
    private boolean playing=false;
    private int numCards=0;

    /**
     * Constructor where the cards are positioned for the first time
     * @param matrix the player's played cards
     * @param playing boolean that represent if the panel is contained in a personal panel or not. If true selection actions will be allowed
     */
    public PlayedCardsPanel(Card[][] matrix, boolean playing) {
        this.playing= playing;
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
        int row = matrix.length;
        int col= matrix[0].length;

        //prendo le carte nella matrice e le trasformo in card component
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] != null) {
                    if(matrix[i][j] instanceof StartingCard && matrix[i][j].getID()>0){
                        center_row=i;
                        center_col=j;
                    }
                    CardComponent c=new CardComponent(matrix[i][j], i, j,matrix[i][j].getPlayOrder());
                    if (c.getCardID()!=-1) {
                        c.setImage(getImage(GUI.getCardPath(matrix[i][j].getID(), matrix[i][j].isFacedown)));
                        numCards++;
                    }
                    cardComponents.add(c);
                }
            }
        }

        if(playing){
            //per i click per scegliere la posizione dove giocare
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    for (CardComponent c : cardComponents) {
                        int x = c.getCol() * (250 - overlapOffset);
                        int y = c.getRow() * (150 - overlapOffset);
                        Rectangle imageBounds = new Rectangle(x, y, 250, 150);
                        if (imageBounds.contains(clickPoint)) {
                            if(c.getCardID()==-1){
                                //System.out.println("position chosen: "+ (c.getRow()-center_row)+","+(c.getCol()-center_col));
                                selectedCard = c;
                                repaint();
                            }
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
    void update(Card[][] matrix){
        int row =matrix.length;
        int col=matrix[0].length;
        removeAll(); // Rimuove tutti i componenti presenti nel JPanel
        cardComponents.clear();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j]!=null && matrix[i][j] instanceof StartingCard && matrix[i][j].getID()>0) {
                    center_row = i;
                    center_col = j;
                }
                //le nuove carte vengono aggiunte
                if (matrix[i][j] != null) {
                    CardComponent c=new CardComponent(matrix[i][j], i, j,matrix[i][j].getPlayOrder());
                    if(c.getCardID()!=-1) {
                        c.setImage(getImage(GUI.getCardPath(matrix[i][j].getID(), matrix[i][j].isFacedown)));
                        numCards++;
                    }
                    cardComponents.add(c);
                }
            }
        }

        selectedCard=null;
        revalidate();
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


    /**
     * draws the cards on the panel in order based on the positionOrder attribute in cardComponent.
     * Allows the overlapping corners.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Ordina i componenti per ordine di posizionamento
        cardComponents.sort(Comparator.comparingInt(CardComponent::getPositionOrder));

        for (CardComponent c : cardComponents) {
            int x = c.getCol() * (250 - overlapOffset);
            int y = c.getRow() * (150 - overlapOffset);
            if(c.getCardID()==-1 && playing) g.drawImage(possiblePlayImage, x, y, 250, 150, null);
            else g.drawImage(c.getImage(), x, y, 250, 150, null);
        }

        if (selectedCard != null) {
            g.setColor(Color.GREEN);
            g.drawRect(selectedCard.getCol()* (250 - overlapOffset), selectedCard.getRow()* (150 - overlapOffset), 250, 150);
        }
    }


    /**
     * makes the preferred size of the panel variable based on the cards played
     * @return the preferred dimension
     */
    @Override
    public Dimension getPreferredSize() {
        if (!cardComponents.isEmpty()) {
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
    CardComponent getSelectedPosition() {
        return selectedCard;
    }
    /**
     * getter for the center row in the matrix of played cards
     * @return the center row index
     */
    int getCenter_row() {
        return center_row;
    }
    /**
     * getter for the center column in the matrix of played cards
     * @return the center column index
     */
    int getCenter_col() {
        return center_col;
    }

}