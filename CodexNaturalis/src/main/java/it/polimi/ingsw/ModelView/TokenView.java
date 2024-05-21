package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Token;
import it.polimi.ingsw.Model.TokenColor;

import java.io.Serializable;

public class TokenView implements Serializable {
    public final TokenColor color;
    public final int position;

    public TokenView(Token token) {
        if (token != null) {
            color = token.getColor();
            position= token.getScoreTrackPos();
        }
        else {
            color = null;
            position = -1;
        }
    }
}
