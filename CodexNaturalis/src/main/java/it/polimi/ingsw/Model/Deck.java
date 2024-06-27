package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class that handles all the methods of all kinds of deck in the game.
 * It implements Serializable because through serialization is possible to transmit objects between different machines in a network.
 */
public abstract class Deck implements Serializable {
    /**
     * The integer used to count the number of cards in a deck.
     */
    protected int NCards;
    /**
     * A boolean of the ack to let us know when the deck is empty, obviously i the beginning it will be false because the deck should be full.
     */
    protected boolean AckEmpty=false;

    /**
     * Method to draw a card from a deck.
     * @return the drawn card
     * @throws isEmptyException if deck is empty
     */
    public abstract Card draw() throws isEmptyException;

    /**
     * Method to know the exact number of cards in the deck.
     * @return the integer of number of cards.
     */
    public int getNCards(){
        return NCards;
    }

    /**
     * This method shuffle the cards of the deck to permit the randomness of the draw.
     * @param deck
     * @return the deck with the order of card inside changed.
     */
    protected Card[] shuffle(Card[] deck){
        List<Card> cardList = Arrays.asList(deck);

        Collections.shuffle(cardList);

        cardList.toArray(deck);

        return deck;
    }
}
