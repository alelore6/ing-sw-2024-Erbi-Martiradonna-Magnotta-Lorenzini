package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.Card;

public class RotateCard extends GenericEvent{
    private Card card;

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
