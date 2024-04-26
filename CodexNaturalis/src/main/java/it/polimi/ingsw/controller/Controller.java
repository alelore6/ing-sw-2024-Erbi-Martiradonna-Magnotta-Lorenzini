package it.polimi.ingsw.controller;

import it.polimi.ingsw.Listener.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Network.Server.GameServer;

public class Controller {
    public Game game;
    private final GameServer server;
    private final Lobby lobby;

    public Controller(GameServer server){
        this.server=server;
        lobby= new Lobby();
    }
    protected void createGame(){
        game= new Game(lobby.numPlayers,lobby.Nicknames,new ModelViewListener(server.controller.game, server));
        //faccio subito startGame?
        game.startGame();
    }
    public boolean addPlayerToLobby(String nickname){
        if(lobby.numPlayers==0){
            game.mvListener.sendMessage("Set numPlayers",nickname);
            //vcListener will get the response and call lobby.setNumPlayers()
        }
        return lobby.addPlayer(nickname);
    }

}
