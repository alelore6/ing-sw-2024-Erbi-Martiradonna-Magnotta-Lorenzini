package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class StartingCard extends Card{
    public Resource[] resource = new Resource[3];

    public StartingCard(int ID){
        super(ID);
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/assets/cards/starting_card.json")) {
            StartingCard[] sCards = gson.fromJson(reader, StartingCard[].class);
        } catch (IOException e) {
            System.out.println("Error, not found.");
        }
    }
    public void flip(){
        isFacedown = !isFacedown;
    }
    public StartingCard getCard(){
        return this;
    }
}