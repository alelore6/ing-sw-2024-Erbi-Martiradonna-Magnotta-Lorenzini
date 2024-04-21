
package it.polimi.ingsw.model;


public class ObjectiveCard1 extends ObjectiveCard{
    // req and color have a 1 to 1 mapping: it means that req[i] is referred to color[i].
    // They also have length 3.
    private int[] req;
    private Color[] color;

    public ObjectiveCard1(int ID) {
        super(ID);
    }

    //TODO aggiungere costruttore

    public ObjectiveCard getCard(){
        return this;
    }
}