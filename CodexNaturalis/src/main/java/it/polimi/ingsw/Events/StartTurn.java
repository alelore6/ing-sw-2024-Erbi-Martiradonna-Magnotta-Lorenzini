package it.polimi.ingsw.Events;

/**
 * Event that represent the notification that it's the turn of a player
 */
public class StartTurn extends GenericEvent{

    public final String color;
    public final String turnPlayer;

    /**
     * Constructor
     * @param nickname the receiver of the event
     * @param turnPlayer the player whose turn is starting
     */
    public StartTurn(String turnPlayer, String color){
        super("It's " + turnPlayer + "'s turn!", "every one");
        this.color = color;
        this.turnPlayer = turnPlayer;
    }
}
