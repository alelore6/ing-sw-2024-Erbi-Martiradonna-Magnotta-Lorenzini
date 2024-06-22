package it.polimi.ingsw.Events;

public class PingMessage extends ServerMessage{

    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public PingMessage(String nickname) {
        super(null, nickname);
    }
}
