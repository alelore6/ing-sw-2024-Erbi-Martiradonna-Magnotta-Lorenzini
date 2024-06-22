
package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.CardColor;

public class ConverterCardColorInt {
    public static int getCardColorId(CardColor color) {
        switch (color) {
            case RED:
                return 1;
            case GREEN:
                return 2;
            case PURPLE:
                return 3;
            case BLUE:
                return 4;
            case WHITE:
                return 5;
            default:
                throw new IllegalArgumentException("Unknown CardColor: " + color);
        }

    }
}
