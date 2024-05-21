package it.polimi.ingsw.Events;

/**
 * Event that represent the notification that it's the turn of a player
 */
public class StartTurn extends GenericEvent{
    /**
     * Constructor
     * @param nickname the receiver of the event
     * @param turnPlayer the player whose turn is starting
     */
    public StartTurn(String nickname, String turnPlayer){
        super("It's "+ turnPlayer+ "'s turn!",nickname);
        mustBeSentToAll=true;
    }
}
