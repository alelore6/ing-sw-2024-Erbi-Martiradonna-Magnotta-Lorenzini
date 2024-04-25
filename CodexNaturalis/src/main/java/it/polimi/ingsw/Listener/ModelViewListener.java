package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Network.Client;
import it.polimi.ingsw.model.Game;

public final class ModelViewListener {
    //listen to model request to change view
    //one for every player
    Game game;
    Client client;
    public ModelViewListener(Game game, Client client){
        this.game=game;
        this.client=client;
    }
}
