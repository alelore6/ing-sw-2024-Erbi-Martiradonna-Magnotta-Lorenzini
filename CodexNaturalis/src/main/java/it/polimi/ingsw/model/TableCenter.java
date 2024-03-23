package it.polimi.ingsw.model;

public class TableCenter {

    private Object resDeck;  //TODO sostituire "Object" con classi corrette
    private Object goldDeck;

    private Object objCards;

    private Object[] resCards = new Object[2];

    private Object[] goldCards = new Object[2];


    public TableCenter(Object resDeck, Object goldDeck, Object objCards) {
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
    }

    public void drawAndPosition(Object playablecard){

    }
}
