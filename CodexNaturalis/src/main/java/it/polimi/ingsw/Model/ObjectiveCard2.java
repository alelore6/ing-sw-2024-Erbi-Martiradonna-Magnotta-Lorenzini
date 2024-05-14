package it.polimi.ingsw.Model;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ObjectiveCard2 extends ObjectiveCard {
    private int points;

    public Map<Resource, Integer> getObjectivecard2Map() {
        return objectivecard2Map;
    }

    private Map<Resource, Integer> objectivecard2Map;

    public ObjectiveCard2(int ID) {
        super(ID);
        objectivecard2Map = new HashMap<>();
        for (Resource k : Resource.values()) {
            objectivecard2Map.put(k, 0);
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/assets/data/objective_cards.json")) {
            ObjectiveCard2[] oCards = gson.fromJson(reader, ObjectiveCard2[].class); //
        } catch (IOException e) {
           System.out.println("Error, not found.");
        }
    }
    public int getpoints() {
        return points;
    }
}
