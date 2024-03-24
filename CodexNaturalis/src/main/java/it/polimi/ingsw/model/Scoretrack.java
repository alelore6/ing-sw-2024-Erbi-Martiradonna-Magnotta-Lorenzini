package it.polimi.ingsw.model;

public class Scoretrack {

    private int[] tokenPos;

    public Scoretrack() {
        tokenPos = new int[29];
    }

    public int[] getTokenPos() {
        return tokenPos;
    }

    //Non ho messo triggerendgame perché era un giro troppo lungo. invece update() della classe currentserousces
    //chiamerà endgame() di Game.
}
