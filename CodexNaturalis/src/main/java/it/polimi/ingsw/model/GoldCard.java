package it.polimi.ingsw.model;

public class GoldCard extends PlayableCard{
    public GoldCard(int ID){
        super(ID);
        //% inserire i dati dal database
    }
    public Resource RPoint;
    public Resource[] req = new Resource[5];

    public void flip(){
        //fare altro
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
    }
}
