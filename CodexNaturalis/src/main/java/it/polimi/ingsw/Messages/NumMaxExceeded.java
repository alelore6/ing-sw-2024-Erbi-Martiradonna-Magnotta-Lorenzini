package it.polimi.ingsw.Messages;

public class NumMaxExceeded extends Generic{
    private final String msg = "The lobby is full.";

    @Override
    public String toString(){
        return msg;
    }
}
