package it.polimi.ingsw.model;

/**
 *  Class containing the score track on which the tokens will be moving
 */
public class Scoretrack {
    /**
     * array containing all the token positions for the current game
     */
    private int[] tokenPos;

    /**
     * Constructor: initializes array representing all the 30 positions of the score track (from 0 to 29)
     */
    public Scoretrack() {
        tokenPos = new int[30];
    }

    /**
     * getter for the token positions array
     * @return token positions array
     */
    public int[] getTokenPos() {
        return tokenPos;
    }


}
