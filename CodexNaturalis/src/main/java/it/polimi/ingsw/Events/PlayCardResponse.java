package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

/**
 * Event that represent the response to a request to play a card
 */
public class PlayCardResponse extends GenericEvent{
    /**
     * the card chosen that will be played
     */
    public final PlayableCard card;
    /**
     * the x-position in the matrix of displayed cards of the player where the card will be played
     */
    public final int posX;
    /**
     * the y-position in the matrix of displayed cards of the player where the card will be played
     */
    public final int posY;

    /**
     * Constructor
     * @param nickname the player that sends the event
     * @param card the chosen card
     * @param posX the chosen x-position where the card will be played
     * @param posY the chosen y-position where the card will be played
     */
    public PlayCardResponse(String nickname, PlayableCard card, int posX, int posY){
        super("Card and position chosen!",nickname);
        this.card = card;
        this.posX = posX;
        this.posY = posY;
    }
}
