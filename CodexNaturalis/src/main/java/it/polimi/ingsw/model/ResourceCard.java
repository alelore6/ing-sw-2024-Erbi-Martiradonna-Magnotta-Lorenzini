package it.polimi.ingsw.model;

import java.util.Optional;

import static it.polimi.ingsw.model.Position.*;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);
        isFacedown = false;

        // TODO insert data from DB

        corners[4] = Optional.of(new Corner(UP_SX, Optional.empty()));
        corners[5] = Optional.of(new Corner(UP_DX, Optional.empty()));
        corners[6] = Optional.of(new Corner(DOWN_SX, Optional.empty()));
        corners[7] = Optional.of(new Corner(DOWN_DX, Optional.empty()));
    }
     public void flip(){
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
