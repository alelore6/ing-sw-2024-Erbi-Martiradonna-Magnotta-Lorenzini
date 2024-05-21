package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.ModelView.GameView;

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

    public final GameView gameView;
    /**
     * Constructor
     * @param ok the result of the action
     * @param nickname the player that did the action
     * @param event the corresponding event
     */
    public AckResponse(boolean ok, String nickname, GenericEvent event, GameView gameView){
        super(ok ? "Event has gone successfully" : "Event has gone wrong. Please try again", nickname);
        this.ok = ok;
        this.event = event;
        mustBeSentToAll=ok;
        this.gameView = gameView;
    }
}
