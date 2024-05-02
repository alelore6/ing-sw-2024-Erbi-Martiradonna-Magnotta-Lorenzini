package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class ReturnDrawnCard extends GenericEvent{
    private final PlayableCard card;

    public ReturnDrawnCard(PlayableCard card, String nickname) {
        super("Here is the card drawn",nickname);
        this.card = card;
    }
}
