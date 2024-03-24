package main.java.it.polimi.ingsw.model;

public class Hand {
    PlayableCard[] HandCard;

    Hand(){
        HandCard= new PlayableCard[3];
        HandCard[0]= DrawFromDeck(ResourceDeck, 0);
        HandCard[1]= DrawFromDeck(ResourceDeck, 1);
        HandCard[2]= DrawFromDeck(GoldDeck, 2);
    }

    public void DrawFromDeck(Deck deck, int posHand){
        //la scelta del deck da cui pescare sarà dell'utente
        HandCard[posHand]= deck.draw();
    }

    public void DrawPositionedCard( PlayableCard card, int posHand){
        //l'utente sceglie direttamente la carta presente in tableCenter da pescare
        HandCard[posHand]= TableCenter.drawAndPosition(card);
    }

    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

    public void playCard(PlayableCard card ){
        // mi servono sapere gli angoli che voglio andare a coprire
        // come li recupero? Oppure li prendo in input?

        // gioca carta

        CurrentResources.update(card);
    }
}
