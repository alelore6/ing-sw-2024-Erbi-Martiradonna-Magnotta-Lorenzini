package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.PlayerNotFoundException;

import java.util.ArrayList;

// WATCH OUT!   The lobby exists w/o the actual players. Only nicknames are used in this class
//              and players will be created after the game creates.

public class Lobby {
    private int numPlayers;
    private ArrayList<String> players;
    private final Object lock_players = new Object();
    //non capisco a cosa serva: il controller non è su thread

    public Lobby(int numPlayers) {

        this.players = new ArrayList<String>();
        this.numPlayers = numPlayers;
    }

    protected boolean addPlayer(String nickname){
        synchronized (lock_players){
            if(players.size() < numPlayers){
                return players.add(nickname);
            }
            return false;
        }
    }

    public void disconnect(String nickname) throws PlayerNotFoundException {
        //capire cosa si vuole fare con questo metodo
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
        return numPlayers;
    }

}
