package it.polimi.ingsw.Model;

import java.util.HashMap;

public class GoldCard extends PlayableCard{

    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    private Resource RPoints;

    private boolean RPointsCorner;

    public void addReq(Resource resource, int value) {
        req.put(resource, value);
    }

    HashMap<Resource,Integer> req = new HashMap<>();

    public Resource getRPoints() {
        return RPoints;
    }

    public HashMap<Resource, Integer> getReq() {
        return req;
    }

    public boolean isRPointsCorner() {
        return RPointsCorner;
    }
}
