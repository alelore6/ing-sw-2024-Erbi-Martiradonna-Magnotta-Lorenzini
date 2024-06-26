package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.ModelView.TableCenterView;

/**
 * Event that represent the model request for a source for the draw of a card.
 */
public class DrawCardRequest extends GenericRequest{
    /**
     * the game info before the draw
     */
    public final GameView gameView;
    /**
     * the table center info before the draw
     */
    public final TableCenterView tableCenterView;
    /**
     * number of remaining cards in the resource deck
     */
    public final int resCardinDeck;
    /**
     * number of remaining cards in the gold deck
     */
    public final int goldCardinDeck;

    /**
     * Constructor
     * @param nickname the player that receive the event
     * @param gameView the game info at the moment of the request
     * @param resCardsinDeck the number of remaining cards in the resource deck
     * @param goldCardinDeck the number of remaining cards in the gold deck
     */
    public DrawCardRequest(String nickname, GameView gameView, int resCardsinDeck, int goldCardinDeck){
        super("Now it's time to draw a card: choose a source to draw from.",nickname);
        this.tableCenterView = gameView.tableCenterView;
        this.gameView = gameView;
        this.resCardinDeck = resCardsinDeck;
        this.goldCardinDeck = goldCardinDeck;
    }
}
