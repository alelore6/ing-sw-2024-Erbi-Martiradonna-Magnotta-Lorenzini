package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.Model.Player;

/**
 * Exception that is thrown when a draw is requested but the hand is full
 * This situation should never happen
 */
public class HandFullException extends Throwable {
    /**
     * the player that causes the exception
     */
    public final Player player;

    /**
     * Constructor
     * @param player the player that causes the exception
     */
    public HandFullException(Player player){
        super("Hand full: cant draw a new card");
        this.player=player;
    }
}
