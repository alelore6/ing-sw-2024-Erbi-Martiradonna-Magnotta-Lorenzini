package it.polimi.ingsw.Events;

/**
 * Class representing a generic response event.
 */
public abstract class GenericResponse extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public GenericResponse(String message, String nickname) {
        super(message, nickname);
    }
}
