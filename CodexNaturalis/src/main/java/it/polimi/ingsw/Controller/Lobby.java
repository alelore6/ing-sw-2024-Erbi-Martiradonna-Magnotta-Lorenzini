package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Lobby {
    private ArrayList<Player> players;
    private final Object lock_players = new Object();

    public Lobby() {
        this.players = new ArrayList<Player>();
    }

    protected boolean addPlayer(String nickname){
        synchronized (lock_players){
            if(players.size() < 4){

                // vedi commento su Player
                return players.add(new Player(nickname));
            }

            return false;
        }
        // notify listeners
    }

    public void disconnect(String nickname) throws PlayerNotFoundException {
        synchronized (lock_players){
            for(Player player : players){
                if(player.getNickname().equals(nickname)){
                    players.remove(player);
                }
            }
        }
        // notify listeners
    }

    public int getNumPlayers(){
        return players.size();
    }
}
