package it.polimi.ingsw.Events;

public abstract class GenericRequest extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public GenericRequest(String message, String nickname) {
        super("\u001B[47m" + "\u001B[30m" + message + "\u001B[0m", nickname);
    }
}
