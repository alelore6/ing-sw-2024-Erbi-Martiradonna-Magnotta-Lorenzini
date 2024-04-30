package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class DrawCardRequest extends GenericEvent{

    DrawCardRequest(String nickname){
        //TODO sistemare messaggio
        super("Now it's time to draw a card: choose a position to draw from. \n" +
                "enter a number between 1 and 6: 1 for resource deck, 2 for gold deck" +
                "3,4,5,6 for card on the table center.",nickname);
    }

}
