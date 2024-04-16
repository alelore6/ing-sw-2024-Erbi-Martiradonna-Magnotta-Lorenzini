package it.polimi.ingsw.model;

import java.util.stream.IntStream;

public class ResourceDeck extends Deck{
    private ResourceCard[] cards;
    public ResourceDeck(){
        // the first card of the deck will be the one with the highest index i.e. NCards - 1
        cards = new ResourceCard[40];

        // TODO: insert cards randomly
        IntStream.range(0, 40).forEach(i -> cards[i] = new ResourceCard(40 + i));
    }
    public ResourceCard draw() throws isEmptyException{
        if(getNCards() == 0){
            throw new isEmptyException();
        }
        else {
            NCards--;
            return cards[NCards];
        }
        // WARNING! We don't delete the card from the array doing this.
    }
}
