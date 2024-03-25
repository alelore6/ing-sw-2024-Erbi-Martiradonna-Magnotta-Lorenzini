
package it.polimi.ingsw.model;


public class ObjectiveCard1 extends ObjectiveCard{

    public int[] req1= new int[3];
    public color[] Color= new color[4];

    public ObjectiveCard getCard(){
        return objectivecard;
    }

    public int getPoints(){
        return points;
    }
}