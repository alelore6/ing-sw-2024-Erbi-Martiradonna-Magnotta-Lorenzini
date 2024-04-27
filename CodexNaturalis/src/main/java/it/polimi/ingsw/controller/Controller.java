package it.polimi.ingsw.controller;

import it.polimi.ingsw.Listener.ModelListener;
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
        // model = new Game(lobby.getNumPlayers(), lobby.Nicknames, new ModelListener(server.controller.model, server));
    }

    public void startGame(){
        getGame().startGame();
    }

    public void endGame(int occasion){
        getGame().endGame(occasion);
    }

    // TODO: why boolean?
    public boolean addPlayerToLobby(String nickname){

        return lobby.addPlayer(nickname);
    }

    public Game getGame(){
        return this.model;
    }
}
