package it.polimi.ingsw.Model;

import java.io.FileNotFoundException;

/**
 * ObjectiveCard1 represents the first type of objectives, those that add points  to your score when the number of resource units required is present in your game board.
 */
public class ObjectiveCard1 extends ObjectiveCard{

    /**
     *
     */
    private int[] req; //POSIZIONE carta di colore "1", poi la 2 e poi la 3
    /**
     *
     */
    private CardColor[] color; //Colore 1, 2 e 3

    /**
     *
     * @return
     */
    public int[] getRequiredPositions() {
        return req;
    }

    /**
     *
     * @return
     */
    public CardColor[] getCardColors() {
        return color;
    }




}