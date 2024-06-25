package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.PlayerNotFoundException;

import java.util.ArrayList;

/**
 * Class that represents the waiting lobby before the game starts.
 * The lobby exists w/o the actual players. Only nicknames are used in this class and players will be created after the game creates.
 */
public class Lobby {
    /**
     * represents the number of players in the game, is set by the first player to join the lobby.
     */
    private int numPlayers;
    /**
     * List of players that are waiting in the lobby for the game to start.
     */
    private ArrayList<String> players;
    private final Object lock_players = new Object();
    //non capisco a cosa serva: il controller non è su thread

    /**
     * Constructor: numPlayers is set to 0.
     */
    public Lobby(){
        this.players = new ArrayList<String>();
        this.numPlayers = 0;
    }

    /**
     * Add a player to the lobby
     * @param nickname the nickname of the player the is being added
     * @return the result of tha action
     */
    protected boolean addPlayer(String nickname){
        synchronized (lock_players){
            if(players.size() == 0 || players.size() < numPlayers){
                return players.add(nickname);
            }
            return false;
        }
    }



    /**
     * Getter for players list
     * @return players list
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Getter for the number of  players for the game
     * @return the number of  players for the game
     */
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * Setter for the number of  players for the game
     * @param numPlayers the number of  players for the game
     */
    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }
}
