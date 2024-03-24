package it.polimi.ingsw.model;

public class TableCenter {

    private ResourceDeck resDeck;  //TODO sostituire "Object" con classi corrette
    private GoldDeck goldDeck;

    private Object objCards;

    private ResourceCard[] resCards = new ResourceCard[2];

    private GoldCard[] goldCards = new GoldCard[2];


    public TableCenter(ResourceDeck resDeck, GoldDeck goldDeck, Object objCards) {  //TODO sostituire OBJECT
        this.resDeck = resDeck;
        this.goldDeck = goldDeck;
        this.objCards = objCards;
    }

    public int getRemainingRes(){
        return 0;
    }

    public int getRemainingGold(){
        return 0;
    }

    public Object getObjCards() {
        return objCards;
    }  //TODO sostituire object

    public static PlayableCard drawAndPosition(PlayableCard playablecard){

    }
}
