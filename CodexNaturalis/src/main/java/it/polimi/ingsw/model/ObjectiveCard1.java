package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class ObjectiveCard1 extends ObjectiveCard{
    private int points;
    private int[] req;
    private Color[] color;
    public ObjectiveCard1(int ID) {
        super(ID);
    }

    public int getpoints() {
        return points;
    }

    Gson gson = new Gson();

    try(FileReader reader = new FileReader("src/main/resources/assets/cards/objective_card.json")) {
        ObjectiveCard1[] oCards = gson.fromJson(reader, ObjectiveCard1[].class); //
    } catch (IOException e) {
        System.out.println("Error, not found.");
    }


}