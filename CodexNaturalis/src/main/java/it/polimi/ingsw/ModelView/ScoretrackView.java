package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Scoretrack;
import it.polimi.ingsw.Model.Token;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represent the situation of the score track in a certain moment
 */
public class ScoretrackView implements Serializable {
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
    }
}
