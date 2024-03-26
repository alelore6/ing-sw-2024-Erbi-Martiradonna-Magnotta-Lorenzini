package it.polimi.ingsw.model;

public class Hand {
    PlayableCard[] HandCard;
    private final Player player;
    Hand(Player player){
        HandCard= new PlayableCard[3];
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

    public void playCard(PlayableCard card ){
        // mi servono sapere gli angoli che voglio andare a coprire
        // come li recupero? Oppure li prendo in input?

        //TODO controllare se la carta si può giocare su quegli angoli

        //TODO se carta oro devo controllare CurrentResources

        //TODO gioca carta

        player.getCurrentResources().update(card);

        //da input scegliere se pescare da mazzo o da terra
    }
}
