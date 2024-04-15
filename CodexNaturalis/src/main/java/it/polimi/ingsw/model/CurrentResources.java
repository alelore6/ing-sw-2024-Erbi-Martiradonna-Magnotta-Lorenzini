package it.polimi.ingsw.model;

import java.util.HashMap;


public class CurrentResources {
     HashMap<Resource,Integer> currentResources;
     private final Player player;
     CurrentResources(Player player){
         currentResources=new HashMap<>();
         this.player=player;
         for (Resource r: Resource.values()){
             currentResources.put(r, 0);
         }
     }

     protected void update(Card card, Corner[] overlaps){
        //TODO aggiornamento delle risorse basato sulla carta giocata
         //overlapped resources must be removed
         for (Corner c: overlaps){
             if (c.getResource() !=null){
                 Resource r = c.getResource();
                 if(currentResources.get(r)>0)
                     currentResources.replace(r,currentResources.get(r)-1);
             }
         }
         // facedown cards are different
         if(card.isFacedown){
             if(card instanceof StartingCard){
                 //corners
                for(int i=4; i<8; i++){
                    if (card.corners[i]!=null && card.corners[i].getResource()!=null){
                        Resource r=card.corners[i].getResource();
                        currentResources.replace(r,currentResources.get(r)+1);
                    }
                }
                //central resources
                 for(Resource t : ((StartingCard) card).resource){
                     currentResources.replace(t,currentResources.get(t)+1);
                 }
             }
             else {
                 switch (((PlayableCard) card).getColor()){
                     case RED -> currentResources.replace(Resource.FUNGI,currentResources.get(Resource.FUNGI)+1);
                     case GREEN -> currentResources.replace(Resource.PLANT,currentResources.get(Resource.PLANT)+1);
                     case PURPLE -> currentResources.replace(Resource.INSECT,currentResources.get(Resource.INSECT)+1);
                     case BLUE -> currentResources.replace(Resource.ANIMAL,currentResources.get(Resource.ANIMAL)+1);
                 }
             }
         } else {
             // add new card's resources
         }
         int addPoints=0;
        //TODO calcolo punti

         player.getToken().move(addPoints);
     }
}
