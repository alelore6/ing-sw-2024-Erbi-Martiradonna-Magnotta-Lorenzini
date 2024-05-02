package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.PlayableCard;

public class DrawCardRequest extends GenericEvent{

    public final PlayableCard[] tableCards;

    public final boolean goldDeckEmpty;
    //if true can't draw from this deck
    public final boolean resDeckEmpty;

    public DrawCardRequest(String nickname, PlayableCard[] tableCards, boolean goldDeckEmpty, boolean resDeckEmpty){
        super("Now it's time to draw a card: choose a source to draw from. \n",nickname);
        this.tableCards = tableCards;
        this.goldDeckEmpty = goldDeckEmpty;
        this.resDeckEmpty = resDeckEmpty;
    }

}
