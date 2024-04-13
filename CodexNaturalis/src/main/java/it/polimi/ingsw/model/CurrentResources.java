package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class CurrentResources {
     Map<Resource,Integer> CurrentResources;
     private final Player player;
     CurrentResources(Player player){
         CurrentResources=new HashMap<>();
         this.player=player;
         for (Resource r: Resource.values()){
             CurrentResources.put(r, 0);
         }
     }

     public void update(Card LastPlayedCard, int x, int y){
        //TODO aggiornamento delle risorse basato sulla carta giocata
        // e gli angoli coperti da essa e lato carta
         int addPoints=0;
        //TODO calcolo punti

         player.getToken().move(addPoints);
     }
}
