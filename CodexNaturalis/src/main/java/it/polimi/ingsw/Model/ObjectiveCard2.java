package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectiveCard2 represents the second type of objective, those that add points to your score if certain positional constraints are met.
 */
public class ObjectiveCard2 extends ObjectiveCard {

    private Map<Resource, Integer> reqMap = new HashMap<Resource, Integer>();

    public Map<Resource, Integer> getReqMap() {
        return reqMap;
    }
    public void addReqMap(Resource resource, int value){
        reqMap.put(resource, value);
    }
}
