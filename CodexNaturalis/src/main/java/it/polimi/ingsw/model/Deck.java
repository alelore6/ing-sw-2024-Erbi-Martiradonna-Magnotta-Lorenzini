package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.isEmptyException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Deck {
    protected int NCards;
    protected boolean AckEmpty=false;
    public abstract Card draw() throws isEmptyException;
    public int getNCards(){
        return NCards;
    }

    protected Card[] shuffle(Card[] deck){
        List<Card> cardList = Arrays.asList(deck);

        Collections.shuffle(cardList);

        cardList.toArray(deck);

        return deck;
    }
}
