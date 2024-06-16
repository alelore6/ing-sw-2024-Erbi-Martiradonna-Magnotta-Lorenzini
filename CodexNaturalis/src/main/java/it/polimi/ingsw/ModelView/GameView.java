package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * represent the game info in a certain moment like a screenshot. Used to send infos through the net
 */
public class GameView implements Serializable {
    /**
     * the list of player and theirs info
     */
    public final ArrayList<PlayerView> players;
    /**
     * the number of players
     */
    public final int numPlayers;
    /**
     * the table center info
     */
    public final TableCenterView tableCenterView;

    /**
     * Constructor that works like a clone method
     * @param model the game
     */
    public GameView(Game model) {
        this.numPlayers = model.getNumPlayers();
        this.players = new ArrayList<PlayerView>();
        for(int i=0;i<numPlayers;i++){
            players.add(new PlayerView(model.players[i]));
        }
        this.tableCenterView = new TableCenterView(model.tablecenter);
    }

    /**
     * getter for a player's info
     * @param nickname the player selected
     * @return the player's info
     */
    public PlayerView getPlayerViewByNickname(String nickname){
        for(int i=0;i<players.size();i++){
            if(players.get(i).nickname.equals(nickname)){
                return players.get(i);
            }
        }
        throw new RuntimeException("Player not found");
    }
}
