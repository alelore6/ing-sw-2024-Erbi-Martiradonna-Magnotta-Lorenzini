package it.polimi.ingsw.model;

public abstract class PlayableCard extends Card{
    private int points;
    private Color color;

    public PlayableCard(int ID) {
        super(ID);
    }
    public int getPoints(){
        return points;
    }
    public Color getColor(){
        return color;
    }
}
