package it.polimi.ingsw.Model;

import java.io.FileNotFoundException;

public class ObjectiveCard1 extends ObjectiveCard{


    private int[] req; //POSIZIONE carta di colore "1", poi la 2 e poi la 3
    private CardColor[] color; //Colore 1, 2 e 3

    public int[] getRequiredPositions() {
        return req;
    }

    public CardColor[] getCardColors() {
        return color;
    }




}