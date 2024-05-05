package it.polimi.ingsw.Events;

/**
 * Generic event that represents the success or failure of the action.
 * if it's negative the action will be redone.
 */
public class AckResponse extends GenericEvent{
    /**
     * the result of the action
     */
    public final boolean ok;
    /**
     * the corresponding event
     */
    public final GenericEvent event;

    /**
     * Constructor
     * @param ok the result of the action
     * @param nickname the player that did the action
     * @param event the corresponding event
     */
    public AckResponse(boolean ok, String nickname, GenericEvent event){
        super(ok ? "Event has gone successfully" : "Event has gone wrong. Please try again", nickname);
        this.ok = ok;
        this.event = event;
    }
}
