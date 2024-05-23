package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.Exceptions.WrongPlayException;
import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that represent the controller in the MVC pattern.
 * It handles the action of the players on the model.
 */
public class Controller {

    private HashMap<String, String> passwords = new HashMap<String, String>();
    /**
     * the server that handles the connections
     */
    private final ServerImpl server;
    /**
     * the waiting lobby before the game starts
     */
    private final Lobby lobby;
    /**
     * the actual game, represent the model in the MVC pattern.
     */
    private Game model = null;
    /**
     * the listeners that allow the exchange of information between the MVC pattern.
     */
    private ArrayList<ModelViewListener> MVListeners = new ArrayList<ModelViewListener>();

    /**
     * Constructor
     * @param server the server that handles the connections
     */
    public Controller(ServerImpl server){
        this.server = server;
        this.lobby  = new Lobby();
    }

    /**
     * Creates and starts the actual game
     */
    protected void createGame() throws RemoteException {
        String[] temp = new String[lobby.getNumPlayers()];
        lobby.getPlayers().toArray(temp);
        model = new Game(lobby.getNumPlayers(), temp, MVListeners);

        // NOTIFY ALL LISTENERS OF STARTGAME EVENT
        sendEventToAll(new StartGame(null, model.clone()));

        getGame().startGame();
    }

    /**
     * Create the listener for the client that is trying to connect
     * if lobby is empty the number of players for the game must be set
     * Calls lobby.addPlayer() if lobby isn't full
     * communicate the result of the action to the client through his listener
     * @param nickname
     */
    public void addPlayerToLobby(String nickname, ModelViewListener mvListener, String oldNickname) throws RemoteException {
        boolean ok = false;

        // Checks the game hasn't started yet.
        if (model == null) {
            // If the lobby is empty, the player decides its size.
            if (lobby.getNumPlayers() == 0) mvListener.addEvent(new NumPlayersRequest(nickname));
            if(!lobby.addPlayer(nickname)) {
                server.logger.addLog("Can't add the player", Severity.WARNING);
                mvListener.addEvent(new ErrorJoinLobby(nickname));
            }
            else {
                mvListener.addEvent(new JoinLobby(nickname, oldNickname));
                if(lobby.getNumPlayers() != 0 && lobby.getNumPlayers() == lobby.getPlayers().size()){
                    createGame();
                }
            }
        }
        else{
            server.logger.addLog("Can't add the player: the game has already started.", Severity.WARNING);
            mvListener.addEvent(new ErrorJoinLobby(nickname));
        }
    }

    /**
     * Getter for the game
     * @return the game
     */
    public Game getGame(){
        return this.model;
    }

    /**
     * Receive an event from the client and act on the model
     * @param event the event sent from the client
     * @param nickname the player that sent the event
     */
    public void updateModel(GenericEvent event, String nickname) throws RemoteException {
            if(event instanceof NumPlayersResponse){
                lobby.setNumPlayers(((NumPlayersResponse) event).numPlayers);
            }

            else if(event instanceof ChooseObjectiveResponse) {
                getPlayerByNickname(nickname).chooseObjective(((ChooseObjectiveResponse) event).objectiveCard);
            }

            else if(event instanceof DrawCardResponse){
                int chosenPosition = ((DrawCardResponse)event).position;
                //If position between 0 and 3 the player draws from the centered cards in the table center.
                if(chosenPosition <= 3){
                    try {
                        //make the draw
                        getPlayerByNickname(nickname).getHand().DrawPositionedCard(model.getTablecenter().getCenterCards()[chosenPosition]);
                        // send ack
                        MVListeners.get(model.getCurPlayerPosition()).addEvent(new AckResponse(true, nickname, event, model.clone()));
                        model.turnPhase++;
                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (HandFullException | isEmptyException e) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event, model.clone()));
                    }
                }
                //else if position is 4 or 5 (exceeds the centered cards array) it means the player
                //wants to draw either from the ResourceDeck or the GoldDeck

