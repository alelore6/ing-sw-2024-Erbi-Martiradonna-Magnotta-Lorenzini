package it.polimi.ingsw.controller;

import it.polimi.ingsw.Listener.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;

public class Controller {
    private final ServerImpl server;
    private final Lobby lobby;

    public Game game;

    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();
    }

    protected void createGame(){
        game = new Game(lobby.getNumPlayers(), lobby.Nicknames, new ModelViewListener(server.controller.game, server));

        //faccio subito startGame?
        game.startGame();
    }
    public boolean addPlayerToLobby(String nickname){

        return lobby.addPlayer(nickname);
    }

}
