package it.polimi.ingsw.model;

public class ObjectiveDeck {
    private ObjectiveCard[] objcards;

    public ObjectiveDeck(){
        objcards = new ObjectiveCard[16];
    }
    public ObjectiveCard draw() {
        return objcards;
    }


}