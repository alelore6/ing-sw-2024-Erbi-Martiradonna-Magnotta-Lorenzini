package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.Card;

public class CardInfoResponse extends GenericEvent{
    public CardInfoResponse(Card card, String nickname) {
        super("Card number " + Integer.toString(card.getID()) + " has the following data:", nickname);
    }
}
