package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Scoretrack;
import it.polimi.ingsw.Model.Token;

import java.io.Serializable;
import java.util.HashMap;

public class ScoretrackView implements Serializable {
    public final HashMap<String,Integer> points;

    public ScoretrackView(Scoretrack scoretrack){
        points=scoretrack.getRankings();
    }
}
