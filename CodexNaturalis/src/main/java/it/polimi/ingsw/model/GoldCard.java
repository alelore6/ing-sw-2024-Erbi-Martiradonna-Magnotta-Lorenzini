package it.polimi.ingsw.model;

import java.util.HashMap;

import static it.polimi.ingsw.model.Position.*;

public class GoldCard extends PlayableCard{

    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    public GoldResource RPoint;
    HashMap<Resource,Integer> req = new HashMap<>();

    public GoldCard(int ID){
        super(ID);

        for (Resource k: Resource.values()){
            // inserts requested resources
            // those that aren't requested are set to 0
            req.put(k,0);
        }

        isFacedown = false;

        // TODO insert data from DB

        corners[4] = new Corner(UP_SX, null);
        corners[5] = new Corner(UP_DX, null);
        corners[6] = new Corner(DOWN_SX, null);
        corners[7] = new Corner(DOWN_DX, null);
    }

    public void flip(){
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
    }
}
