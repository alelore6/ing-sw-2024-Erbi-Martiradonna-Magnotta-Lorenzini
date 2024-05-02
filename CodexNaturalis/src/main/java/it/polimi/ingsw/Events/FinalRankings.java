package it.polimi.ingsw.Events;

import java.util.HashMap;

public class FinalRankings extends GenericEvent{
    private final HashMap<String,Integer> Rankings;
    public FinalRankings(String nickname, HashMap<String,Integer> rankings){
        super("The final rankings are: ",nickname);
        Rankings = rankings;
    }
}
