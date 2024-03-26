package it.polimi.ingsw.model;

import java.util.Optional;

public class TableCenter {

    private ResourceDeck resDeck;
    private GoldDeck goldDeck;

    private ObjectiveDeck objDeck;

    private ObjectiveCard[] objCards = new ObjectiveCard[2];

    private PlayableCard[] centerCards = new PlayableCard[4]; //le prime 2 sono resource card, le ultime gold cards


    public TableCenter(ResourceDeck resDeck, GoldDeck goldDeck, ObjectiveDeck objDeck) {
        this.resDeck = resDeck;
        this.goldDeck = goldDeck;
        this.objDeck = objDeck;
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


    public Optional<PlayableCard> drawAndPosition(PlayableCard playablecard) throws isEmptyException {
        //TODO exception is to be dealt here
        int i;
        boolean found = false;
        for (i = 0; i < centerCards.length; i++) {   //iterate array to find card passed by parameter
            if (centerCards[i] == playablecard) {
                found = true;
                break;
            }
        }

        if (found) {
            if (playablecard instanceof ResourceCard) {  //if card passed by param. is a resource card I draw from ResourceDeck
                if (resDeck.getNCards() > 0) {
                    try {
                        centerCards[i] = resDeck.draw();
                        return Optional.of(playablecard);
                    } catch (isEmptyException e) {
                        try {
                            centerCards[i] = goldDeck.draw();
                            return Optional.of(playablecard);
                        } catch (isEmptyException e1) {
                            //TODO what to do if both decks are empty?
                        }
                    }
                }

            } else if (playablecard instanceof GoldCard) { //if instead card is GoldCard I first draw from goldDeck
                try {
                    centerCards[i] = goldDeck.draw();
                    return Optional.of(playablecard);
                } catch (isEmptyException e) {
                    try {
                        centerCards[i] = resDeck.draw();
                        return Optional.of(playablecard);
                    } catch (isEmptyException e1) {
                        //TODO what to do if both decks are empty?
                    }

                }

            }
            centerCards[i] = null; //if card  passed by parameter is found but I cant draw, just empty the space

        }
        return Optional.ofNullable(playablecard); //return statement in case the card is not found (the player won't draw anything)
    }
}

