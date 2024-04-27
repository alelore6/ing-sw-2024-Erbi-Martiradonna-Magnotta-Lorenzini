package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class StartingCard extends Card{

    public Resource[] resource;

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