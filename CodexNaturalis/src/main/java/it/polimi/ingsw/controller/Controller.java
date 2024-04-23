package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Network.GameServer;

public class Controller {
    private Game GameModel;
    private GameServer gameServer;
    private Lobby lobby;

    Controller(GameServer gameServer){
        this.gameServer=gameServer;
        lobby= new Lobby();
    }
    protected void createGame(){
        GameModel= new Game(lobby.numPlayers,lobby.Nicknames);
        //va qui?
        GameModel.startGame();
    }
    protected boolean addPlayerToLobby(String nickname){
        //ha senso?
        if(lobby.addPlayer(nickname)){
            return true;
        }
        return false;
    }

}
