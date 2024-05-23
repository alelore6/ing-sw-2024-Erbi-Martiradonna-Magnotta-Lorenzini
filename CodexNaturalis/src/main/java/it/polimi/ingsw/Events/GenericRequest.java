package it.polimi.ingsw.Events;

public abstract class GenericRequest extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public GenericRequest(String message, String nickname) {
        super(message, nickname);
    }
}
