
package it.polimi.ingsw.model;


public class ObjectiveCard1 extends ObjectiveCard{

    public int[] req1= new int[3];
    public Color[] Color= new Color[4];

    public ObjectiveCard1(int ID) {
        super(ID);
    }

    //TODO aggiungere costruttore

    public ObjectiveCard getCard(){
        return this;
    }
}