package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectiveCard2 represents the second type of objective, those that add points to your score if certain positional constraints are met.
 */
public class ObjectiveCard2 extends ObjectiveCard {
    /**
     * reMap is a map that holds the required resources and their corresponding values that represents coordinates to v.
     */
    private Map<Resource, Integer> reqMap = new HashMap<Resource, Integer>();

    /**
     * getReqMap is the getter that when called returns
     * @return reqMap
     */
    public Map<Resource, Integer> getReqMap() {
        return reqMap;
    }

    /**
     *  add a resource and its corresponding integer of coordinates
     * @param resource the resource required to be added.
     * @param value value of coordinate in
     */
    public void addReqMap(Resource resource, int value){
        reqMap.put(resource, value);
    }
}
