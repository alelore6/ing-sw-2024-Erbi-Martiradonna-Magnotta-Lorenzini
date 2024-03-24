package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class CurrentResources {
     Map<Resource,Integer> CurrentResources;
     CurrentResources(){
         CurrentResources=new HashMap<>();
         for (Resource r: Resource.values()){
             CurrentResources.put(r, 0);
         }
     }

     public void update(PlayableCard LastPlayedCard){
        // aggiornamento delle risorse

         int addPoints=0;
        //calcolo punti

         Player.getToken().move(addPoints);
     }
}
