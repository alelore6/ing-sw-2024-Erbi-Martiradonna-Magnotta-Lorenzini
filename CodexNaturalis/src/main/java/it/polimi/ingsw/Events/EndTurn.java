package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

import java.util.HashMap;

/**
 * Event that represent the end of a player's turn
 */
public class EndTurn extends GenericEvent{
    /**
     * current rankings
     */
    public final GameView gameView;
    /**
     * Constructor
     * @param turnPlayer the player whose turn is over
     * @param nickname player that receives or sends the event
     * @param points current rankings
     */
    public EndTurn(String turnPlayer, String nickname, GameView gameView) {
        super(turnPlayer+"'s turn is over.", nickname);
        this.gameView = gameView;
        mustBeSentToAll=true;
    }

}
