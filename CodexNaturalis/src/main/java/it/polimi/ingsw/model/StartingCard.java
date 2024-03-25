package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;

public class StartingCard extends Card{
    public Resource[] resource = new Resource[7];

    public StartingCard(int ID){
        super(ID);
    }
    public void flip(){
        isFacedown = !isFacedown;
    }
    public StartingCard getCard(){
        return this;
    }
}