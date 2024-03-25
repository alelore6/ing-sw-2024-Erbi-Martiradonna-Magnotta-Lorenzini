package it.polimi.ingsw.model;

public class ResourceDeck extends Deck{
    private ResourceCard[] cards;
    public ResourceDeck(){
        // the first card of the deck will be the one with the highest index i.e. NCards - 1
        cards = new ResourceCard[40];
        //% insert the 40 cards from DB randomly
    }
    public ResourceCard draw() throws isEmptyException{
        if(getNCards() == 0){
            throw new isEmptyException();
        }
        else {
            NCards--;
            return cards[NCards];
        }
        //% così però non si elimina la carta dal vettore
    }
}
