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
        // controllo che la carta in input sia veramente nella mano? Non nel caso di starting card
        boolean found=false;
        if (!(card instanceof StartingCard)){
            for (Card c: HandCard){
                if (c.equals(card)) {
                    found = true;
                    break;
                }
            }
            if (!found){
                throw new WrongPlayException(player,-1,-1,card);
                //pos -1,-1 indicates this specific type of error
            }
        }
        if (card instanceof GoldCard && !card.isFacedown){
            //TODO devo controllare CurrentResources
        }
        //check surrounding places
        if( displayedCards[x][y]!=null && displayedCards[x-1][y]!=null && displayedCards[x+1][y]!=null && displayedCards[x][y-1]!=null && displayedCards[x][y+1]!=null){
            throw new WrongPlayException(player,x,y,card);
        }
        // numero di angoli che la cartà andrà a sovrapporre
        // deve essercene almeno 1
        int overlaps=0;
        // check overlapping corners are free
        if (displayedCards[x-1][y-1]!=null){
            // 0: UP_SX; 1: UP_DX; 2: DOWN_SX; 3: DOWN_DX
            //if there's no corner, it's NULL
            //come è definito un angolo libero ma vuoto?
            if ((displayedCards[x-1][y-1]).corners[1]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps++;
        }
        if (displayedCards[x-1][y+1]!=null){
            if ((displayedCards[x-1][y+1]).corners[3]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps++;
        }
        if (displayedCards[x+1][y+1]!=null){
            if ((displayedCards[x+1][y+1]).corners[2]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps++;
        }
        if (displayedCards[x+1][y-1]!=null){
            if ((displayedCards[x+1][y-1]).corners[0]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps++;
        }
        if(overlaps==0){
            throw new WrongPlayException(player,-2,-2,card);
        }
        displayedCards[x][y] = card;
        player.getCurrentResources().update(card);
    }
}
