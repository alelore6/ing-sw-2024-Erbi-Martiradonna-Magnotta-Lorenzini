package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class ObjectiveCard1 extends ObjectiveCard{
    public int[] req1= new int[3];
    public Color[] Color= new Color[4];
    public ObjectiveCard1(int ID) {
        super(ID);
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("src/main/resources/assets/cards/objective_card.json")
        } {
            ObjCard[] oCards = gson.fromJson(reader, ObjCard[].class); //
        } catch(IOException e){
            System.out.println("Error, not found.");
        }
    }
    public ObjectiveCard getCard(){
        return objectivecard;
    }

    public int getPoints(){
        return points;
    }
}