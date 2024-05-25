package it.polimi.ingsw.Model;

public abstract class PlayableCard extends Card{

    protected int points;
    protected CardColor color;

    protected int isChecked = 0;

    public int getPoints(){
        return points;
    }
    public CardColor getColor(){
        return color;
    }
}
