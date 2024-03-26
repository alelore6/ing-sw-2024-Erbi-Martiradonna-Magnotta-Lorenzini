package it.polimi.ingsw.model;

public class Hand {
    PlayableCard[] HandCard;
    private Card[][] displayedCards;
    private final Player player;
    Hand(Player player){
        HandCard= new PlayableCard[3];
        displayedCards=new Card[81][81];
        this.player=player;
    }

    public void DrawFromDeck(Deck deck, int posHand){
        //la scelta del deck da cui pescare sarà dell'utente
        try {
            HandCard[posHand]= deck.draw();
        } catch (isEmptyException e) {
            //TODO gestire eccezione
            //pesco nell'altro mazzo
            //se anche lui vuoto chiamo endgame()
        }
    }

    public void DrawPositionedCard( PlayableCard card, int posHand){
        //l'utente sceglie direttamente la carta presente in tableCenter da pescare
        HandCard[posHand]= player.getGame().tablecenter.drawAndPosition(card);
    }

    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

    public void playCard(Card card, int x, int y ){
        // devo controllare che la carta in input sia veramente nella mano? Non nel caso di starting card
        // TODO controllo se la carta è girata
        //TODO se carta oro devo controllare CurrentResources
        //TODO controllare se la carta si può in quella posizione

        displayedCards[x][y] = card;
        player.getCurrentResources().update(card);

    }
}
