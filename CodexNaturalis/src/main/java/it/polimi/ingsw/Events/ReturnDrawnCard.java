package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

/**
 * Event that represent the return of the drawn card after a draw response
 */
public class ReturnDrawnCard extends GenericEvent{
    /**
     * the drawn card
     */
    private final PlayableCard card;

    /**
     * Constructor
     * @param card the drawn card
     * @param nickname the player that made the draw
     */
    public ReturnDrawnCard(PlayableCard card, String nickname) {
        super("Here is the card drawn",nickname);
        this.card = card;
    }
}
