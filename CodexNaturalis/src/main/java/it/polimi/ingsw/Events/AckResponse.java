package it.polimi.ingsw.Events;

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
    public final GenericRequest  request;
    public final GenericResponse response;

    public final GameView gameView;
    /**
     * Constructor
     * @param ok the result of the action
     * @param nickname the player that did the action
     * @param event the corresponding event
     */
    public AckResponse(String nickname, GenericResponse response, GameView gameView){
        //per esito positivo e per aggiornare view
        super("Event has gone successfully", nickname);
        this.ok = true;
        this.request = null;
        this.response = response;
        mustBeSentToAll=true;
        this.gameView = gameView;
    }

    public AckResponse(String errorMessage, String nickname, GenericRequest request){
        //solo per esito negativo
        super(errorMessage, nickname);
        this.ok = false;
        this.request = request;
        this.response = null;
        mustBeSentToAll=true;
        this.gameView = null;
    }
    public AckResponse(String nickname, GenericResponse response){
        //per esito positivo senza aggiornare view
        //togliere il booleano da input e sistemare utilizzi
        super("Event has gone successfully", nickname);
        this.ok = true;
        this.request = null;
        this.response = response;
        mustBeSentToAll=true;
        this.gameView = null;
    }

    public AckResponse(String errorMessage, String nickname, GenericRequest request, GameView gameView){
        //solo per esito negativo
        super(errorMessage, nickname);
        this.ok = false;
        this.request = request;
        this.response = null;
        mustBeSentToAll=true;
        this.gameView = gameView;
    }

    @Override
    public String msgOutput(){
        if(response == null)    return "";
        return "\n\u001B[30m" + (ok ? "\u001B[42m" + response.message : "\u001B[41m" + message) + "\u001B[0m";
    }
}
