package it.polimi.ingsw.model;

public abstract class ObjectiveCard {

    protected int ID;
    protected int points;

    public ObjectiveCard(int ID) {
        this.ID = ID;
    }
    public abstract int getpoints();
    public int getID(){
        return this.ID;
    }
}