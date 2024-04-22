package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;


import com.google.gson.Gson;
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