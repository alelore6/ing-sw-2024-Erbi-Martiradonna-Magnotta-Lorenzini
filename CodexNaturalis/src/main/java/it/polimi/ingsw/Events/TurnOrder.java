package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification of the turns order in the game
 */
public class TurnOrder extends GenericEvent{
    /**
     * the message that contains the players order
     */
    public final String order;
    /**
     * the game info at the moment of the event
     */
    public final GameView gameView;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param order the turns order
     * @param gameView the game info
     */
    public TurnOrder(String nickname, String order, GameView gameView) {
        super("The game turn order is: "+ order, nickname);
        this.gameView = gameView;
        this.order = order;
        mustBeSentToAll=true;
    }

}
