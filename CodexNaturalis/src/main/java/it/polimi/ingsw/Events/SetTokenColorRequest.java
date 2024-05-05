package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.TokenColor;

import java.util.ArrayList;

/**
 * Event that represent the model request to choose the color of the token of the player
 */
public class SetTokenColorRequest extends GenericEvent{
    /**
     * the list of available colors
     */
    public final ArrayList<TokenColor> availableColors;

    /**
     * Constructor
     * @param nickname the player that receives the action
     * @param availableColors the list of available colors
     */
    public SetTokenColorRequest(String nickname, ArrayList<TokenColor> availableColors) {
        super("Choose token color:\n" + availableColors.toString(), nickname);
        this.availableColors = availableColors;
    }
}
