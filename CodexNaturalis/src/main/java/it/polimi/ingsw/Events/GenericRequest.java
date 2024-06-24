package it.polimi.ingsw.Events;

/**
 * Class representing a generic request event.
 */
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

    @Override
    public String msgOutput() {
        return "\u001B[47m" + "\u001B[30m" + super.msgOutput() + "\u001B[0m";
    }
}
