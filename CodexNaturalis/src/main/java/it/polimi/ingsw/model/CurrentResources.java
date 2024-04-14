package it.polimi.ingsw.model;

import java.util.HashMap;


public class CurrentResources {
     HashMap<Resource,Integer> CurrentResources;
     private final Player player;
     CurrentResources(Player player){
         CurrentResources=new HashMap<>();
         this.player=player;
         for (Resource r: Resource.values()){
             CurrentResources.put(r, 0);
         }
     }

     protected void update(Card card, Corner[] overlaps){
        //TODO aggiornamento delle risorse basato sulla carta giocata
        // e gli angoli coperti da essa e lato carta
         //if corner is visible but without resource, the attribute resource will be null
         int addPoints=0;
        //TODO calcolo punti

         player.getToken().move(addPoints);
     }
}
