package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.StartingCard;

/**
 * Events that represent the action of placing the starting card
 */
public class PlaceStartingCard extends GenericEvent {
    /**
     * the starting card
     */
    public final StartingCard startingCard;

    /**
     * Constructor
     * @param startingCard the starting card
     * @param nickname the receiver of the event
     */
    public PlaceStartingCard(StartingCard startingCard, String nickname) {
        super("Now it's time to place your starting card. You can choose whether flip or not the card",nickname);
        this.startingCard = startingCard;
    }
}
