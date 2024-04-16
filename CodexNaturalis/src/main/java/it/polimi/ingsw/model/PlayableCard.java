package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.Color.*;
import static it.polimi.ingsw.model.Position.*;
import static it.polimi.ingsw.model.Position.DOWN_DX;

public abstract class PlayableCard extends Card{
    private int points;
    private Color color;

    // WATCH OUT! I'm assuming that a PlayableCard with ID not in the range 1-80 is NEVER created.

    public PlayableCard(int ID) {
        super(ID);

        // Colors are assigned automatically w/out DB because I think it is faster and easier.
        // I used modulo 40 because colors are distributed the same way every 40 cards (up to 80).
             if (ID%40 >=  1 && ID%40 <= 10)                color = RED;
        else if (ID%40 >= 11 && ID%40 <= 20)                color = GREEN;
        else if (ID%40 >= 21 && ID%40 <= 30)                color = BLUE;
        else if (ID%40 >= 31 || ID%40 ==  0)                color = PURPLE;

        corners[4] = new Corner(UP_SX, null);
        corners[5] = new Corner(UP_DX, null);
        corners[6] = new Corner(DOWN_SX, null);
        corners[7] = new Corner(DOWN_DX, null);
    }
    public int getPoints(){
        return points;
    }
    public Color getColor(){
        return color;
    }
}
