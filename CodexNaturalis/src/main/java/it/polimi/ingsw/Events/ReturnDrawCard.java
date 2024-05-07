package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

/**
 * Event that represent the return of the drawn card after a draw response
 */
public class ReturnDrawCard extends GenericEvent{
    /**
     * the new hand with the drawn card
     */
    private final PlayableCard[] handCards;

    /**
     * Constructor
     * @param handCards the new hand with the drawn card
     * @param nickname the player that made the draw
     */
    public ReturnDrawCard(PlayableCard handCards[], String nickname) {
        super("Here is the card drawn",nickname);
        this.handCards = handCards;
    }
}
