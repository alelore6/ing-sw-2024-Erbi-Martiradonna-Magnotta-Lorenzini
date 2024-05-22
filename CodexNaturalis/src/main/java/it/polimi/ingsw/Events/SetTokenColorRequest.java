package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.TokenColor;

import java.util.ArrayList;

/**
 * Event that represent the model request to choose the color of the token of the player
 */
public class SetTokenColorRequest extends GenericEvent{
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
        super("Choose token color from these:\n", nickname);
        this.availableColors = availableColors;
    }

    @Override
    public String msgOutput() {

        String s = null;

        if(availableColors.contains(TokenColor.RED))        s = s + "(1) for RED";
        if(availableColors.contains(TokenColor.YELLOW))     s = s + "(2) for YELLOW";
        if(availableColors.contains(TokenColor.GREEN))      s = s + "(3) for GREEN";
        if(availableColors.contains(TokenColor.BLUE))       s = s + "(4) for BLUE";

        return super.msgOutput() + availableColors.toString() + "\nColor: ";
    }

    public boolean choiceIsValid(int n){
        if(n == 1 && availableColors.contains(TokenColor.RED))      return true;
        if(n == 2 && availableColors.contains(TokenColor.YELLOW))   return true;
        if(n == 3 && availableColors.contains(TokenColor.GREEN))    return true;
        if(n == 4 && availableColors.contains(TokenColor.BLUE))     return true;
        else                                                        return false;
    }
}
