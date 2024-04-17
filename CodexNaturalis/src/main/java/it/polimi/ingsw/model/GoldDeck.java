package it.polimi.ingsw.model;

import java.util.stream.IntStream;

public class GoldDeck extends Deck{
    private final GoldCard[] cards;
    public GoldDeck(){
        // the first card of the deck will be the one with the highest index, i.e. NCards - 1
        cards = new GoldCard[40];

        // TODO: insert cards randomly
        IntStream.range(0, 40).forEach(i -> cards[i] = new GoldCard(40 + i));
    }
    public GoldCard draw() throws isEmptyException{
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;
            return cards[NCards];
        }
        // WARNING! We don't delete the card from the array doing this.
    }
}
