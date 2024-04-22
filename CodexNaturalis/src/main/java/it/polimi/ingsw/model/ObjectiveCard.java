package it.polimi.ingsw.model;

public abstract class ObjectiveCard {

    private int ID;
    public int points;

    public ObjectiveCard(int ID) {
        this.ID = ID;
    }
    public abstract ObjectiveCard getCard();
    public int getpoints(){
        return this.points;
    }
}