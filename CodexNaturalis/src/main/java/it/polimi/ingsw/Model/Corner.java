package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Corner class represents a generic corner of a card with all of his properties.
 * Implements Serializable because through serialization is possible to transmit object between different machines in a network.
 */

public class Corner implements Serializable {
    /**
     * position of the corner in the card.
     */
    protected Position pos;
    /**
     *  resource represents all type of resource that can be in a corner.
     */
    protected Resource resource;
    /**
     * isCovered is a boolean to know if a corner is covered or not.
     */
    public boolean isCovered;

    /**
     * Constructor builds corners to allow to add them to the cards.
     * @param pos position of a corner in the card.
     * @param resource resource that could occupy a corner.
     */
    public Corner(Position pos, Resource resource){
        isCovered = false;
        this.pos = pos;
        this.resource = resource;
    }

    /**
     * getPosition is the getter that returns corner positions in a card.
     * @return corner position in a card.
     */
    public String getPosition(){
        return pos == null ? null : pos.toString();
    }

    /**
     * getResource is the getter that returns resource that is on a corner.
     * @return resource that occupies a corner.
     */
    public Resource getResource(){
        return resource;
    }

    /**
     *
     * @return
     */
    public String getStringResource(){
        return resource == null ? "none" : resource.toString();
    }
}
