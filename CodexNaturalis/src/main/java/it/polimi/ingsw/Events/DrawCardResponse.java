package it.polimi.ingsw.Events;

/**
 * Event that represent the client response to a draw card request
 */
public class DrawCardResponse extends GenericEvent{
    private static final long serialVersionUID = 7L;
    /**
     * number that represent the chosen source of the draw:
     * 0,1,2,3 represent the respective card in the table center
     * 4 for resource deck
     * 5 for gold deck
     */
    public final int position;

    /**
     * Constructor
     * @param position the chosen position to draw from
     * @param nickname the player that sends the response
     */
    public DrawCardResponse(int position,String nickname) {
        super("Draw source chosen!",nickname);
        this.position = position;
    }
}
