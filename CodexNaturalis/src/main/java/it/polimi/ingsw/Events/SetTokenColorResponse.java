package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.TokenColor;

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
    public SetTokenColorResponse(int choice, String nickname) {
        super("Token color set.", nickname);
        switch (choice){
            case 1 -> this.tokenColor = TokenColor.RED;
            case 2 -> this.tokenColor = TokenColor.YELLOW;
            case 3 -> this.tokenColor = TokenColor.GREEN;
            case 4 -> this.tokenColor = TokenColor.BLUE;

            // Can't happen
            default -> this.tokenColor = null;
        }
    }
}
