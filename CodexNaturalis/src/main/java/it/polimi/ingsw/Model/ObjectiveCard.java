package it.polimi.ingsw.Model;

import java.io.Serializable;

public abstract class ObjectiveCard implements Serializable {

    protected int ID;
    protected int points;

    public int getPoints(){
        return points;
    }
    public int getID(){
        return this.ID;
    }
    public ObjectiveCard getObjectiveCard(){
        return this;
    }
}