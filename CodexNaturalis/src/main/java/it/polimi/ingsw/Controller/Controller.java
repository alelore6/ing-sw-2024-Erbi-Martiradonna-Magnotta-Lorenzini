package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.ErrorJoinLobby;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Events.StartGame;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;

public class Controller {

    private final ServerImpl server;
    private final Lobby lobby;
    private Game model;
    private ModelViewListener[] mvListeners;
    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();

    }

    protected void createGame(){
        String[] nicknames = new String[lobby.getPlayers().size()];
        nicknames = lobby.getPlayers().toArray(nicknames);
        model = new Game(lobby.getNumPlayers(), nicknames, mvListeners);

        //TODO notify all listener on startGame event

        getGame().startGame();
    }

    public void endGame(int occasion){
        //( finchè non spostiamo)endGame viene gestito all'interno del model
        // al di fuori arriva solo la notifica che è stato triggerato
        getGame().endGame(occasion);
    }

    public void addPlayerToLobby(String nickname){
        boolean ok=false;
        //TODO creare listener
        //check game hasn't started
        if(model==null) {
            //TODO se la lobby è vuota evento SetNumPlayer
            ok = lobby.addPlayer(nickname);
        }
        else {
            //notify listener : ErrorJoinLobby
        }
        //TODO notify listener: joinLobby or ErrorJoinLobby event based on ok
    }

    public Game getGame(){
        return this.model;
    }
    public void updateModel(GenericEvent event, String nickname){
        //TODO agire sul model in base al tipo di evento
    }
}
