package it.polimi.ingsw.model;

import java.util.Optional;

import static it.polimi.ingsw.model.Position.*;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);
        isFacedown = false;

        // TODO insert data from DB

        corners[4] = new Corner(UP_SX, null);
        corners[5] = new Corner(UP_DX, null);
        corners[6] = new Corner(DOWN_SX, null);
        corners[7] = new Corner(DOWN_DX, null);
    }
     public void flip(){
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
