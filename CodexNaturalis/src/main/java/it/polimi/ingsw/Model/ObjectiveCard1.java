package it.polimi.ingsw.Model;

import java.io.FileNotFoundException;

/**
 * ObjectiveCard1 represents the first type of objectives, those that add points  to your score when positional requirements are satisfied.
 */
public class ObjectiveCard1 extends ObjectiveCard{

    /**
     *req is an array that encodes position of cards on the game board, for some values of req , the objective card will bestow bonus points.
     */
    private int[] req; //POSIZIONE carta di colore "1", poi la 2 e poi la 3
    /**
     * color is an array of colors of cards, (Note: some objective card has in some position different color requirements).
     */
    private CardColor[] color; //Colore 1, 2 e 3

    /**
     * getter used to know about which cards positions associated to array colors allows to any given objective card to add points to your score track.
     * @return position of the cards on the game board that are set in color array.
     */
    public int[] getRequiredPositions() {
        return req;
    }

    /**
     * getter used to know about which cards colors associated to array position allows to any given objective card to add points to your score tracks.
     * @return colors of the cards on the game board that are set in the position array.
     */
    public CardColor[] getCardColors() {
        return color;
    }




}