package it.polimi.ingsw.model;

public class GoldCard extends PlayableCard{
    public GoldCard(int ID){
        super(ID);
        //% insert data from DB
    }
    // WARNING! In the back of the gold cards, there's no requirement nor points
    public GoldResource RPoint;
    public Resource[] req = new Resource[5];

    public void flip(){
        isFacedown = !isFacedown;
    }
    public GoldCard getCard(){
        return this;
    }
}
