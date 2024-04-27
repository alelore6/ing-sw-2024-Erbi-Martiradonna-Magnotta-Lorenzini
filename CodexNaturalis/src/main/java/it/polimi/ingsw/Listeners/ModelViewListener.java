package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Messages.Generic;

public class ModelViewListener {
    Client client;

    void handleModelMessage(Generic msg){}

    /*// listens to model request to change view
    // one for each player?
    Game game;
    ServerImpl server;

    public ModelViewListener(Game game, ServerImpl server){
        this.game = game;
        this.server = server;
    }

    public void sendMessageAll(String message){

    }

    public void sendMessage(String message, String nickname){
        ClientImpl c = server.findClient(nickname);
        //c.view.update() ????
    }*/
}
