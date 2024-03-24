package it.polimi.ingsw.model;

public class ResourceDeck extends Deck{
    private ResourceCard[] cards;
    public ResourceDeck(){
        //si considera la prima carta del mazzo quella con indice più alto
        cards = new ResourceCard[40];
        //% mettere in ordine randomico le 40 carte dal database
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
