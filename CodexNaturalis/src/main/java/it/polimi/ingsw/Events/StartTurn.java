package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification that it's the turn of a player
 */
public class StartTurn extends GenericEvent{
    /**
     * the color of the player whose turn is starting
     */
    public final String color;
    /**
     * the player whose turn is starting
     */
    public final String turnPlayer;
    /**
     * the game info at the moment of the event
     */
    public final GameView gameView;

    /**
     * Constructor
     * @param color the color of the player
     * @param turnPlayer the player whose turn is starting
     * @param gameView the game info
     */
    public StartTurn(String turnPlayer, String color, GameView gameView){
        super("It's " + turnPlayer + "'s turn!", "every one");
        this.color = color;
        this.turnPlayer = turnPlayer;
        this.gameView = gameView;
    }
}
