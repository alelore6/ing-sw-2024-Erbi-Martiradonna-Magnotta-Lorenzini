package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.ModelView.GameView;

/**
 * Generic event that represents the success or failure of the action.
 * if it's negative the action will be redone.
 */
public class AckResponse extends GenericResponse{
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
    public AckResponse(String nickname, GenericEvent event, GameView gameView){
        //per esito positivo e per aggiornare view
        super("Event has gone successfully", nickname);
        this.ok = true;
        this.event = event;
        mustBeSentToAll=true;
        this.gameView = gameView;
    }

    public AckResponse(String errorMessage, String nickname, GenericEvent event){
        //solo per esito negativo
        super(errorMessage, nickname);
        this.ok = false;
        this.event = event;
        mustBeSentToAll=true;
        this.gameView = null;
    }
    public AckResponse( String nickname, GenericEvent event){
        //per esito positivo senza aggiornare view
        //togliere il booleano da input e sistemare utilizzi
        super("Event has gone successfully", nickname);
        this.ok = true;
        this.event = event;
        mustBeSentToAll=true;
        this.gameView = null;
    }
}
