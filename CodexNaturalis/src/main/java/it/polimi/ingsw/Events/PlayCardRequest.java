package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.CurrentResources;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.ModelView.TableCenterView;

/**
 * Event that represent the model request to play a card to a player
 */
public class PlayCardRequest extends GenericRequest {
    /**
     * the game info at the moment of the request
     */
    public final GameView gameView;
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param gameView the game info at the moment of the event
     */
    public PlayCardRequest(String nickname, GameView gameView){
        super("Now it's time to play a card. Choose a card from your hand and a position where the card will be played.\n",nickname);
        this.gameView = gameView;
    }

    /**
     * Getter for the player view of the receiver of the event
     * @param nickname the player nickname
     * @return the player info
     */
    public PlayerView getPlayerView(String nickname) {
        return gameView.getPlayerViewByNickname(nickname);
    }

    /**
     * Getter for the table center view
     * @return the table center view
     */
    public TableCenterView getTableView() {
        return gameView.tableCenterView;
    }
    /**
     * Getter for the event message in a cli friendly format
     * @return the message
     */
    @Override
    public String msgOutput(){
        String s = super.msgOutput() + "Select a card:\n";

        for(int i = 0; i < 3; i++){
            if(getPlayerView(nickname).hand.handCards[i] != null){
                s = s + "(" + (i+1) + ")" + ": " + String.valueOf(getPlayerView(nickname).hand.handCards[i].getID()) + "\n";
            }
        }

        return s;
    }
    /**
     * Getter for the second part of the event message in a cli friendly format
     * @return the message
     */
    public String msgOutput2(){
        return "Enter (1) if you want the card faced up, (2) if faced down: ";
    }

    /**
     * Getter for the third part of the event message in a cli friendly format
     * @return the message
     */
    public String msgOutput3(){
        return "Now enter the two coordinates (first row, then column) where the card will be placed (look at the grid for help): ";
    }
}
