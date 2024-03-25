package it.polimi.ingsw.model;

public abstract class PlayableCard extends Card{
    private int points;
    private Color color;

    // 0: UP_SX, 1: UP_DX, 2: DOWN_SX, 3: DOWN_DX
    public Resource[] resource = new Resource[4];

    public PlayableCard(int ID) {
        super(ID);
    }
    public int getPoints(){
        return points;
    }
    public Color getColor(){
        return color;
    }
    public Resource getResource(Position pos){
        return switch (pos) {
            case UP_SX   -> resource[0];
            case UP_DX   -> resource[1];
            case DOWN_SX -> resource[2];
            case DOWN_DX -> resource[3];
        };
    }
}
