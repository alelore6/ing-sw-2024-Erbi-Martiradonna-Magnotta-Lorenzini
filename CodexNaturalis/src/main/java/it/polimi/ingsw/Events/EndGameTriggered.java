package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the trigger of the end game
 */
public class EndGameTriggered extends GenericEvent{

    public final GameView gameView;
    /**
     * Constructor
     * @param message message that explain the cause of the event
     * @param nickname the player that receives the event
     */
    public EndGameTriggered(String message, String nickname, GameView gameView) {
        super(message,nickname);
        this.gameView = gameView;
    }
}
