package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.TokenColor;

import java.util.ArrayList;

/**
 * Event that represent the model request to choose the color of the token of the player
 */
public class SetTokenColorRequest extends GenericRequest{
    /**
     * the list of available colors
     */
    public final ArrayList<TokenColor> availableColors;

    /**
     * Constructor
     * @param nickname the player that receives the action
     * @param availableColors the list of available colors
     */
    public SetTokenColorRequest(String nickname, ArrayList<TokenColor> availableColors) {
        super("Choose token color from these:", nickname);
        this.availableColors = availableColors;
    }

    public String msgOutput2() {

        String s = "\n";
        String colorReset = "\u001B[0m";

        if(availableColors.contains(TokenColor.RED))        s = s + "\u001B[31m" + "(1) for RED\n" + colorReset;
        if(availableColors.contains(TokenColor.YELLOW))     s = s + "\u001B[33m" + "(2) for YELLOW\n" + colorReset;
        if(availableColors.contains(TokenColor.GREEN))      s = s + "\u001B[32m" + "(3) for GREEN\n" + colorReset;
        if(availableColors.contains(TokenColor.BLUE))       s = s + "\u001B[34m" + "(4) for BLUE\n" + colorReset;

        return s;
    }

    public boolean choiceIsValid(int n){
        if(n == 1 && availableColors.contains(TokenColor.RED))      return true;
        if(n == 2 && availableColors.contains(TokenColor.YELLOW))   return true;
        if(n == 3 && availableColors.contains(TokenColor.GREEN))    return true;
        if(n == 4 && availableColors.contains(TokenColor.BLUE))     return true;
        else                                                        return false;
    }
}
