package it.polimi.ingsw.model;

public abstract class PlayableCard extends Card{
    public int points;
    public Color color;
    public Resource[] resource = new Resource[4];

    public PlayableCard(int ID) {
        super(ID);
    }
}
