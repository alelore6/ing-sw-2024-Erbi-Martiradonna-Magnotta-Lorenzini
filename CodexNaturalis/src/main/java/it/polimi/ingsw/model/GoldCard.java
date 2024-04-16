package it.polimi.ingsw.model;

import java.util.HashMap;

import static it.polimi.ingsw.model.Position.*;

public class GoldCard extends PlayableCard{

    // WATCH OUT! In the back of the gold cards, there's no requirement nor points
    private final GoldResource RPoints;
    final HashMap<Resource,Integer> req = new HashMap<>();

    public GoldCard(int ID){
        super(ID);

        for (Resource k: Resource.values()){
            // Inserts requested resources
            // Those that aren't requested are set to 0
            req.put(k,0);
        }

        isFacedown = false;

        // TODO: insert data from DB

    }

    public void flip(){
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
    }
}
