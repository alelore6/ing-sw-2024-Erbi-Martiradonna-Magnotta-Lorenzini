package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class DrawCard extends GenericEvent{
    private int position;
    private PlayableCard card;

    DrawCard(){
        message="Now it's time to draw a card: choose a position to draw from";
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCard(PlayableCard card) {
        this.card = card;
    }

    public PlayableCard getCard() {
        return card;
    }

    public int getPosition() {
        return position;
    }
}
