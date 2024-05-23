package it.polimi.ingsw.Events;

public class ReconnectionRequest extends GenericRequest{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ReconnectionRequest(String nickname) {
        super("It has been found a disconnected player with this nickname. If it's you enter your password,\n else set one but know that game has already started: no new players can join ", nickname);
    }
}
