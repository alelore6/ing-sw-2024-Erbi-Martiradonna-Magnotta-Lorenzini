package it.polimi.ingsw.model;

public abstract class ObjectiveCard {

     protected int ID;
    protected int points;

    public ObjectiveCard(int ID) {
        this.ID = ID;
    }
    public abstract ObjectiveCard getCard();
    public int getpoints(){
        return this.points;
    }
}