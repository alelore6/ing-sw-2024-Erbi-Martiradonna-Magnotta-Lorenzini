package it.polimi.ingsw.Events;

/**
 * Event that represent the notification that it's the turn of a player
 */
public class YourTurn extends GenericEvent{
    /**
     * Constructor
     * @param nickname the receiver of the event
     * @param turnPlayer the player whose turn is starting
     */
    public YourTurn(String nickname, String turnPlayer){
        super("It's "+ turnPlayer+ "'s turn!",nickname);
    }
}
