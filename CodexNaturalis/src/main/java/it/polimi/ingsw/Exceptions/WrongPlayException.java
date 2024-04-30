package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;

/**
 * Exception that is thrown if the play is not valid
 * contains the information about the play
 */
public class WrongPlayException extends Throwable{
    /**
     * the player that makes the play
     */
    public final Player player;
    /**
     * message that will be sent to the client describing the cause of the exception
     */
    public final String message;
    /**
     * the card that should have been played
     */
    public final Card card;

    /**
     * Constructor
     * @param player the player that causes the exception
     * @param x the x-axis coordinate where the card cant be played
     * @param y the y-axis coordinate where the card cant be played
     * negative values for x and y represent specific types of error
     * @param card the card that cant be played
     */
    public WrongPlayException(Player player,int x,int y, Card card){
        this.player=player;
        this.card=card;
        if (x==-1 && y==-1){
            message= "Error: the card cant be found in the hand of the player";
        } else if (x==-2 && y==-2) {
            message= "Error: the card doesnt overlap any corner";
        } else if (x==-3 && y==-3) {
            message="Error: the player doesnt have enough resources to play that card";
        } else message= "Error: the card cant be played in the position: "+x+","+y;
    }
}
