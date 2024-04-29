package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.PlayerNotFoundException;

import java.util.ArrayList;

// WATCH OUT!   The lobby exists w/o the actual players. Only nicknames are used in this class
//              and players will be created after the game.

public class Lobby {

    private ArrayList<String> players;
    private final Object lock_players = new Object();

    public Lobby() {
        this.players = new ArrayList<String>();
    }

    protected boolean addPlayer(String nickname){
        synchronized (lock_players){
            if(players.size() < 4){
                return players.add(nickname);
            }

            return false;
        }
        // notify listeners
    }

    public void disconnect(String nickname) throws PlayerNotFoundException {
        boolean isRemoved = false;

        synchronized (lock_players){
            for( int i = 0; i < players.size(); i++){
                if(players.get(i).equals(nickname)){
                    players.remove(i);
                    isRemoved = true;

                    break;
                }
            }

            if(!isRemoved){
                throw new PlayerNotFoundException(nickname);
            }
        }
        // notify listeners
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public int getNumPlayers(){
        return players.size();
    }
}
