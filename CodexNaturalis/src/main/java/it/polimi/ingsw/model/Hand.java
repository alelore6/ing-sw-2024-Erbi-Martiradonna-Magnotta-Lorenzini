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
        HashMap<PlayableCard, Boolean> hashMap = player.game.tablecenter.drawAndPosition(card);
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
            for (Resource r : Resource.values()){
                if (((GoldCard) card).req.get(r) > player.getCurrentResources().currentResources.get(r)){
                    throw new WrongPlayException(player, -3,-3, card);
                }
            }
        }
        //check position and its surroundings are free
        //TODO sistemare per bordi matrice
        if( displayedCards[x][y]!=null && displayedCards[x-1][y]!=null && displayedCards[x+1][y]!=null && displayedCards[x][y-1]!=null && displayedCards[x][y+1]!=null){
            throw new WrongPlayException(player,x,y,card);
        }
        // corner that the card overlaps, must be at least 1
        Corner[] overlaps= new Corner[4];
        // if there is a card, check overlapping corners are visible
        if (displayedCards[x-1][y-1]!=null){
            // 0,4: UP_SX; 1,5: UP_DX; 2,6: DOWN_SX; 3,7: DOWN_DX (4,5,6,7 are backs corner)
            //if there's no corner, it's NULL
            if (!card.isFacedown && (displayedCards[x-1][y-1]).corners[1]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else {
                //save the corner that is overlapped
                if(!card.isFacedown)
                    overlaps[0]= (displayedCards[x-1][y-1]).corners[1];
                else overlaps[0]= (displayedCards[x-1][y-1]).corners[5];
            }
        }
        if (displayedCards[x-1][y+1]!=null) {
            if (!card.isFacedown && (displayedCards[x-1][y+1]).corners[3] == null) {
                throw new WrongPlayException(player, x, y, card);
            } else {
                if(!card.isFacedown)
                    overlaps[1] = (displayedCards[x-1][y+1]).corners[3];
                else overlaps[1] = (displayedCards[x-1][y+1]).corners[7];
            }
        }
        if (displayedCards[x+1][y+1]!=null){
            if (!card.isFacedown && (displayedCards[x+1][y+1]).corners[2]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else {
                if(!card.isFacedown)
                    overlaps[2]=(displayedCards[x+1][y+1]).corners[2];
                else overlaps[2]=(displayedCards[x+1][y+1]).corners[6];
            }
        }
        if (displayedCards[x+1][y-1]!=null){
            if (!card.isFacedown && (displayedCards[x+1][y-1]).corners[0]==null){
                throw new WrongPlayException(player,x,y,card);
            }
            else {
                if(!card.isFacedown)
                    overlaps[3]=(displayedCards[x+1][y-1]).corners[0];
                else overlaps[3]=(displayedCards[x+1][y-1]).corners[4];
            }
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
        player.getCurrentResources().update(card, overlaps);
        //card is removed from the hand
        if (found){
            HandCard[i]=null;
        }
    }
}
