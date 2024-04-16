package it.polimi.ingsw.model;

import java.util.Optional;

public class Corner {
    private final Position pos;
    private final Resource resource;
    public boolean isCovered;

    public Corner(Position pos, Resource resource){
        this.pos = pos;
        this.resource = resource;
        isCovered = false;
    }
    public Resource getResource(){
        return resource;
    }
}
