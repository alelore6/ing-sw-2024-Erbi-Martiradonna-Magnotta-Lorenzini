package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.model.Game;

public final class ModelViewListener {
    //listen to model request to change view
    //one for every player?
    Game game;
    ServerImpl server;
    public ModelViewListener(Game game, ServerImpl server){
        this.game=game;
        this.server=server;
    }

    public void sendMessageAll(String message){

    }

    public void sendMessage(String message, String nickname){
        ClientImpl c=server.findClient(nickname);
        //c.view.update() ????
    }
}
