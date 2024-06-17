package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.*;

import java.awt.*;
import java.io.Serializable;

/**
 * Represent the table center of the game in a certain moment
 */
public class TableCenterView implements Serializable {
    /**
     * the public objective cards
     */
    public final ObjectiveCard[] objCards;
    /**
     * the displayed cards
     */
    public final PlayableCard[] centerCards;//first two are resource cards, last two are gold
    /**
     * the score track info
     */
    public final ScoretrackView scoreTrack;

    public final CardColor topResourceCardColor;
    public final CardColor topGoldCardColor;
    /**
     * Constructor that works like a clone method
     * @param tableCenter
     */
    public TableCenterView(TableCenter tableCenter) {
        objCards=tableCenter.getObjCards();
        centerCards=tableCenter.getCenterCards();
        scoreTrack=new ScoretrackView(tableCenter.getScoretrack());
        this.topResourceCardColor = tableCenter.getResDeck().peek();
        this.topGoldCardColor = tableCenter.getGoldDeck().peek();
    }
}
