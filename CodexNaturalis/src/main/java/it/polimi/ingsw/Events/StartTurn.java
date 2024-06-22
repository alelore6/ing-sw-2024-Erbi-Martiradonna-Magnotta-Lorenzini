package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification that it's the turn of a player
 */
public class StartTurn extends GenericEvent{

    public final String color;
    public final String turnPlayer;
    public final GameView gameView;

    /**
     * Constructor
     * @param nickname the receiver of the event
     * @param turnPlayer the player whose turn is starting
     */
    public StartTurn(String turnPlayer, String color, GameView gameView){
        super("It's " + turnPlayer + "'s turn!", "every one");
        this.color = color;
        this.turnPlayer = turnPlayer;
        this.gameView = gameView;
    }
}
