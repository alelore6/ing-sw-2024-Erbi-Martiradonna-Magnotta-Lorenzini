package it.polimi.ingsw.model;

import java.util.Optional;

public class TableCenter {

    private ResourceDeck resDeck;  //TODO sostituire "Object" con classi corrette
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

                int i;
                boolean found = false;
                for(i = 0; i < centerCards.length; i++) {   //itero array per trovare carta passata come parametro
                    if (centerCards[i] == playablecard) {
                        found = true;
                        break;
                    }
                }

            if(found) {
                if (playablecard instanceof ResourceCard) {  //se la carta è una resource card prima pesco dal resDeck
                    if (resDeck.getNCards() > 0) {
                        centerCards[i] = resDeck.draw();
                        return Optional.of(playablecard);
                    } else if (goldDeck.getNCards() > 0) {
                        centerCards[i] = goldDeck.draw();
                        return Optional.of(playablecard);
                    }

                }
                else if (playablecard instanceof GoldCard) { //altrimenti prima pesco dal GoldDeck
                    if (goldDeck.getNCards() > 0) {
                        centerCards[i] = resDeck.draw();
                        return Optional.of(playablecard);
                    } else if (resDeck.getNCards() > 0) {
                        centerCards[i] = goldDeck.draw();
                        return Optional.of(playablecard);
                    }

                }
                centerCards[i] = null;
            }
            return Optional.ofNullable(playablecard);
    }
}

