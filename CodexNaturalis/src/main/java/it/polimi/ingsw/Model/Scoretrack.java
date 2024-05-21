package it.polimi.ingsw.Model;

import java.util.HashMap;

/**
 *  Class containing the score track on which the tokens will be moving
 */
public class Scoretrack {
    /**
     * array containing all the token positions for the current game
     */
    private int[] tokenPos;
    /**
     * map that connect every token to his position on the table
     */
    protected HashMap<Token,Integer> points;

    /**
     * Constructor: initializes array representing all the 30 positions of the score track (from 0 to 29)
     */
    protected Scoretrack() {
        tokenPos = new int[30];
        points = new HashMap<Token, Integer>();
    }

    /**
     * getter for the token positions array
     * @return token positions array
     */
    protected int[] getTokenPos() {
        return tokenPos;
    }

    /**
     * add a token to the scoretrack
     * @param token the player's token
     */
    protected void addToken(Token token) {
        points.put(token,0);
    }

    /**
     * move the token on the scoretrack
     * @param token the player's token
     * @param point updated points
     */
    protected void move(Token token,int point) {
        points.replace(token,point);
    }

    /**
     * Create a map between players' nicknames and their points
     * @return a map between players' nicknames and their points
     */
    public HashMap<String,Integer> getRankings() {
        HashMap<String,Integer> p = new HashMap<String,Integer>();
        for (Token token : points.keySet()) {
            p.put(token.getPlayer().getNickname(),points.get(token));
        }
        return p;
    }

}
