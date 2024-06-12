package it.polimi.ingsw.Events;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        super("The final rankings are:\n ",nickname);
        Rankings = rankings;
        mustBeSentToAll=true;
    }

    @Override
    public String msgOutput() {

        int initialSize=Rankings.size();

        //create a new hashmap and sort it from the beginning one
        LinkedHashMap<String, Integer> tmp = new LinkedHashMap<>();
        for(int i = 0; i < initialSize; i++){
            sortHashmap(Rankings, tmp);
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


//    @Override
//    public String getMessage() {
//        String m=" ";
//        int count=1;
//        for (String key : Rankings.keySet()) {
//            //non sono sicuro siano già in ordine
//            m.concat("\t"+ count+". "+key+": "+Rankings.get(key)+"\n");
//            count++;
//        }
//        return message.concat(m);
//    }

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
        return;
    }
}
