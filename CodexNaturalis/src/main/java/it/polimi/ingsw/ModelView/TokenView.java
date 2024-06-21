package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Token;
import it.polimi.ingsw.Model.TokenColor;

import java.io.Serializable;

/**
 * Represent a player's token info in a certain moment
 */
public class TokenView implements Serializable {
    /**
     * The nickname associated with the token.
     */
    public final String nickname;

    /**
     * the color of the token
     */
    public final TokenColor color;
    /**
     * the position of the token on the scoretrack
     */
    public final int position;

    /**
     * Constructor that works like a clone method
     * @param token the player's token
     */
    public TokenView(Token token) {
        if (token != null) {
            color = token.getColor();
            position= token.getScoreTrackPos();
            this.nickname = token.getPlayer().getNickname();
        }
        else {
            color = null;
            position = -1;
            nickname = null;
        }
    }
}
