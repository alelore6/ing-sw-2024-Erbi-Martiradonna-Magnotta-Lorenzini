package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.WrongPlayException;
import it.polimi.ingsw.Exceptions.isEmptyException;

/**
 * Class that contains player's hand and his played cards.
 * Also contains the logic behind the play and the draw actions.
 */
public class Hand {
    /**
     * the player that owns the class
     */
    private final Player player;
    /**
     * player's hand card
     */
    private final PlayableCard[] HandCards;
    /**
     * player's played cards
     */
    private final Card[][] displayedCards;

    /**
     * getter for displayed cards
     * @return displayedCards
     */
    public Card[][] getDisplayedCards() {
        return displayedCards;
    }

    /**
     * Number of cards played
     */
    private int NcardsPlayed=0;

    /**
     * Constructor of the class
     * @param player player that owns the class
     */
    public Hand(Player player){
        HandCards = new PlayableCard[3];
        displayedCards=new Card[81][81];
        this.player=player;
    }

    /**
     * Draw a card from a deck and add it to the player's hand,
     * if deck is empty endGame() is called
     * @param deck the chosen deck to draw from
     * @throws HandFullException if hand is already full, should not happen
     * @throws isEmptyException if the chosen deck is empty, the Controller will ask the player a new source for the draw
     */
    public void DrawFromDeck(Deck deck) throws HandFullException, isEmptyException {
        for(int i=0; i< 3; i++){
            if (HandCards[i]==null){
                try{ HandCards[i] = (PlayableCard) deck.draw();}
                catch (isEmptyException e){
                    player.game.endGame(deck instanceof GoldDeck? 5 : 6);
                    throw new isEmptyException(deck);
                }
                return;
            }
        }
        throw new HandFullException(player);
    }

    /**
     * Draw one of the positioned card in table center and add it to the player's hand
     * @param card the chosen card
     * @throws HandFullException if hand is already full, should not happen
     * @throws isEmptyException if the deck that should substitute the chosen card is empty or the card was not found in the table center
     */
    public void DrawPositionedCard( PlayableCard card) throws HandFullException, isEmptyException {
        int result = player.game.tablecenter.drawAndPosition(card);
        //the card chosen cant be found
        if (result<0){
            throw new isEmptyException(null);
        }

        boolean done=false;
        for(int i=0; i< 3; i++){
            if (HandCards[i]==null){
                HandCards[i] = card;
                done=true;
                break;
            }
        }
        if(!done)
            throw new HandFullException(player);
        // if one deck is empty throw exception
        if (result==1) {
            player.game.endGame(card instanceof GoldCard ? 5 : 6);
            throw new isEmptyException( card instanceof GoldCard? player.game.tablecenter.getGoldDeck() : player.game.tablecenter.getResDeck());
        }
    }

    /**
     * Getter for the hand cards of the player
     * @return the hand cards of the player
     */
    public PlayableCard[] getHandCards(){return HandCards;}

