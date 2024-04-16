package it.polimi.ingsw.model;

import java.util.Optional;

import static it.polimi.ingsw.model.Position.*;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);

        isFacedown = false;

        // TODO: insert data from DB

    }
     public void flip(){
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
