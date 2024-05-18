package it.polimi.ingsw.Model;

public abstract class ObjectiveCard {

    protected int ID;
    protected int points;

    public ObjectiveCard(int ID) {
        this.ID = ID;
    }
    public int getPoints(){return points;}
    public int getID(){
        return this.ID;
    }
    public ObjectiveCard getObjectiveCard(){
        return this;
    }
}