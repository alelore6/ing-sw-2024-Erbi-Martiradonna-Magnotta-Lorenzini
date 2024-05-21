package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.*;

import java.io.Serializable;

public class TableCenterView implements Serializable {

    public final ObjectiveCard[] objCards;
    public final PlayableCard[] centerCards;//first two are resource cards, last two are gold
    public final ScoretrackView scoreTrack;

    public TableCenterView(TableCenter tableCenter) {
        objCards=tableCenter.getObjCards();
        centerCards=tableCenter.getCenterCards();
        scoreTrack=new ScoretrackView(tableCenter.getScoretrack());
    }
}
