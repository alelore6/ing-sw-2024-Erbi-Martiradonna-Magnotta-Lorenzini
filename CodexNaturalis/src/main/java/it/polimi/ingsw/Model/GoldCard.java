package it.polimi.ingsw.Model;

import java.util.HashMap;

public class GoldCard extends PlayableCard{

    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    private Resource RPoints;

    private boolean RPointsCorner;

    protected HashMap<Resource,Integer> req = new HashMap<>();

    public GoldCard(int ID){
        super(ID);

        for (Resource k: Resource.values()){
            // Inserts requested resources
            // Those that aren't requested are set to 0
            req.put(k,0);
        }
    }

    public void flip(){
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
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
