package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;

public class Controller implements ViewControllerListener {

    private final ServerImpl server;
    private final Lobby lobby;
    private Game model;

    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();

    }

    protected void createGame(){
        // model = new Game(lobby.getNumPlayers(), lobby.Nicknames, new ModelListener(server.Controller.model, server));
    }

    public void startGame(){
        getGame().startGame();
    }

    public void endGame(int occasion){
        //endGame viene gestito all'interno del model, al di fuori arriva solo la notifica che è stato triggerato
        getGame().endGame(occasion);
    }

    // TODO: why boolean?
    public boolean addPlayerToLobby(String nickname){

        return lobby.addPlayer(nickname);
    }

    public Game getGame(){
        return this.model;
    }

    @Override
    public void handleViewMsg(Generic msg) {

    }
}
