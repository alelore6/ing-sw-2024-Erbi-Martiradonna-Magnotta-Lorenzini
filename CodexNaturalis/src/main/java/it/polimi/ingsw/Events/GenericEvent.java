package it.polimi.ingsw.Events;

import java.io.Serializable;

/**
 * Generic event that represents an action that it's used in the client-server communication.
 */
public abstract class GenericEvent implements Serializable {
    /**
     * message that describes the event.
     */
    protected final String message;
    /**
     * the player that receives or sends the event
     */
    public final String nickname;
    /**
     * Boolean that indicates if the event must be sent to all.
     */
    public boolean mustBeSentToAll = false;

    /**
     * Constructor
     * @param message message describing the event
     * @param nickname player that receives or sends the event
     */
    public GenericEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
        if(nickname == null || nickname.equals("every one")) mustBeSentToAll = true;
    }

    /**
     * Method to return the colored message for TUI.
     * @return the colored message.
     */
    public String msgOutput(){
        return message;
    }

    /**
     * Method to return the  message for GUI.
     * @return the  message.
     */
    public String getMessage() {
        return message;
    }
}
