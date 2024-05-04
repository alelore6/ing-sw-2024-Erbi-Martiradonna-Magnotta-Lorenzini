package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.util.ArrayList;

public class Controller {

    private final ServerImpl server;
    private final Lobby lobby;
    private Game model;
    private ArrayList<ModelViewListener> mvListeners;
    private int numPlayers;
    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();


    }

    protected void createGame(){
        String[] nicknames = new String[lobby.getPlayers().size()]; //crea l'array di nicknames dei player
        nicknames = lobby.getPlayers().toArray(nicknames); //fills the nicknames array
        model = new Game(lobby.getNumPlayers(), nicknames, mvListeners);


        for(int i = 0; i < mvListeners.size(); i++){
            //NOTIFY ALL LISTENERS OF STARTGAME EVENT
            mvListeners.get(i).addEvent(new StartGame(lobby.getPlayers().get(i)));
        }

        getGame().startGame();
    }

    public void endGame(int occasion){
        //( finchè non spostiamo)endGame viene gestito all'interno del model
        // al di fuori arriva solo la notifica che è stato triggerato
        getGame().endGame(occasion);
    }

    public void addPlayerToLobby(String nickname){
        boolean ok=false;



        //check game hasn't started
        if(model==null) {
            //se la lobby è vuota evento SetNumPlayer
            if(lobby.getPlayers().size() == 0){
                ok = lobby.addPlayer(nickname);

            }

            if(mvListeners.size() < lobby.getNumPlayers()){
                mvListeners.add(new ModelViewListener(server));
            }

            if(lobby.getPlayers().size() == 1) {
                NumPlayersRequest event = new NumPlayersRequest(nickname);
                mvListeners.getFirst().addEvent(event);
            }
        }
        else {
            //notify listener : ErrorJoinLobby
            //how do I notify listener if the player didn't correctly enter the lobby?
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
