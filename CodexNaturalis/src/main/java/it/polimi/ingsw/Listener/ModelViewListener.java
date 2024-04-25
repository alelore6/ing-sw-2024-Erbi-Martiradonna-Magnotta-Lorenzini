package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Network.Client;
import it.polimi.ingsw.Network.GameServer;
import it.polimi.ingsw.model.Game;

public final class ModelViewListener {
    //listen to model request to change view
    //one for every player?
    Game game;
    GameServer server;
    public ModelViewListener(Game game, GameServer server){
        this.game=game;
        this.server=server;
    }

    public void sendMessageAll(String message){

    }

    public void sendMessage(String message, String nickname){
        Client c=server.findClient(nickname);
        //c.view.update() ????
    }
}
