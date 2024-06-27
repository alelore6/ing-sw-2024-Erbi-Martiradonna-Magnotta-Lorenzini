package it.polimi.ingsw.Model;

import java.util.HashMap;

/**
 * GoldCards represents the gold cards, it extends playable card because playable cards are composed by gold and resource cards.
 */
public class GoldCard extends PlayableCard{
    /**
     * Rpoints are the resource needed to a gold card to bestow points for the score of a player.
     */
    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    private Resource RPoints;
    /**
     * RpointsCorner is a boolean to know if corner requirements are satisfied, to allow the gold card to bestow points associated with those requirements.
     */
    private boolean RPointsCorner;

    /**
     * addreq is a method that adds resource requirement to a gold card.
     * @param resource the type of resource to add.
     * @param value the value of points associated with that resource.
     */
    public void addReq(Resource resource, int value) {
        req.put(resource, value);
    }

    /**
     * the map between the resources and the relative required number to play a card
     */
    HashMap<Resource,Integer> req = new HashMap<>();

    /**
     * getRpoints is a getter method used to know about which resource need any given gold card to bestow its bonus points.
     * @return resource needed by a gold card to bestow its bonus points.
     */
    public Resource getRPoints() {
        return RPoints;
    }

    /**
     * getReq is a getter to know about the map of resource requirements for any given gold card.
     * @return map of resource requirements for a gold card.
     */
    public HashMap<Resource, Integer> getReq() {
        return req;
    }

    /**
     * isRPointsCorner is a method used to know if corner requirements of any given gold card are satisfied.
     * @return true if requirements are satisfied, false otherwise.
     */
    public boolean isRPointsCorner() {
        return RPointsCorner;
    }
}