    /**
     * check that play is valid following the game rules:
     * 1. if not a starting card check that the card is found in the hand cards
     * 2. if card is a gold card, check required resources
     * 3. check x,y position and its surroundings are free
     * 4. check that overlapping corners are visible
     * 5. setting overlapping corners to covered
     * 6. the card is set on displayedCards and removed from the hand and the player's resources updated by calling currentResources.update()
     * @param card the card in the hand that will be played
     * @param x the x-axis coordinates that describes the position where the card will be played
     * @param y the y-axis coordinates that describes the position where the card will be played
     * @throws WrongPlayException if play is not valid, the exception will be handled by the Controller asking the client a new play
     */
    public void playCard(Card card, int x, int y ) throws WrongPlayException{
        //check that the input card is in the hand, not in case of starting card
        boolean found=false;
        int i=-1;
        if (!(card instanceof StartingCard)){
            for (Card c: HandCards){
                //save the position of the card in the hand
                i++;
                if (c!=null && c.ID==card.ID) {
                    found = true;
                    break;
                }
            }
            if (!found){
                throw new WrongPlayException(player,-1,-1,card);
                //negative position in the exception indicate specific type of errors
            }
        }
        else{
            displayedCards[x][y]=card;
            card.playOrder=NcardsPlayed;
            NcardsPlayed++;
            player.getCurrentResources().update(card, null);
            return;
        }
        // check required resource to play that card
        if (card instanceof GoldCard && !card.isFacedown){
            for (Resource r : Resource.values()){
                if (((GoldCard) card).req.get(r) > player.getCurrentResources().currentResources.get(r)){
                    throw new WrongPlayException(player, -3,-3, card);
                    //error not enough resources
                }
            }
        }
        //check position and its surroundings are free
        if (displayedCards[x][y]!=null)
            throw new WrongPlayException(player,x,y,card);
        if (x!=0 && displayedCards[x-1][y]!=null)
            throw new WrongPlayException(player,x,y,card);
        if (x!=80 && displayedCards[x+1][y]!=null)
            throw new WrongPlayException(player,x,y,card);
        if (y!=0 && displayedCards[x][y-1]!=null)
            throw new WrongPlayException(player,x,y,card);
        if (y!=80 && displayedCards[x][y+1]!=null)
            throw new WrongPlayException(player,x,y,card);
        // corner that the card overlaps, must be at least 1
        Corner[] overlaps= new Corner[4];
        // if there is a card, check overlapping corners are visible
        //CARD UP SX
        if (x!=0 && y!=0 && displayedCards[x-1][y-1]!=null){
            // 0: UP_SX; 1: UP_DX; 2: DOWN_SX; 3: DOWN_DX
            //if there's no corner, it's NULL
            if (!displayedCards[x-1][y-1].isFacedown){
                if ((displayedCards[x-1][y-1]).frontCorners[3].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                //save the corner that is overlapped
                else overlaps[0]= (displayedCards[x-1][y-1]).frontCorners[3];
            }
            else {
                //check back corner
                if ((displayedCards[x-1][y-1]).backCorners[3].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                //save the corner that is overlapped
                else overlaps[0]= (displayedCards[x-1][y-1]).backCorners[3];
            }
        }
        //card UP RIGHT
        if (x!=0 && y!=80 && displayedCards[x-1][y+1]!=null) {
            if (!displayedCards[x-1][y+1].isFacedown){
                if ((displayedCards[x-1][y+1]).frontCorners[2].pos == null)
                    throw new WrongPlayException(player, x, y, card);
                else overlaps[1] = (displayedCards[x-1][y+1]).frontCorners[2];
            }
            else {
                if ((displayedCards[x-1][y+1]).backCorners[2].pos == null)
                    throw new WrongPlayException(player, x, y, card);
                else overlaps[1] = (displayedCards[x-1][y+1]).backCorners[2];
            }
        }
        //card DOWN RIGHT
        if (x!=80 && y!=80 && displayedCards[x+1][y+1]!=null){
            if (!displayedCards[x+1][y+1].isFacedown){
                if((displayedCards[x+1][y+1]).frontCorners[0].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                else overlaps[2]=(displayedCards[x+1][y+1]).frontCorners[0];
            }
            else {
                if((displayedCards[x+1][y+1]).backCorners[0].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                else overlaps[2]=(displayedCards[x+1][y+1]).backCorners[0];
            }
        }
        //card down SX
        if (x!=80 && y!=0 && displayedCards[x+1][y-1]!=null){
            if (!displayedCards[x+1][y-1].isFacedown){
                if((displayedCards[x+1][y-1]).frontCorners[1].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                overlaps[3]=(displayedCards[x+1][y-1]).frontCorners[1];
                }
            else {
                if((displayedCards[x+1][y-1]).backCorners[1].pos==null)
                    throw new WrongPlayException(player,x,y,card);
                overlaps[3]=(displayedCards[x+1][y-1]).backCorners[1];
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
            if (count ==0){
                //corner are set free again
                for( int k=0; k<4; k++) {
                    if (overlaps[k] != null) {
                        overlaps[k].isCovered = false;
                    }
                }
                //indicates the error play without overlaps
                throw new WrongPlayException(player,-2,-2,card);
            }
        }
        //play the card
        displayedCards[x][y] = card;
        card.playOrder=NcardsPlayed;
        NcardsPlayed++;
        //card is removed from the hand, not in case of starting card
        if (found) {
            HandCards[i] = null;
        }
        // update current resources
        player.getCurrentResources().update(card, overlaps);

    }



}
