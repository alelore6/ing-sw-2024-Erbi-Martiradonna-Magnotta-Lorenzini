package it.polimi.ingsw.Model;

import java.io.Serializable;

public class Corner implements Serializable {
    protected Position pos;
    protected Resource resource;
    public boolean isCovered;

    public Corner(Position pos, Resource resource){
        isCovered = false;
        this.pos = pos;
        this.resource = resource;
    }

    public String getPosition(){
        return pos == null ? null : pos.toString();
    }
    public Resource getResource(){
        return resource;
    }
    public String getStringResource(){
        return resource == null ? "none" : resource.toString();
    }
}
