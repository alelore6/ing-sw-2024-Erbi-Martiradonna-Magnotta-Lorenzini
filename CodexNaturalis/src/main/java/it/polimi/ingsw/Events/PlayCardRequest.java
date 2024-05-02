package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class PlayCardRequest extends GenericEvent {
    public final PlayableCard[] handCards;
    //serve anche le displayedCards?
    public  PlayCardRequest(String nickname, PlayableCard[] handCards){
        super("Now it's time to play a card: Choose a card from your hand and a position where the card will be played",nickname);
        this.handCards = handCards;
    }
}
