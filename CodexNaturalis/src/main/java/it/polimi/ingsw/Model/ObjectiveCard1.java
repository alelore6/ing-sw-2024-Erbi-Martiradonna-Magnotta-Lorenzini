package it.polimi.ingsw.Model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ObjectiveCard1 extends ObjectiveCard{
    private int points;

    private int[] requiredPositions; //POSIZIONE carta di colore "1", poi la 2 e poi la 3
    private Color[] cardColors; //Colore 1, 2 e 3
    public ObjectiveCard1(int ID) throws FileNotFoundException {
        super(ID);
        Gson gson = new Gson();

        try{
            FileReader reader = new FileReader("src/main/resources/assets/data/objective_cards.json");
            ObjectiveCard1[] oCards = gson.fromJson(reader, ObjectiveCard1[].class); //
        } catch(IOException e) {
            System.out.println("Error, not found.");
        }
    }

    public int getpoints() {
        return points;
    }

    public int[] getRequiredPositions() {
        return requiredPositions;
    }

    public Color[] getCardColors() {
        return cardColors;
    }




}