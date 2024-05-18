package it.polimi.ingsw.Events;

/**
 * Event that represent the trigger of the end game
 */
public class EndGameTriggered extends GenericEvent{
    /**
     * Constructor
     * @param message message that explain the cause of the event
     * @param nickname the player that receives the event
     */
    public EndGameTriggered(String message,String nickname) {
        super(message,nickname);
    }
}
