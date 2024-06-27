package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 *Card is the class that represents all the generic card (playable and starting) that compose game board.
 * It implements Serializable because through serialization it's possible to transmit objects between different machines in a network.
 */
public abstract class Card implements Serializable {
    /**
     *IDs of the cards.
     */
    protected int ID;
    /**
     * color of the cards.
     */
    protected CardColor color;
    /**
     *
     */
    protected int playOrder;
    /**
     *   isFacedown is a boolean to know if a card is displayed on the game board on the front or on the back.
     */
    // Everything about the back of the card is based on this boolean:
    public boolean isFacedown = false;
    /**
     * frontCorners represents all corners of the front of a card.
     */
    protected Corner[] frontCorners;
    /**
     * backCorners represents all corners of the back of a card.
     */
    protected Corner[] backCorners;

    /**
     *getCard is a getter to obtain the card required.
     * @return the card required.
     */
    public Card getCard(){
         return this;
     }

    /**
     * getID is a getter to obtain the ID of any card.
     * @return ID of any card.
     */
    public int getID(){
          return this.ID;
     }

    /**
     * getFrontCorners is a getter of the corners of the front of a card.
     * @return corners of the front of a card.
     */
    public Corner[] getFrontCorners(){
         return frontCorners;
     }

    /**
     * getBackCorners is a getter of the corners of the back of a card.
     * @return corners of the back of a card.
     */
    public Corner[] getBackCorners(){return backCorners;}

    /**
     * getCorners is a getter method that determines which corners of the card to return based on the card's face status.
     * @return if card is face down it  returns back corners, if card is face up it returns front corners.
     */
    public Corner[] getCorners(){
        return isFacedown ? getBackCorners() : getFrontCorners();
    }

    /**
     * getColors is a getter method that return the color of any card.
     * @return Colors of any card.
     */
    public CardColor getColor(){
        return color;
    }

    /**
     *
     * @return
     */
    public int getPlayOrder() {return playOrder;}
}
