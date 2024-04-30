package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class PlayCardResponse extends GenericEvent{
    public final PlayableCard card;
    public final int posX;
    public final int posY;
    public PlayCardResponse(String nickname, PlayableCard card, int posX, int posY){
        super("Card and position chosen!",nickname);
        this.card = card;
        this.posX = posX;
        this.posY = posY;
    }
}
