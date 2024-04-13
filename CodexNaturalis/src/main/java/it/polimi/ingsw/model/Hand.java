package it.polimi.ingsw.model;

import java.util.Optional;

public class Hand {
    PlayableCard[] HandCard;
    private final Card[][] displayedCards;
    private final Player player;
    Hand(Player player){
        HandCard= new PlayableCard[3];
        displayedCards=new Card[81][81];
        this.player=player;
    }

    public void DrawFromDeck(Deck deck, int posHand) throws isEmptyException {
            HandCard[posHand]= deck.draw();
    }

    public void DrawPositionedCard( PlayableCard card, int posHand) throws isEmptyException {
        //ha senso tenere l'optional ?
        Optional<PlayableCard> c=player.getGame().tablecenter.drawAndPosition(card);
        c.ifPresent(playableCard -> HandCard[posHand] = playableCard);
    }

    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

    public void playCard(Card card, int x, int y ) throws WrongPlayException{
        // check that the input card is in the hand, not in case of starting card
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
        // check required resource to play that card
        if (card instanceof GoldCard && !card.isFacedown){
            //TODO devo controllare CurrentResources
        }
        //check position and its surroundings are free
        if( displayedCards[x][y]!=null && displayedCards[x-1][y]!=null && displayedCards[x+1][y]!=null && displayedCards[x][y-1]!=null && displayedCards[x][y+1]!=null){
            throw new WrongPlayException(player,x,y,card);
        }
        // number of corner that the card overlaps, must be at least 1
        int overlaps=0;
        // check overlapping corners are visible
        if (displayedCards[x-1][y-1]!=null){
            // 0: UP_SX; 1: UP_DX; 2: DOWN_SX; 3: DOWN_DX
            //if there's no corner, it's NULL
            //if corner is visible but without resource, the attribute resource will be null
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
        player.getCurrentResources().update(card,x ,y);
    }
}
