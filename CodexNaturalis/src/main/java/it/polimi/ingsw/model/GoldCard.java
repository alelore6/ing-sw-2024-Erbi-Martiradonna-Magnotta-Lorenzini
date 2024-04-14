package it.polimi.ingsw.model;

import java.util.HashMap;

public class GoldCard extends PlayableCard{
    public GoldCard(int ID){
        super(ID);
        //% insert data from DB
        for (Resource k: Resource.values()){
            //insert requested resources
            // those that aren't requested are set to 0
            req.put(k,0);
        }
    }
    // WARNING! In the back of the gold cards, there's no requirement nor points
    public GoldResource RPoint;
    HashMap<Resource,Integer> req = new HashMap<>();

    public void flip(){
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
    }
}
