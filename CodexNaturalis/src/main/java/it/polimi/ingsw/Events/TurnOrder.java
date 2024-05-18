package it.polimi.ingsw.Events;

/**
 * Event that represent the notification of the turns order in the game
 */
public class TurnOrder extends GenericEvent{
    private static final long serialVersionUID = 25L;
    /**
     * the player's turns order
     */
    public final String[] order;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param order the turns order
     */
    public TurnOrder(String nickname, String[] order) {
        super("The game turn order is", nickname);
        this.order = order;
    }
}
