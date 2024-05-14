package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.CurrentResources;
import it.polimi.ingsw.Model.PlayableCard;

/**
 * Event that represent the model request to play a card to a player
 */
public class PlayCardRequest extends GenericEvent {
    /**
     * the cards in the hand of the player that can be played
     */
    public final PlayableCard[] handCards;
    /**
     * the already played cards of the player
     */
    public final Card[][] displayedCards;
    /**
     * the current resources of the player
     */
    public final CurrentResources currentResources;

    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param handCards the card in the hand of the player
     * @param displayedCards the cards already played by the player
     * @param currentResources the current resources of the player
     */
    public  PlayCardRequest(String nickname, PlayableCard[] handCards, Card[][] displayedCards, CurrentResources currentResources){
        super("Now it's time to play a card. Choose a card from your hand and a position where the card will be played.\n",nickname);
        this.handCards = handCards;
        this.displayedCards = displayedCards;
        this.currentResources = currentResources;
    }

    public boolean choiceIsValid(int n){
        if(n <= handCards.length) return true;

        return false;
    }

    @Override
    public String msgOutput(){
        String s = super.msgOutput() + "Enter the chosen card's number:\n";

        for(int i = 0; i < handCards.length; i++){
            s = s + String.valueOf(i+1) + ": " + String.valueOf(handCards[i].getID()) + "\n";
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
