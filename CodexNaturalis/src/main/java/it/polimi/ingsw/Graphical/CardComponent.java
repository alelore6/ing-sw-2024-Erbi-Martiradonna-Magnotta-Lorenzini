package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Card;

import java.awt.image.BufferedImage;

/**
 * describes the cards contained in the PlayedCardsPanel
 */
public class CardComponent {
    /**
     * the card
     */
    private final Card card;
    /**
     * the image of the card
     */
    private BufferedImage image;
    /**
     * the x position of the card in the played cards matrix
     */
    private final int row;
    /**
     * the y position of the card in the played cards matrix
     */
    private final int col;
    /**
     * the play order of the card so that overlapping corners are correct
     */
    private final int positionOrder;
    /**
     * indicates whether tha card has been flipped
     */
    private boolean flipped;

    /**
     * Constructor
     * @param card the card
     * @param row the x position
     * @param col the y position
     * @param positionOrder the play order of the card
     */
    public CardComponent(Card card, int row, int col, int positionOrder) {
        this.card = card;
        this.row = row;
        this.col = col;
        this.positionOrder = positionOrder;
    }

    /**
     * getter for the image
     * @return the image
     */
    protected BufferedImage getImage() {
        return image;
    }

    /**
     * getter for the x position
     * @return the x position of the card
     */
    public int getRow() {
        return row;
    }
    /**
     * getter for the y position
     * @return the y position of the card
     */
    public int getCol() {
        return col;
    }

    /**
     * getter for the play order
     * @return the play order
     */
    protected int getPositionOrder() {
        return positionOrder;
    }

    /**
     * setter for the image
     * @param image the image
     */
    protected void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * getter for the card id
     * @return the card id
     */
    public int getCardID(){
        return card.getID();
    }

    /**
     * Setter for the flipped attribute
     * @param flipped indicates whether the card has been flipped
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Getter for the flipped attribute
     * @return the value of the flipped attribute
     */
    public boolean isFlipped() {return flipped;}
}
