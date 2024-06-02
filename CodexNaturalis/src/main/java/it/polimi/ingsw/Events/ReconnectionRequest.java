package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Client;

public class ReconnectionRequest extends GenericRequest{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ReconnectionRequest(String nickname) {
        super("It has been found a disconnected player with this nickname. Enter the password associated with that player:", nickname);
    }
}
