package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.CurrentResources;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.PlayerView;

/**
 * Event that represent the model request to play a card to a player
 */
public class PlayCardRequest extends GenericRequest {
    public final PlayerView playerView;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param handCards the card in the hand of the player
     * @param displayedCards the cards already played by the player
     * @param currentResources the current resources of the player
     */
    public  PlayCardRequest(String nickname, PlayerView playerView){
        super("Now it's time to play a card. Choose a card from your hand and a position where the card will be played.\n",nickname);
        this.playerView = playerView;
    }

    public boolean choiceIsValid(int n){
        if(n <= 3) return true;

        return false;
    }

    @Override
    public String msgOutput(){
        String s = super.msgOutput() + "Enter the chosen card's number:\n";

        for(int i = 0; i < 3; i++){
            s = s + String.valueOf(i+1) + ": " + String.valueOf(playerView.hand.handCards[i].getID()) + "\n";
        }

        return s;
    }

    public String msgOutput2(){
        return "Enter (1) if you want the card faced up, (2) if faced down: \n";
    }

    public String msgOutput3(){
        return "Now enter the two coordinates where the card will be placed (0 to 80 included):\n";
    }
}
