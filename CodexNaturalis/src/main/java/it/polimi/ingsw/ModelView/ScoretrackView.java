package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Scoretrack;
import it.polimi.ingsw.Model.Token;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represent the situation of the score track in a certain moment
 */
public class ScoretrackView implements Serializable {

    /**
     * The array of nicknames present in the hash map
     */
    private final String[] nicknames;

    /**
     * the map between every player's nickname and his points
     */
    public final HashMap<String,Integer> points;

    /**
     * Constructor that works like a clone method
     * @param scoretrack the game's score track
     */
    public ScoretrackView(Scoretrack scoretrack){
        points=scoretrack.getRankings();
        nicknames = points.keySet().toArray(new String[points.size()]);
    }

    /**
     * Method to get a nickname with the corresponding color. (TODO)
     * @param index the position in the array.
     * @return the colored nickname or null if index is out of bound.
     */
    public String nickToString(int index){
        return index >= 0 && index < nicknames.length ? nicknames[index] : null;
    }
}
