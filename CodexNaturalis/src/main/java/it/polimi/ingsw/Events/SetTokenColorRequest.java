package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.TokenColor;

public class SetTokenColorRequest extends GenericEvent{
    public final TokenColor[] availableColors;

    public SetTokenColorRequest(String nickname, TokenColor[] availableColors) {
        super("Choose token color:", nickname);
        this.availableColors = availableColors;
    }
}
