package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * ObjectiveCard is the abstract class that encodes the objective cards in the game.
 * It implements Serializable because trough serialization is possible to transmit objects between different machines in a network.
 */
public abstract class ObjectiveCard implements Serializable {
    /**
     * ID is the integer that counts IDs of the cards,(objective ards in this case).
     */
    protected int ID;
    /**
     * Points is the integer used to track each objective card points bonus.
     */
    protected int points;

    /**
     * getPoints is the getter to obtain the number of points an objective card can deliver.
     *
     * @return points of an objective card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * getID is the getter of the objective cards IDs to find out which it is.
     *
     * @return ID of the card.
     */
    public int getID() {
        return this.ID;
    }
}

