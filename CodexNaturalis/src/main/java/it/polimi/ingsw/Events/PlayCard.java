package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class PlayCard extends GenericEvent{
    private PlayableCard card;
    private int posX;
    private int posY;
    public PlayCard(){
        message="Now it's time to play a card: Choose a card from your hand and a position where the card will be played";
    }
    public PlayableCard getCard() {
        return card;
    }

    public void setCard(PlayableCard card) {
        this.card = card;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
