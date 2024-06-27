package it.polimi.ingsw.Events;

/**
 * Event that represent the client choice of a nickname
 */
public class ChooseNickname extends GenericEvent{
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ChooseNickname(String message, String nickname) {
        super(message, nickname);
        mustBeSentToAll=true;
    }
}
