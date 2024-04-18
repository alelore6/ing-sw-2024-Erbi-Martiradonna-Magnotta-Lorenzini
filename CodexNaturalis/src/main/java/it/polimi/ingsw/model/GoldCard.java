package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
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

        isFacedown = false;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/cards/resource_card.json")) {

            ResourceCard[] rCards = gson.fromJson(reader, ResourceCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE non trovato!");

            // TODO: add the catch action
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

    public boolean isRPointsCorner() {
        return RPointsCorner;
    }
}
