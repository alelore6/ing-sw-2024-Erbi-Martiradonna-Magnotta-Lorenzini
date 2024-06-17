package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.TableCenterView;

/**
 * Event that represent the model request for a source for the draw of a card.
 */
public class DrawCardRequest extends GenericRequest{


    public final TableCenterView tableCenterView;

    public final int resCardinDeck;
    public final int goldCardinDeck;

    /**
     * Constructor
     * @param nickname the player that receive the event
     * @param tableCards the card positioned in the table center
     * @param goldDeckEmpty represent if the gold deck is empty
     * @param resDeckEmpty represent if the resource deck is empty
     */
    public DrawCardRequest(String nickname, TableCenterView tableCenterView, int resCardsinDeck, int goldCardinDeck){
        super("Now it's time to draw a card: choose a source to draw from.",nickname);
        this.tableCenterView = tableCenterView;
        this.resCardinDeck = resCardsinDeck;
        this.goldCardinDeck = goldCardinDeck;
    }
}
