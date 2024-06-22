package it.polimi.ingsw.Events;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Event that represent the end of the game and its final rankings
 */
public class FinalRankings extends ServerMessage{
    /**
     * represent a map between every player and his points
     */
    public final HashMap<String,Integer> rankings;

    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param rankings the final rankings
     */
    public FinalRankings(String nickname, HashMap<String,Integer> rankings){
        super("The final rankings are:\n ",nickname);
        this.rankings = rankings;
    }

    @Override
    public String msgOutput() {

        if(rankings == null)
            return("Since you are the only player left playing, you win!");

        int initialSize= rankings.size();

        //create a new hashmap and sort it from the beginning one
        LinkedHashMap<String, Integer> tmp = new LinkedHashMap<>();
        for(int i = 0; i < initialSize; i++){
            sortHashmap(rankings, tmp);
        }

        String m= message;
        int count=1;
        for (String key : tmp.keySet()) {
            //ora sono già in ordine
            m = m.concat("\t"+ count+". "+key+": "+tmp.get(key)+"\n"); //string is immutable
           count++;
        }
       return m;
    }


    @Override
    public String getMessage() {
        return msgOutput();
    }

    public static void sortHashmap(HashMap<String, Integer> map, LinkedHashMap<String, Integer> sorted) {

        if (map.isEmpty()) {
            return; //should never happen
        }

        // initialize maxValue with the minimum integer value
        int maxValue = -1;
        String tmp = "";
        // iterate through the values of the map
        for (String nickname : map.keySet()) {
            // Update maxValue if a larger value is found and removes from the hashmap
            if (map.get(nickname) > maxValue) {
                maxValue = map.get(nickname);
                tmp = nickname;
            }
        }
        sorted.put(tmp, maxValue);
        map.remove(tmp);
    }
}
