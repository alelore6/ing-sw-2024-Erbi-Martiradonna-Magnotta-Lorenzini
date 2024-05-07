package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.Scoretrack;

public class EndTurn extends GenericEvent{
    //TODO scoretrack ha le informazioni che servono???
    public final Scoretrack scoretrack;
    /**
     * Constructor
     * @param nickname player that receives or sends the event
     */
    public EndTurn(String turnPlayer, String nickname, Scoretrack scoretrack) {
        super(turnPlayer+"'s turn is over. Here there are the new scores.\n", nickname);
        this.scoretrack = scoretrack;
    }
}
