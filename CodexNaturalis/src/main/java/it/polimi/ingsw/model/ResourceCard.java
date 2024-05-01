package it.polimi.ingsw.model;

public class ResourceCard extends PlayableCard{


    public ResourceCard(int ID){
        super(ID);

        isFacedown = false;
    }
     public void flip(){
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
