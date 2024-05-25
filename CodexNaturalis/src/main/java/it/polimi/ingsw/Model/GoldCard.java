package it.polimi.ingsw.Model;

import java.util.HashMap;

public class GoldCard extends PlayableCard{

    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    private Resource RPoints;

    private boolean RPointsCorner;

    protected HashMap<Resource,Integer> req = new HashMap<>();

    public void flip(){
        isFacedown = !isFacedown;
    }

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
