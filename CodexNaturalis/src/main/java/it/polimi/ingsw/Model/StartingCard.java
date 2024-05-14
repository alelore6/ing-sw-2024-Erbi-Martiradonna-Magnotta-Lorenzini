package it.polimi.ingsw.Model;

public abstract class StartingCard extends Card{

    public Resource[] resource;

    public StartingCard(int ID){
        super(ID);
    }
    public StartingCard getStartingCard(){
        return this;
    }
}