package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification that the game is starting
 */
public class StartGame extends GenericEvent{
    /**
     * the game info at the moment of the event
     */
    private final GameView model;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param model the game info
     */
    public StartGame(String nickname, GameView model){
        super("The game is starting...", nickname);
        this.model = model;
    }
}
