package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Player;

import java.io.FileReader;
import java.io.IOException;

public class StartingCard extends Card{

    public Resource[] resource = new Resource[3];

    public StartingCard(int ID){
        super(ID);

        isFacedown = false;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/cards/starting_card.json")) {

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
    public StartingCard getCard(){
        return this;
    }
}