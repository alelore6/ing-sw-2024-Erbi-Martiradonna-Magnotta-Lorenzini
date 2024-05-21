package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;

/**
 * Event that represent the notification that the game is starting
 */
public class StartGame extends GenericEvent{

    public final GameView model;
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public StartGame(String nickname, GameView model){
        super("The game is starting", nickname);
        this.model = model;
        mustBeSentToAll = true;
    }
}
