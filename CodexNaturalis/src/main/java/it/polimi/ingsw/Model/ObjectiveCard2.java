package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectiveCard2 represents the second type of objective, those that add points to your score if the number of resources required is present on your game board.
 */
public class ObjectiveCard2 extends ObjectiveCard {
    /**
     * reqMap is a map that holds the required resources and their corresponding points values.
     */
    private Map<Resource, Integer> reqMap = new HashMap<Resource, Integer>();

    /**
     * getReqMap is the getter to know about the value of points bonus of an objective card associated with its resource requirements.
     * @return the value of points bonus of an objective card associated with its resource requirements.
     */
    public Map<Resource, Integer> getReqMap() {
        return reqMap;
    }

    /**
     *  addReqMap adds to a resource  its corresponding value of points bonus.
     * @param resource the resource required to be added.
     * @param value value of points that the satisfaction of requirements bestow to the score of a player.
     */
    public void addReqMap(Resource resource, int value){
        reqMap.put(resource, value);
    }
}