                else if(chosenPosition == 4){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());
                        MVListeners.get(model.getCurPlayerPosition()).addEvent(new AckResponse(true, nickname, event, model.clone()));
                        model.turnPhase++;
                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (HandFullException | isEmptyException e) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event, model.clone()));
                    }
                }
                else if(chosenPosition == 5){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getGoldDeck());
                        MVListeners.get(model.getCurPlayerPosition()).addEvent(new AckResponse(true, nickname, event, model.clone()));
                        model.turnPhase++;
                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));

                    } catch (HandFullException | isEmptyException e ) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event, model.clone()));
                    }
                }
                nextPlayer();
            }

            else if(event instanceof PlayCardResponse){
                try {
                    getPlayerByNickname(nickname).getHand().playCard(((PlayCardResponse)event).card, ((PlayCardResponse)event).posX, ((PlayCardResponse)event).posY);
                    MVListeners.get(model.getCurPlayerPosition()).addEvent(new AckResponse(true, nickname, event, model.clone()));
                    model.turnPhase++;
                    //getMVListenerByNickname(nickname).addEvent(new ReturnPlayCard(nickname,getPlayerByNickname(nickname).getHand().getDisplayedCards().clone(),getPlayerByNickname(nickname).getCurrentResources()));
                } catch (WrongPlayException e) {
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event, model.clone()));
                }
            }

            else if(event instanceof SetTokenColorResponse){
               boolean ok=getPlayerByNickname(nickname).setToken(((SetTokenColorResponse)event).tokenColor);
                if (ok){
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(ok, nickname, event, model.clone()));
                }
                else getMVListenerByNickname(nickname).addEvent(new AckResponse(ok, nickname, event, model.clone()));
            }
            else if(event instanceof PlaceStartingCard){
                try {
                    getPlayerByNickname(nickname).placeStartingCard(((PlaceStartingCard) event).startingCard);
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(true, nickname, event, model.clone()));
                } catch (WrongPlayException e) {
                    //shouldn't happen
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event, model.clone()));                }
            }
            else if(event instanceof SetPassword){
                passwords.put(event.nickname, ((SetPassword) event).getPassword());
                String p = passwords.get(event.nickname);
            }
    }

    /**
     * Getter for a specific player in the game
     * @param nickname the player's nickname
     * @return the player
     */
    private Player getPlayerByNickname(String nickname){
        try {
            for (Player player : model.getPlayers()) {
                if (player.getNickname().equals(nickname)) {
                    return player;
                }
            }
            throw new PlayerNotFoundException(nickname);
        } catch (PlayerNotFoundException e) {
            server.logger.addLog("Player " + nickname + " not found\n", Severity.WARNING);
            return null;
        }
    }

    public void sendEventToAll(GenericEvent event) throws RemoteException {
        event.mustBeSentToAll = true;
        for(ClientSkeleton client : server.getClientSkeletons()) getMVListenerByNickname(client.getNickname()).addEvent(event);
        for(Client client : server.getClientProxies())           getMVListenerByNickname(client.getNickname()).addEvent(event);
    }

    public ModelViewListener getMVListenerByNickname(String nickname){
        try {
            for(ModelViewListener listener : MVListeners){
                if(listener.client.getNickname().equals(nickname)){
                    return listener;
               }
            }
            throw new RuntimeException();
        } catch (Exception e) { //SHOULDN'T HAPPEN
            server.logger.addLog("MVListener not found", Severity.FAILURE);
            return null;
        }
    }

    public ArrayList<ModelViewListener> getMVListeners() {
        return MVListeners;
    }

    public void addMVListener(ModelViewListener listener) throws RemoteException {
        MVListeners.add(listener);
        listener.handleEvent();
    }

    private void nextPlayer(){
        if(model.turnPhase==3){
            //end turn event
            EndTurn endTurn=new EndTurn(model.getCurrentPlayer(),model.players[model.getCurPlayerPosition()].getNickname(),model.clone());
            MVListeners.get(model.getCurPlayerPosition()).addEvent(endTurn);
            if(model.getRemainingTurns() == 0) model.checkWinner();
            else model.nextPlayer(model.players[model.getCurPlayerPosition()]);
        }
    }

    public void disconnectPlayer(String nickname){
        model.disconnectPlayer(getPlayerByNickname(nickname));
    }
}
