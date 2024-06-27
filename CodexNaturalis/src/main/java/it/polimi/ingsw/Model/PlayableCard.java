package it.polimi.ingsw.Model;

/**
 * Playable Card is an abstract class that represents only playable cards so only gold and resource cards.
 * It implements Card because it exploits all methods of that class.
 */
public abstract class PlayableCard extends Card{
    /**
     * points is an integer that represents all points values of gold cards (Ed.Note resource cards don't have points bonus).
     */
    protected int points;
    /**
     * isChecked is an attribute that which is used to see if they have been counted in the objective bonus assessment of positions.
     */
    protected int isChecked = 0;

    /**
     * getPoints is a getter to obtain points of gold cards (Ed.Note resource cards don't have points bonus).
     * @return number of points of the card.
     */
    public int getPoints(){
        return points;
    }
}
