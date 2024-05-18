package it.polimi.ingsw.Events;

import java.io.Serializable;

/**
 * Generic event that represent an action that it's used in the client-server communication
 */
public abstract class GenericEvent implements Serializable {
    /**
     * message that describe the event
     */
    private final String message;
    /**
     * the player that receives or sends the event
     */
    public final String nickname;

    /**
     * Constructor
     * @param message message describing the event
     * @param nickname player that receives or sends the event
     */
    public GenericEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    /**
     * Getter for message
     * @return message describing the event
     */
    public String msgOutput(){
        return message;
    }
}
