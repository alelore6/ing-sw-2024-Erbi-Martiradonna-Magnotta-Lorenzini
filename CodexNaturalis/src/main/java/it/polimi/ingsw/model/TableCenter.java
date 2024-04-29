package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.isEmptyException;

/**
 *  Class containing the table center of the game, holding the playing card decks aswell the cards layed common for everyone.
 */
public class TableCenter {
    /**
     * Game instance linked to this table center
     */
    private final Game game;
    /**
     * Deck instance containing the resource cards to be drawn by the players
     */
    private final ResourceDeck resDeck;
    /**
     * Deck instance containing the gold cards to be drawn by the players
     */
    private final GoldDeck goldDeck;
    /**
     * Deck instance containing the objective cards to be drawn by the players
     */
    private final ObjectiveDeck objDeck;
    /**
     * array containing the two common objective cards laying on the table
     */
    private final ObjectiveCard[] objCards = new ObjectiveCard[2];
    /**
     * array containing the four playing cards (two resource and two gold) layed on the table,
     * that can be drawn alternatively to the two decks
     */
    private final PlayableCard[] centerCards = new PlayableCard[4]; //first two are resource cards, last two are gold


    /**
     * attribute Scoretrack representing the score track of the current game
     */
    private Scoretrack scoretrack;

    /**
     * Constructor: initializes the table center, linking all the attributes to the class
     * @param resDeck resource cards deck that will be placed on the table center
     * @param goldDeck gold cards deck that will be placed on the table center
     * @param objDeck objective cards deck that will be placed on the table center
     * @param game Game instance linked to the table center
     */
    public TableCenter(ResourceDeck resDeck, GoldDeck goldDeck, ObjectiveDeck objDeck, Game game) {
        this.resDeck = resDeck;
        this.goldDeck = goldDeck;
        this.objDeck = objDeck;
        this.game = game;
        scoretrack = new Scoretrack();
    }

    /**
     * getter for the two objective cards layed on the table
     * @return objective cards array
     */
    public ObjectiveCard[] getObjCards() {
        return objCards;
    }    //GETTER
    /**
     * getter for the four playable cards layed on the table
     * @return playable cards array
     */
    public PlayableCard[] getCenterCards() {
        return centerCards;
    }

    /**
     * getter for the resource cards deck placed on the table center
     * @return resource card deck instance
     */
    public ResourceDeck getResDeck() {
        return resDeck;
    }

    /**
     * getter for the gold cards deck placed on the table center
     * @return gold card deck instance
     */
    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    /**
     * getter for the objective cards deck placed on the table center
     * @return objective card deck instance
     */
    public ObjectiveDeck getObjDeck() {
        return objDeck;
    }

    /**
     * getter for scoretrack
     * @return scoretrack
     */
    public Scoretrack getScoretrack() {
        return scoretrack;
    }

    /**
     * method that can be called by the user ALTERNATIVELY to the standard draw from deck method.
     * Finds the playable card the user requested through the array, returns it if it exists.
     * Later it replaces it (if possible) with a card of the same type (gold or resource) and if not
     * just replaces it with the other type.
     * If both decks are empty, the card slot remains empty and endgame process is triggered
     * @param playablecard
     * @return an integer representing the state of the draw:
     *   0 means the draw was correct and neither of the decks were empty
     *   1 means the draw was correct but one or both the decks were empty
     *  -1 means the card requested by the user was not found (the player will not draw anything, and it
     *   will be prompted to draw again either from the deck or from the cards layed on the table)
     */
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

