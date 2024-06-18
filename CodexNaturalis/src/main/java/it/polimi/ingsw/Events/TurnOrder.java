package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification of the turns order in the game
 */
public class TurnOrder extends GenericEvent{

    public final String order;

    public final GameView gameView;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param order the turns order
     */
    public TurnOrder(String nickname, String order, GameView gameView) {
        super("The game turn order is: "+ order, nickname);
        this.gameView = gameView;
        this.order = order;
        mustBeSentToAll=true;
    }

}
