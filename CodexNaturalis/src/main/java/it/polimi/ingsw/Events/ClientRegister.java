package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.View.UI;

import java.io.Serializable;

/**
 * event that represent a client request to connect to the server
 */
public class ClientRegister extends GenericEvent {
    /**
     * Constructor
     * @param client the client that is connecting
     */
    public ClientRegister(ClientImpl client) {
        super("Connecting...", client.getNickname());
    }

    /**
     * Getter for the nickname of the client
     * @return the client's nickname
     */
    public String getNickname() {
        return this.nickname;
    }
}
