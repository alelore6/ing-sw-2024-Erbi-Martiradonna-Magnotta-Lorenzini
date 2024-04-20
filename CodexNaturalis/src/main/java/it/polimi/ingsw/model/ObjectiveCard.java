package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public abstract class ObjectiveCard {

    protected int ID;
    protected int points;

    ObjectiveCard(int ID) {
        this.ID = ID;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/cards/objective_card.json")) {

            ResourceCard[] rCards = gson.fromJson(reader, ResourceCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE non trovato!");

            // TODO: add the catch action
        }
    }
    public abstract ObjectiveCard getCard();
    public int getpoints(){
        return this.points;
    }
}