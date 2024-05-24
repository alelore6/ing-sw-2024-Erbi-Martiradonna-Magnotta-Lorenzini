package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.TableCenterView;

/**
 * Event that represent the model request for a source for the draw of a card.
 */
public class DrawCardRequest extends GenericRequest{


    public final TableCenterView tableCenterView;
    /**
     * represent if the gold deck is empty,
     * so that it will not be shown as an option for the draw
     */
    public final boolean goldDeckEmpty;
    /**
     * represent if the resource deck is empty,
     * so that it will not be shown as an option for the draw
     */
    public final boolean resDeckEmpty;

    /**
     * Constructor
     * @param nickname the player that receive the event
     * @param tableCards the card positioned in the table center
     * @param goldDeckEmpty represent if the gold deck is empty
     * @param resDeckEmpty represent if the resource deck is empty
     */
    public DrawCardRequest(String nickname, TableCenterView tableCenterView, boolean goldDeckEmpty, boolean resDeckEmpty){
        super("Now it's time to draw a card: choose a source to draw from. \n",nickname);
        this.tableCenterView = tableCenterView;
        this.goldDeckEmpty = goldDeckEmpty;
        this.resDeckEmpty = resDeckEmpty;
    }

    @Override
    public String msgOutput() {
        return super.msgOutput() + "(1) for RESOURCE or (2) for GOLD: ";
    }
}
