package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;


public class ObjectiveCard3 {
    //TODO aggiungere extends e costruttore
    Map <Resource,Integer> ObjectiveCard3;
    ObjectiveCard3(){
        ObjectiveCard3 =new HashMap<Resource,Integer>();
        //TODO da sistemare
        for (Resource k: Resource.values()){
            ObjectiveCard3.put(k,0);
        }
    }
}