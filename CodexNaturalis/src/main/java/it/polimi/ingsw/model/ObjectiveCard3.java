package it.polimi.ingsw.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ObjectiveCard3 extends ObjectiveCard {
    private Resource points;
    private Map<Resource, Integer> objectivecard3Map;

    public ObjectiveCard3(int ID) {
        super(ID);
        objectivecard3Map = new HashMap<>();
        for (Resource k : Resource.values()) {
            objectivecard3Map.put(k, 0);
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/assets/cards/objective_card.json")) {
            ObjCard[] oCards = gson.fromJson(reader, ObjCard[].class); //
        } catch (IOException e) {
            System.out.println("Error, not found.");
        }
    }
    public Resource getpoints() {
        return points;
    }
}
