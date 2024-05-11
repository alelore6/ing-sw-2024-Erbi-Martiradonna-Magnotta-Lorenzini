package it.polimi.ingsw.model;

public class Corner {
    protected Position pos;
    protected Resource resource;
    public boolean isCovered;

    public Corner(Position pos, Resource resource){
        isCovered = false;
    }
    public Resource getResource(){
        return resource;
    }
    public Position getPosition(){return pos;}
}
