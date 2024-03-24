package it.polimi.ingsw.model;

public class GoldDeck extends Deck{
    private GoldCard[] cards;
    public GoldDeck(){
        //si considera la prima carta del mazzo quella con indice più alto
        cards = new GoldCard[40];
        //% mettere in ordine randomico le 40 carte dal database
    }
    public GoldCard draw() throws isEmptyException{
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
