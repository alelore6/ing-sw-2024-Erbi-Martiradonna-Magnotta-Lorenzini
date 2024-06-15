package it.polimi.ingsw.Events;

public class ServerMessage extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ServerMessage(String message, String nickname) {
        super(message, nickname);
        mustBeSentToAll = (nickname == null ? true : false);
    }
}
