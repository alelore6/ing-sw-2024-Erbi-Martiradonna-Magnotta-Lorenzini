package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.CurrentResources;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.ModelView.TableCenterView;

/**
 * Event that represent the model request to play a card to a player
 */
public class PlayCardRequest extends GenericRequest {
    public final PlayerView playerView;
    public final TableCenterView tableView;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param handCards the card in the hand of the player
     * @param displayedCards the cards already played by the player
     * @param currentResources the current resources of the player
     */
    public  PlayCardRequest(String nickname, PlayerView playerView, TableCenterView tableView){
        super("Now it's time to play a card. Choose a card from your hand and a position where the card will be played.\n",nickname);
        this.playerView = playerView;
        this.tableView = tableView;
    }

    @Override
    public String msgOutput(){
        String s = super.msgOutput() + "Select a card:\n";

        for(int i = 0; i < 3; i++){
            if(playerView.hand.handCards[i] != null){
                s = s + "(" + String.valueOf(i+1) + ")" + ": " + String.valueOf(playerView.hand.handCards[i].getID()) + "\n";
            }
        }

        return s;
    }

    public String msgOutput2(){
        return "Enter (1) if you want the card faced up, (2) if faced down: ";
    }

    public String msgOutput3(){
        return "Now enter the two coordinates (first row, then column) where the card will be placed (look at the grid for help): ";
    }
}
