package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Network.GameServer;

public class Controller {
    public Game GameModel;
    private final GameServer gameServer;
    private final Lobby lobby;

    public Controller(GameServer gameServer){
        this.gameServer=gameServer;
        lobby= new Lobby();
    }
    protected void createGame(){
        GameModel= new Game(lobby.numPlayers,lobby.Nicknames);
        //va qui?
        GameModel.startGame();
    }
    public boolean addPlayerToLobby(String nickname){
        if(lobby.numPlayers==0){
            // find the client with this nickname
            gameServer.findClient(nickname);
            // and ask him to set numplayers (a lui o tramite il suo listener?)
            lobby.setNumPlayers(4);
        }
        return lobby.addPlayer(nickname);
    }

}
