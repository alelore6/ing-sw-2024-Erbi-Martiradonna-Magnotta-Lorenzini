package it.polimi.ingsw.model;

/**
 * Class that contains player's hand and played cards
 */
public class Hand {
    /**
     * the player that owns the class
     */
    private final Player player;
    /**
     * player's hand card
     */
    private final PlayableCard[] HandCard;
    /**
     * player's played cards
     */
    private final Card[][] displayedCards;

    /**
     * Constructor of the class
     * @param player player that owns the class
     */
    Hand(Player player){
        HandCard= new PlayableCard[3];
        displayedCards=new Card[81][81];
        this.player=player;
    }

    /**
     * Draw a card from a deck and add it to the player's hand,
     * if deck is empty endGame() is called
     * @param deck the chosen deck to draw from
     * @throws HandFullException if hand is already full, should not happen
     * @throws isEmptyException if the chosen deck is empty, the controller will ask the player a new source for the draw
     */
    public void DrawFromDeck(Deck deck) throws HandFullException, isEmptyException {
        for(int i=0; i< 3; i++){
            if (HandCard[i]==null){
                try{ HandCard[i] = deck.draw();}
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
     * @throws isEmptyException if the deck that should substitute the chosen card is empty
     */
    public void DrawPositionedCard( PlayableCard card) throws HandFullException, isEmptyException {
        int result = player.game.tablecenter.drawAndPosition(card);
        //the card chosen cant be found
        if (result<0)
            return;
        boolean done=false;
        for(int i=0; i< 3; i++){
            if (HandCard[i]==null){
                HandCard[i] = card;
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
     * Getter for card in hand
     * @param pos the position that describes the card in the hand we want to get
     * @return the card in tha hand at the pos position
     */
    public PlayableCard getHandCard(int pos){
        return HandCard[pos];
    }

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
     * @throws WrongPlayException if play is not valid, the exception will be handled by the controller asking the client a new play
     */
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
        //card down SX
        if (x!=0 && y!=0 && displayedCards[x-1][y-1]!=null){
            // 0,4: UP_SX; 1,5: UP_DX; 2,6: DOWN_SX; 3,7: DOWN_DX (4,5,6,7 are backs corner)
            //if there's no corner, it's NULL
            if (!displayedCards[x-1][y-1].isFacedown){
                if ((displayedCards[x-1][y-1]).corners[1]==null)
                    throw new WrongPlayException(player,x,y,card);
                //save the corner that is overlapped
                else overlaps[0]= (displayedCards[x-1][y-1]).corners[1];
            }
            else {
                //check back corner
                if ((displayedCards[x-1][y-1]).corners[5]==null)
                    throw new WrongPlayException(player,x,y,card);
                //save the corner that is overlapped
                else overlaps[0]= (displayedCards[x-1][y-1]).corners[5];
            }
        }
        //card up SX
        if (x!=0 && y!=80 && displayedCards[x-1][y+1]!=null) {
            if (!displayedCards[x-1][y+1].isFacedown){
                if ((displayedCards[x-1][y+1]).corners[3] == null)
                    throw new WrongPlayException(player, x, y, card);
                else overlaps[1] = (displayedCards[x-1][y+1]).corners[3];
            }
            else {
                if ((displayedCards[x-1][y+1]).corners[7] == null)
                    throw new WrongPlayException(player, x, y, card);
                else overlaps[1] = (displayedCards[x-1][y+1]).corners[7];
            }
        }
        //card up DX
        if (x!=80 && y!=80 && displayedCards[x+1][y+1]!=null){
            if (!displayedCards[x+1][y+1].isFacedown){
                if((displayedCards[x+1][y+1]).corners[2]==null)
                    throw new WrongPlayException(player,x,y,card);
                else overlaps[2]=(displayedCards[x+1][y+1]).corners[2];
            }
            else {
                if((displayedCards[x+1][y+1]).corners[6]==null)
                    throw new WrongPlayException(player,x,y,card);
                else overlaps[2]=(displayedCards[x+1][y+1]).corners[6];
            }
        }
        //card down SX
        if (x!=80 && y!=0 && displayedCards[x+1][y-1]!=null){
            if (!displayedCards[x+1][y-1].isFacedown){
                if((displayedCards[x+1][y-1]).corners[0]==null)
                    throw new WrongPlayException(player,x,y,card);
                overlaps[3]=(displayedCards[x+1][y-1]).corners[0];
                }
            else {
                if((displayedCards[x+1][y-1]).corners[4]==null)
                    throw new WrongPlayException(player,x,y,card);
                overlaps[3]=(displayedCards[x+1][y-1]).corners[4];
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
                throw new WrongPlayException(player,-2,-2,card);
            }
        }
        //play the card
        displayedCards[x][y] = card;
        //card is removed from the hand, not in case of starting card
        if (found) {
            HandCard[i] = null;
        }
        // update current resources
        player.getCurrentResources().update(card, overlaps);
    }
}
