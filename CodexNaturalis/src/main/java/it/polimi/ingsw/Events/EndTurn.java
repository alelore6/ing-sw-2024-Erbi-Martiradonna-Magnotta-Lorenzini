package it.polimi.ingsw.Events;

import java.util.HashMap;

/**
 * Event that represent the end of a player's turn
 */
public class EndTurn extends GenericEvent{
    /**
     * current rankings
     */
    public final HashMap<String,Integer> points;
    /**
     * Constructor
     * @param turnPlayer the player whose turn is over
     * @param nickname player that receives or sends the event
     * @param points current rankings
     */
    public EndTurn(String turnPlayer, String nickname, HashMap<String, Integer> points) {
        super(turnPlayer+"'s turn is over. Here there are the new scores.\n", nickname);
        this.points = points;
    }

}
