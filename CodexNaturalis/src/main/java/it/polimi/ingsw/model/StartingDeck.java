package it.polimi.ingsw.model;

public class StartingDeck {
    private StartingCard[] startcards;

    public StartingDeck(){
        startcards = new StartingCard[6];
    }
    public ObjectiveCard draw() {
        return startcards;
    }


}