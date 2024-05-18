package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Game;

/**
 * Event that represent the notification that the game is starting
 */
public class StartGame extends GenericEvent{
    public final Game model;
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public StartGame(String nickname, Game model){
        super("The game is starting",nickname);
        this.model = model;
    }
}
