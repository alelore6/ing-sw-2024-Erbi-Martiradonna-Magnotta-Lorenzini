package it.polimi.ingsw.model;

public class GoldDeck extends Deck{
    private GoldCard[] cards;
    public GoldDeck(){
        // the first card of the deck will be the one with the highest index i.e. NCards - 1
        cards = new GoldCard[40];
        //% insert the 40 cards from DB randomly
    }
    public GoldCard draw() throws isEmptyException{
        if(getNCards() == 0){
            throw new isEmptyException();
        }
        else {
            NCards--;
            return cards[NCards];
        }
        //% we don't delete the card from the array doing this
    }
}
