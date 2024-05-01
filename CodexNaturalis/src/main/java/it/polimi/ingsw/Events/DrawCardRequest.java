package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class DrawCardRequest extends GenericEvent{
    public final PlayableCard[] tableCards;

    DrawCardRequest(String nickname, PlayableCard[] tableCards){
        super("Now it's time to draw a card: choose a source to draw from. \n" +
                "enter a number between 1 and 6: 1 for resource deck, 2 for gold deck" +
                "3,4,5,6 for card on the table center.",nickname);
        this.tableCards = tableCards;
    }

}
