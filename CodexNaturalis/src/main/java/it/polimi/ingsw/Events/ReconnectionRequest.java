package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Client;

/**
 * when a player enter a nickname of a disconnected player a possibility of reconnection is communicated through this event
 */
public class ReconnectionRequest extends GenericRequest{
    /**
     * Constructor
     *
     * @param nickname player that receives the event
     */
    public ReconnectionRequest(String nickname) {
        super("It has been found a disconnected player with this nickname. Enter the password associated with that player:", nickname);
    }
}
