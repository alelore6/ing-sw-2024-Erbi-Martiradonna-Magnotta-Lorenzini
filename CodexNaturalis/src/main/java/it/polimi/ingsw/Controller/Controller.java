package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;

public class Controller {

    private final ServerImpl server;
    private final Lobby lobby;
    private Game model;

    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();

    }

    protected void createGame(){
        String[] temp = new String[lobby.getPlayers().size()];
        temp = lobby.getPlayers().toArray(temp);

        model = new Game(lobby.getNumPlayers(), temp, ); // TODO: PASSARE I LISTENER O CREARLI DIRETTAMENTE PER OGNI PLAYER PER IL COSTRUTTORE DI GAME
    }

    public void startGame(){
        getGame().startGame();
    }

    public void endGame(int occasion){
        //( finchè non spostiamo)endGame viene gestito all'interno del model
        // al di fuori arriva solo la notifica che è stato triggerato
        getGame().endGame(occasion);
    }

    public boolean addPlayerToLobby(String nickname){
        return lobby.addPlayer(nickname);
    }

    public Game getGame(){
        return this.model;
    }

}
