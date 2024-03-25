package it.polimi.ingsw.model;

public abstract class Deck {
    int NCards;
    public abstract PlayableCard draw() throws isEmptyException;
    public int getNCards(){
        return NCards;
    }
}
