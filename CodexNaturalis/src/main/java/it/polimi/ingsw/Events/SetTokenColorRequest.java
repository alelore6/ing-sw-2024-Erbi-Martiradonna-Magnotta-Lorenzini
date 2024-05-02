package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.TokenColor;

import java.util.ArrayList;

public class SetTokenColorRequest extends GenericEvent{
    public final ArrayList<TokenColor> availableColors;

    public SetTokenColorRequest(String nickname, ArrayList<TokenColor> availableColors) {
        super("Choose token color:", nickname);
        this.availableColors = availableColors;
    }
}
