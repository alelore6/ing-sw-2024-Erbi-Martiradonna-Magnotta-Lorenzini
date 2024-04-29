package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.Player;

public class Winner extends GenericEvent{
    Player[] Winners;
    public Winner(Player[] Winners){
        this.Winners=Winners;
        if (Winners.length==1)
            message=Winners[0].getNickname() + " has won!";
        else {
            message = "It's a tie! ";
            for (Player p:Winners){
                message=message+p.getNickname()+" ";
            }
            message=message+" are the winners!";
        }
    }
}
