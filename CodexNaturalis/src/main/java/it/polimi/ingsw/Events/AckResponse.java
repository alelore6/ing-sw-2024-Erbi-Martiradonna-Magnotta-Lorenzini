package it.polimi.ingsw.Events;

import it.polimi.ingsw.ModelView.GameView;

/**
 * Generic event that represents the success or failure of the action.
 * If it's negative, the action will be redone.
 */
public class AckResponse extends GenericResponse{
    /**
     * the result of the action
     */
    public final boolean ok;
    /**
     * The event corresponding to the ack.
     */
    public final GenericEvent receivedEvent;
    /**
     * The current game view.
     */
    public final GameView gameView;

    /**
     * Constructor for positive acks with an update on game view.
     *
     * @param nickname the player that did the action
     * @param response the event corresponding to the ack
     * @param gameView the current game view.
     */
    public AckResponse(String nickname, GenericResponse response, GameView gameView){
        super("Event has gone successfully", nickname);
        this.ok = true;
        this.receivedEvent = response;
        mustBeSentToAll=true;
        this.gameView = gameView;
    }

    /**
     * Constructor for generic acks without updating the game view.
     *
     * @param nickname the player that did the action.
     * @param message the string result.
     * @param response the event corresponding to the ack.
     * @param isOk the ack's outcome.
     */
    public AckResponse(String nickname, String message, GenericResponse response, boolean isOk){
        super(message, nickname);
        this.ok = isOk;
        this.receivedEvent = response;
        mustBeSentToAll=true;
        this.gameView = null;
    }

    /**
     * Constructor for telling the server that the client associated with the nickname has received the event.
     * @param nickname the client's nickname.
     * @param event received by the client.
     */
    public AckResponse(String nickname, ServerMessage event){
        super("", nickname);
        ok = true;
        receivedEvent = event;
        gameView = null;
    }

    @Override
    public String msgOutput(){
        if(receivedEvent == null)    return "";
        return "\n\u001B[30m" + (ok ? "\u001B[42m" + receivedEvent.message : "\u001B[41m" + message) + "\u001B[0m";
    }
}
