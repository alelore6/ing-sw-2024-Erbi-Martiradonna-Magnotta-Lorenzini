package it.polimi.ingsw.model;

public class ObjectiveDeck {
    private ObjectiveCard[] objcards;

    public ObjectiveDeck(){
        objcards = new ObjectiveCard[16];
    }
    public ObjectiveCard draw() {
        //TODO da sistemare
        return objcards[0];
    }


}