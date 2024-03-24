package it.polimi.ingsw.model;

public class ResourceCard extends PlayableCard{
    public ResourceCard(int ID){
        super(ID);
        //% inserire i dati dal database
    }
     public void flip(){
         //implementare altro
         isFacedown = !isFacedown;
     }
     public ResourceCard getCard(){
         return this;
     }
}
