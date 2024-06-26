package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Client;

/**
 * Response to a reconnection request, the player enter his password to complete the reconnection
 */
public class ReconnectionResponse extends GenericResponse{
    // This attribute is for registering the client after a disconnection and it'll be assigned by the server
    // after a correct login.
    /**
     * the client that is reconnecting
     */
    public transient Client client;
    /**
     * the entered password
     */
    private final String password;
    /**
     * Constructor
     *
     * @param password  the entered password
     * @param nickname player that receives or sends the event
     */
    public ReconnectionResponse(String nickname, String password) {
        super("Login done correctly.", nickname);
        this.password = password;
    }

    /**
     * Getter for the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the message for the gui
     * @return the message
     */
    @Override
    public String getMessage() {
        return nickname +" has reconnected to the game";
    }
}
