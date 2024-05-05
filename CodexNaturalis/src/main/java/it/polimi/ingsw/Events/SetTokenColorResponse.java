package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.TokenColor;

/**
 * Event that represent the response to a request to choose the token color
 */
public class SetTokenColorResponse extends GenericEvent {
    /**
     * the chosen color
     */
    public final TokenColor tokenColor;

    /**
     * Constructor
     * @param tokenColor the chosen color
     * @param nickname the player that sends the event
     */
    public SetTokenColorResponse(TokenColor tokenColor, String nickname) {
        super("Token color set.", nickname);
        this.tokenColor = tokenColor;
    }
}
