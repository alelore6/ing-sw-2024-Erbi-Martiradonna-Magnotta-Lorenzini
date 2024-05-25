package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveCard2 extends ObjectiveCard {

    private Map<Resource, Integer> reqMap = new HashMap<Resource, Integer>();

    public Map<Resource, Integer> getReqMap() {
        return reqMap;
    }
    public void addReqMap(Resource resource, int value){
        reqMap.put(resource, value);
    }
}
