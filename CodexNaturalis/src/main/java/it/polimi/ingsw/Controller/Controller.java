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

        // creo listener
        if(lobby.getNumPlayers()==0 || mvListeners.size() < lobby.getNumPlayers()) {
            ModelViewListener mvListener = new ModelViewListener(server);
            mvListeners.add(mvListener);

            //check game hasn't started
            if (model == null) {
                if (lobby.getNumPlayers()==0) {
                    // se la lobby è vuota evento SetNumPlayer
                    NumPlayersRequest numPlayersRequest = new NumPlayersRequest(nickname);
                    mvListener.addEvent(numPlayersRequest);
                }
                if(!lobby.addPlayer(nickname))
                    mvListener.addEvent(new ErrorJoinLobby(nickname));
            }
            else{
                mvListener.addEvent(new ErrorJoinLobby(nickname));
            }

        }
    }

    public Game getGame(){
        return this.model;
    }
    public void updateModel(GenericEvent event, String nickname){
        //TODO agire sul model in base al tipo di evento
        if (event instanceof NumPlayersResponse){
            lobby.setNumPlayers(((NumPlayersResponse) event).numPlayers);
        }
        //e cosi via agendo sul model
    }
}
