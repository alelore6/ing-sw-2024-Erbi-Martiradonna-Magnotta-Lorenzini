package it.polimi.ingsw.model;

/**
 * Exception that is thrown when a draw is requested but the hand is full
 * This situation should never happen
 */
public class HandFullException extends Throwable {
    public final Player player;

    /**
     * Constructor
     * @param player the player that causes the exception
     */
    HandFullException(Player player){
        this.player=player;
    }
}
