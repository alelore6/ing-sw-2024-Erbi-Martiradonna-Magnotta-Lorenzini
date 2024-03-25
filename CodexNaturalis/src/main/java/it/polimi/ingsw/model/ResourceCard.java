package it.polimi.ingsw.model;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);
        //% insert data from DB
    }
     public void flip(){
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
