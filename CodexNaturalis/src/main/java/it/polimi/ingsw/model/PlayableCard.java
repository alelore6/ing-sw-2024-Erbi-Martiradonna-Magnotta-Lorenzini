package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.Color.*;
import static it.polimi.ingsw.model.Position.*;

public abstract class PlayableCard extends Card{

    protected int points;
    protected Color color;

    protected int isChecked = 0;

    public PlayableCard(int ID) {
        super(ID);
    }

    public int getPoints(){
        return points;
    }
    public Color getColor(){
        return color;
    }
}
