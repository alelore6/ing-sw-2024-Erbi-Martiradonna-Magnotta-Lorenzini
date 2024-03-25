package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;


public class ObjectiveCard2 {
    //TODO aggiungere extends e costruttore
    Map <Resource,Integer> ObjectiveCard2;
    ObjectiveCard2(){
        ObjectiveCard2 =new HashMap<Resource,Integer>();
        //TODO da sistemare
        for (Resource k: Resource.values()){
            ObjectiveCard2.put(k,0);
        }
    }
}