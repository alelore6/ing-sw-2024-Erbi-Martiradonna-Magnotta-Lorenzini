package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class DrawCardResponse extends GenericEvent{
    public final int position;
    //coded position to draw from (see DrawCardRequest)

    public DrawCardResponse(int position,String nickname) {
        super("Draw source chosen!",nickname);
        this.position = position;
    }
}
