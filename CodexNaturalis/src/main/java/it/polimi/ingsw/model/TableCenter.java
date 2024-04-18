package it.polimi.ingsw.model;

public class TableCenter {

    private final Game game;
    private final ResourceDeck resDeck;
    private final GoldDeck goldDeck;

    private final ObjectiveDeck objDeck;

    private final ObjectiveCard[] objCards = new ObjectiveCard[2];

    private final PlayableCard[] centerCards = new PlayableCard[4]; //le prime 2 sono resource card, le ultime gold cards


    public TableCenter(ResourceDeck resDeck, GoldDeck goldDeck, ObjectiveDeck objDeck, Game game) {
        this.resDeck = resDeck;
        this.goldDeck = goldDeck;
        this.objDeck = objDeck;
        this.game = game;
    }


    public ObjectiveCard[] getObjCards() {
        return objCards;
    }    //GETTER

    public PlayableCard[] getCenterCards() {
        return centerCards;
    }

    public ResourceDeck getResDeck() {
        return resDeck;
    }

    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    public ObjectiveDeck getObjDeck() {
        return objDeck;
    }


    public int drawAndPosition(PlayableCard playablecard){
        int result=0;
        int i;
        boolean found = false;
        for (i = 0; i < centerCards.length; i++) {   //iterate array to find card passed by parameter
            if (centerCards[i] == playablecard) {
                found = true;
                break;
            }
        }

        if (found) {
            centerCards[i] = null; //if card  passed by parameter is found but I cant draw, just empty the space

            if (playablecard instanceof ResourceCard) {  //if card passed by param. is a resource card I draw from ResourceDeck
                if (resDeck.getNCards() > 0) {
                    try {
                        centerCards[i] = resDeck.draw();

                    } catch(isEmptyException e){
                        result=1;
                        try {
                            centerCards[i] = goldDeck.draw();

                        } catch (isEmptyException e1) {
                            game.endGame(4);
                        }
                    }
                }

            } else if (playablecard instanceof GoldCard) { //if instead card is GoldCard I first draw from goldDeck
                try {
                    centerCards[i] = goldDeck.draw();

                } catch (isEmptyException e) {
                    result=1;
                    try {
                        centerCards[i] = resDeck.draw();

                    } catch (isEmptyException e1) {
                        game.endGame(4);
                    }
                }
            }
            return result;
        }
        return -1; //return statement in case the card is not found (the player won't draw anything)
    }
}

