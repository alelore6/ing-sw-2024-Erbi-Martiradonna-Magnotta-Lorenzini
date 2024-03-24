package it.polimi.ingsw.model;

public class Corner {
    private final Position pos;
    private final Resource resource;
    private Card cardOn;
    public boolean isCovered;

    public Corner(Position pos, Resource resource){
        this.pos = pos;
        this.resource = resource;

        isCovered = false;
    }
}
