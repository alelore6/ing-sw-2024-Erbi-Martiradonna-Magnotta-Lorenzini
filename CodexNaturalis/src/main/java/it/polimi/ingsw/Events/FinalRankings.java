package it.polimi.ingsw.Events;

import java.util.HashMap;

/**
 * Event that represent the end of the game and its final rankings
 */
public class FinalRankings extends GenericEvent{
    /**
     * represent a map between every player and his points
     */
    public final HashMap<String,Integer> Rankings;

    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param rankings the final rankings
     */
    public FinalRankings(String nickname, HashMap<String,Integer> rankings){
        super("The final rankings are: ",nickname);
        Rankings = rankings;
        mustBeSentToAll=true;
    }


}
