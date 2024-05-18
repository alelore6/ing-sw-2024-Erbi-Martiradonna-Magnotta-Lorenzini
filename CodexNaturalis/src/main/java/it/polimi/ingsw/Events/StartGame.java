package it.polimi.ingsw.Events;

/**
 * Event that represent the notification that the game is starting
 */
public class StartGame extends GenericEvent{
    private static final long serialVersionUID = 23L;
    //TODO passare il model completo?????
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public StartGame(String nickname){
        super("The game is starting",nickname);
    }
}
