package it.polimi.ingsw.Events;

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
