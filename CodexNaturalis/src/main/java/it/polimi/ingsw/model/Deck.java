package it.polimi.ingsw.model;

public abstract class Deck {
    protected int NCards;
    protected boolean AckEmpty=false;
    public abstract PlayableCard draw() throws isEmptyException;
    public int getNCards(){
        return NCards;
    }
}
