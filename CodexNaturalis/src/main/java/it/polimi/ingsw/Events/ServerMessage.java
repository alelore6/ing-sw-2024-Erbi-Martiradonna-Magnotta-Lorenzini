package it.polimi.ingsw.Events;

/**
 * Event that needs to be handled asynchronously
 */
public class ServerMessage extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ServerMessage(String message, String nickname) {
        super(message, nickname);
    }
}
