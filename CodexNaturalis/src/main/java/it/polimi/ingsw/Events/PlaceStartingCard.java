package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.StartingCard;

import java.io.Serializable;

/**
 * Events that represent the action of placing the starting card
 */
public class PlaceStartingCard extends GenericRequest{
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
        super("Now it's time to place your starting card.\n" +
                "Enter (1) if you want the card faced up, (2) if faced down: ",nickname);
        this.startingCard = startingCard;
    }
}
