package it.polimi.ingsw.model;

import java.util.Optional;

public class Corner {
    private final Position pos;
    private final Optional<Resource> resource;
    private Card cardOn;
    public boolean isCovered;

    public Corner(Position pos, Optional<Resource> resource){
        this.pos = pos;
        this.resource = resource;
        isCovered = false;
    }
    protected void setCardOn(PlayableCard cardOn){
        this.cardOn = cardOn;
    }
    public Optional<Resource> getResource(){
        return resource;
    }
}
