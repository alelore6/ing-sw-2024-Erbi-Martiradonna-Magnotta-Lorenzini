package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.CurrentResources;

/**
 * Event that represent the updated current resource and displayed cards after a play
 */
public class ReturnPlayCard extends GenericEvent{
    /**
     * the displayed cards updated
     */
    public final Card[][] displayedCards;
    /**
     * the updated current resources
     */
    public final CurrentResources currentResources;
    /**
     * Constructor
     * @param displayedCards  the displayed card updated
     * @param nickname player that receives or sends the event
     */
    public ReturnPlayCard(String nickname, Card[][] displayedCards, CurrentResources currentResources) {
        super("Here there are the displayed cards updated", nickname);
        this.displayedCards = displayedCards;
        this.currentResources = currentResources;
    }
}
