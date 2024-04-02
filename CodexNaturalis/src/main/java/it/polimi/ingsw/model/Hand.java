package it.polimi.ingsw.model;

public class Hand {
    PlayableCard[] HandCard;
    private final Card[][] displayedCards;
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
            // draw from other deck or ask the player another draw source
            if(deck instanceof GoldDeck){
                try {
                    HandCard[posHand]= player.getGame().tablecenter.getResDeck().draw();
                } catch (isEmptyException e2) {
                    player.getGame().endGame();
                }
            }
            else {
                try {
                    HandCard[posHand]= player.getGame().tablecenter.getGoldDeck().draw();
                } catch (isEmptyException e2) {
                    player.getGame().endGame();
                }
            }
        }
    }

    public void DrawPositionedCard( PlayableCard card, int posHand){
        //l'utente sceglie direttamente la carta presente in tableCenter da pescare
        HandCard[posHand]= player.getGame().tablecenter.drawAndPosition(card);
    }

    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

    public void playCard(Card card, int x, int y ) throws WrongPlayException{
        // devo controllare che la carta in input sia veramente nella mano? Non nel caso di starting card
        // TODO controllo se la carta è girata
        //TODO se carta oro devo controllare CurrentResources
        if (card instanceof GoldCard){

        }
        //check surrounding places
        if( displayedCards[x][y]!=null && displayedCards[x-1][y]!=null && displayedCards[x+1][y]!=null && displayedCards[x][y-1]!=null && displayedCards[x][y+1]!=null){
            throw new WrongPlayException(player,x,y,card);
            //TODO check corner liberi
        }
        displayedCards[x][y] = card;
        player.getCurrentResources().update(card);
    }
}
