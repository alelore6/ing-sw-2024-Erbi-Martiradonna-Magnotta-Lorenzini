package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.Player;

public class Winner extends GenericEvent{
    private final String[] Ranking;
    public Winner(String nickname, String[] ranking){
        super("The final rankings are: ",nickname);
        Ranking = ranking;
    }
}
