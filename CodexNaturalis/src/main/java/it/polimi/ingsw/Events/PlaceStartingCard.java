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
        super("Now it's time to place your starting card.",nickname);
        this.startingCard = startingCard;
    }

    public String msgOutput2(){
        return "Enter (1) if you want the card faced up, (2) if faced down: \n";
    }
}
