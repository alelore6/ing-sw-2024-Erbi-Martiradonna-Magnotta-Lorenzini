package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.TokenColor;

public class SetTokenColorResponse extends GenericEvent {
    public final TokenColor tokenColor;

    public SetTokenColorResponse(TokenColor tokenColor, String nickname) {
        super("Token color set.", nickname);
        this.tokenColor = tokenColor;
    }
}
