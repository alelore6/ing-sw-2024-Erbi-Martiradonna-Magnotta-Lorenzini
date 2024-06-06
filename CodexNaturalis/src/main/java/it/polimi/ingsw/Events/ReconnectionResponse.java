package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Client;

public class ReconnectionResponse extends GenericResponse{
    // This attribute is for registering the client after a disconnection and it'll be assigned by the server
    // after a correct login.
    public transient Client client;

    private final String password;
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ReconnectionResponse(String nickname, String password) {
        super("Login done correctly.", nickname);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
