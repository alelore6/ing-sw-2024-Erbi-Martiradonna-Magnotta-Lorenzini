package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represent the player's info in a certain moment
 */
public class PlayerView implements Serializable {
    /**
     * the player's nickname
     */
    public final String nickname;
    /**
    the player's secret objective card
     */
    public final ObjectiveCard objectiveCard;
    /**
     * the player's token
     */
    public final TokenView token;
    /**
     * the player's hand
     */
    public final HandView hand;
    /**
     * the player's current resources
     */
    public final HashMap<Resource,Integer> currentResources;

    /**
     * Constructor that works like a clone method
     * @param player the player in the game
     */
    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.objectiveCard = player.getObjective();
        this.token = new TokenView(player.getToken());
        this.hand= new HandView(player.getHand());
        this.currentResources=player.getCurrentResources().getCurrentResources();
    }
}
