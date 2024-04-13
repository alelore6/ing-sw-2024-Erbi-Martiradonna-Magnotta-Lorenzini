package it.polimi.ingsw.model;

import java.util.HashMap;

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

    public void DrawPositionedCard( PlayableCard card) throws isEmptyException {
        //mappa inutile, bastava ritornare un intero
        HashMap<PlayableCard, Boolean> hashMap = player.getGame().tablecenter.drawAndPosition(card);
        for(int i=0; i< 3; i++){
            if (HandCard[i]==null)
                HandCard[i] = card;
        }
        // if one deck is empty throw exception
        if (hashMap.get(card))
            throw new isEmptyException();
    }

    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

    public void playCard(Card card, int x, int y ) throws WrongPlayException{
        //check that the input card is in the hand, not in case of starting card
        boolean found=false;
        int i=-1;
        if (!(card instanceof StartingCard)){
            for (Card c: HandCard){
                //save the position of the card in the hand
                i++;
                if (c.equals(card)) {
                    found = true;
                    break;
                }
            }
            if (!found){
                throw new WrongPlayException(player,-1,-1,card);
                //negative position in the exception indicate specific type of errors
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
        // corner that the card overlaps, must be at least 1
        Corner[] overlaps= new Corner[4];
        // if there is a card, check overlapping corners are visible
        if (displayedCards[x-1][y-1]!=null){
            // 0: UP_SX; 1: UP_DX; 2: DOWN_SX; 3: DOWN_DX
            //if there's no corner, it's NULL
            //if corner is visible but without resource, the attribute resource will be null
            if ((displayedCards[x-1][y-1]).corners[1]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            //save the corner that is overlapped
            else overlaps[0]= (displayedCards[x-1][y-1]).corners[1];
        }
        if (displayedCards[x-1][y+1]!=null){
            if ((displayedCards[x-1][y+1]).corners[3]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps[1]= (displayedCards[x-1][y+1]).corners[3];
        }
        if (displayedCards[x+1][y+1]!=null){
            if ((displayedCards[x+1][y+1]).corners[2]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps[2]=(displayedCards[x+1][y+1]).corners[2];
        }
        if (displayedCards[x+1][y-1]!=null){
            if ((displayedCards[x+1][y-1]).corners[0]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else overlaps[3]=(displayedCards[x+1][y-1]).corners[0];
        }
        //if it's not a starting card: overlaps must be at least 1 and all overlaps corners are set covered
        if (!(card instanceof StartingCard)){
            int count=0;
            for( int j=0; j<4; j++){
                if(overlaps[j]!=null){
                    count++;
                    overlaps[j].isCovered=true;
                }
            }
            if (count ==0)
                throw new WrongPlayException(player,-2,-2,card);
        }
        displayedCards[x][y] = card;
        player.getCurrentResources().update(card,x ,y);
        //card is removed from the hand
        if (i!=-1){
            HandCard[i]=null;
        }
    }
}
