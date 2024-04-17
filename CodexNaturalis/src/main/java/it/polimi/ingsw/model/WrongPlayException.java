package it.polimi.ingsw.model;

/**
 * Exception that is thrown if the play is not valid
 * contains the information about the play
 */
public class WrongPlayException extends Throwable{
    public final Player player;
    public final String message;
    public final Card card;

    /**
     * Constructor
     * @param player the player that causes the exception
     * @param x the x-axis coordinate where the card cant be played
     * @param y the y-axis coordinate where the card cant be played
     * negative values for x and y represent specific types of error
     * @param card the card that cant be played
     */
    WrongPlayException(Player player,int x,int y, Card card){
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
