package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);

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
     public ResourceCard getCard(){
         return this;
     }
}
