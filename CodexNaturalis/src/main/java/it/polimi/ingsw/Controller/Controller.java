package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.Client;
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
    private Lobby lobby;
    /**
     * the actual game, represent the model in the MVC pattern.
     */
    private Game model = null;
    /**
     * the listeners that allow the exchange of information between the MVC pattern.
     */
    private ArrayList<ModelViewListener> MVListeners = new ArrayList<ModelViewListener>();

    private boolean numPlayersRequestSent = false;
    private String newNickname = null;

    private final Object flux_lock = new Object();
    private Integer wait=0;

    /**
     * Constructor
     * @param server the server that handles the connections
     */
    public Controller(ServerImpl server){
        this.server = server;
    }

    /**
     * Creates and starts the actual game
     */
    protected void createGame() throws RemoteException {
        String[] temp = new String[lobby.getNumPlayers()];
        lobby.getPlayers().toArray(temp);
        model = new Game(lobby.getNumPlayers(), temp, MVListeners);

        // NOTIFY ALL LISTENERS OF STARTGAME EVENT
        sendEventToAll(new StartGame("everyone", model.clone()));
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    //wait for everyone to complete the start
                    synchronized (flux_lock){
                        if (wait == lobby.getNumPlayers()) break;
                    }
                }
                wait=0;
                getGame().startGame();
            }
        }.start();
    }

    public void createLobby(int numPlayers) throws RemoteException {
        synchronized(this){
        this.lobby  = new Lobby();
        lobby.setNumPlayers(numPlayers);}
    }

    /**
     * Create the listener for the client that is trying to connect
     * if lobby is empty the number of players for the game must be set
     * Calls lobby.addPlayer() if lobby isn't full
     * communicate the result of the action to the client through his listener
     * @param nickname
     */
    public void addPlayerToLobby(String nickname, ModelViewListener mvListener, String oldNickname) throws RemoteException {
        synchronized (this){
            // If the lobby is empty, the player decides its size.
            if (lobby == null && !numPlayersRequestSent){
                this.newNickname = nickname;

                mvListener.addEvent(new NumPlayersRequest(oldNickname));
                numPlayersRequestSent = true;
            }
            else{
                // Checks the game hasn't started yet.
                if (model == null && lobby!=null) {
                    if(!lobby.addPlayer(nickname)) {
                        server.logger.addLog("Can't add the player.", Severity.WARNING);
                        mvListener.addEvent(new ErrorJoinLobby(nickname, lobby.getNumPlayers() == 0 ? 1 : 0));
                    }
                    else {
                        mvListener.addEvent(new JoinLobby(oldNickname, nickname));
                        if(lobby.getNumPlayers() != 0 && lobby.getNumPlayers() == lobby.getPlayers().size()){
                            createGame();
                        }
                    }
                }
                // This is for rejoin a game
                else if(model != null){
                    boolean isPresent = false;
                    for(Player player : model.getPlayers()){
                        if(player.getNickname().equals(nickname)){
                            isPresent = true;

                            model.rejoin(mvListener);
                        }
                    }
                    if(!isPresent){
                        server.logger.addLog("Can't add the player: the game has already started.", Severity.WARNING);
                        mvListener.addEvent(new ErrorJoinLobby(oldNickname, 2));
                    }
                }
                else{
                    server.logger.addLog("Can't add the player: lobby isn't ready.", Severity.WARNING);
                    mvListener.addEvent(new ErrorJoinLobby(oldNickname, 1));
                }
            }
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
                createLobby(((NumPlayersResponse) event).numPlayers);
                getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, null, (GenericResponse) event, true));
                //the first player is added after creating the lobby
                if(!lobby.addPlayer(nickname)) {
                    server.logger.addLog("Can't add the player", Severity.WARNING);
                    getMVListenerByNickname(nickname).addEvent(new ErrorJoinLobby(nickname, 1));
                }
                else {
                    String newNickname = null;
                    if(this.newNickname != null){
                        newNickname = this.newNickname;
                        this.newNickname = null;
                    }
                    getMVListenerByNickname(nickname).addEvent(new JoinLobby(nickname, newNickname));

                    if(lobby.getNumPlayers() != 0 && lobby.getNumPlayers() == lobby.getPlayers().size()){
                        createGame();
                    }
                }
            }
            else if(event instanceof ReconnectionResponse){
                // The password is correct.
                if(((ReconnectionResponse) event).getPassword().equals(passwords.get(nickname))){
                    synchronized (server.disconnectedClients){
                        server.disconnectedClients.remove(nickname);
                    }

                    getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, null, (GenericResponse) event, true));

                    server.register(((ReconnectionResponse) event).client);
                    model.notifyAll();
                }
                else{
                    GenericResponse ack = new AckResponse(event.nickname, "Can't rejoin: the password is incorrect.", (GenericResponse) event, false);
                    ModelViewListener listener = getMVListenerByNickname(event.nickname);
                    listener.addEvent(ack);

                    // wait until the ack is sent
                    while(!listener.getPendingAck().equals(ack)){
                    }

                    MVListeners.remove(listener);
                }
            }
            else if(event instanceof ChatMessage){

                if(event.mustBeSentToAll)   sendEventToAll(event);
                else if(server.findClientByNickname(((ChatMessage) event).recipient, null) != null)
                    getMVListenerByNickname(((ChatMessage) event).recipient).addChatMessage((ChatMessage) event);
                else {
                    getMVListenerByNickname(nickname).addEvent(new ChatAck((ChatMessage) event, false));
                    return;
                }

                getMVListenerByNickname(nickname).addEvent(new ChatAck((ChatMessage) event, true));
            }

            else if(event instanceof ChooseObjectiveResponse) {
                getPlayerByNickname(nickname).chooseObjective(((ChooseObjectiveResponse) event).objectiveCard);
                getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, null, (GenericResponse) event, true));
            }

            else if(event instanceof DrawCardResponse){
                int chosenPosition = ((DrawCardResponse)event).position;
                boolean isException = false;
                //If position between 0 and 3 the player draws from the centered cards in the table center.
                if(chosenPosition <= 3){
                    try {
                        //TODO collegarsi al tablecenter per fare pescata anche di carte a terra
                        //make the draw
                        getPlayerByNickname(nickname).getHand().DrawPositionedCard(model.getTablecenter().getCenterCards()[chosenPosition]);
                        // send ack
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) event, model.clone()));

                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (isEmptyException e) {
                        //isException = true;
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, e.getMessage(), (GenericResponse) event, false));
                    }catch(HandFullException e2){
                        model.turnPhase++;
                        nextPlayer();
                    }
                }
                //else if position is 4 or 5 (exceeds the centered cards array) it means the player
                //wants to draw either from the ResourceDeck or the GoldDeck

                else if(chosenPosition == 4){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) event, model.clone()));

                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (isEmptyException e) {
                        isException = true;
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, e.getMessage(), (GenericResponse) event, false));
                    }catch(HandFullException e2){

                    }
                }
                else if(chosenPosition == 5){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getGoldDeck());
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) event, model.clone()));

                        //getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (isEmptyException e) {
                        isException = true;
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, e.getMessage(), (GenericResponse) event, false));
                    }catch(HandFullException e2){

                    }
                }
                if(!isException){
                    model.turnPhase++;
                    nextPlayer();
                }
            }

            else if(event instanceof PlayCardResponse){
                try {
                    synchronized (model.controllerLock){
                        getPlayerByNickname(nickname).getHand().playCard(((PlayCardResponse)event).card, ((PlayCardResponse)event).posX, ((PlayCardResponse)event).posY);
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) event, model.clone()));
                        model.turnPhase++;
                    }
                    //getMVListenerByNickname(nickname).addEvent(new ReturnPlayCard(nickname,getPlayerByNickname(nickname).getHand().getDisplayedCards().clone(),getPlayerByNickname(nickname).getCurrentResources()));
                } catch (WrongPlayException e) {
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, e.message, (GenericResponse) event, false));
                }
            }

            else if(event instanceof SetTokenColorResponse){
               boolean ok=getPlayerByNickname(nickname).setToken(((SetTokenColorResponse)event).tokenColor);
                if (ok){
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) event, model.clone()));
//                    synchronized (model.controllerLock){
//
//                        model.waitNumClient++;
//                        model.controllerLock.notifyAll();
//                    }
                }
                else getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, "Color already taken. Please try again.", (GenericResponse) event, false));
            }
            else if(event instanceof PlaceStartingCard){
                try {
                        getPlayerByNickname(nickname).placeStartingCard(((PlaceStartingCard) event).startingCard);
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, (GenericResponse) null, model.clone()));

                        synchronized (model.lock) {
                            model.waitNumClient++;
                        }
                } catch (WrongPlayException e) {
                    //shouldn't happen
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, "Error in placing starting card", null, false));                }
            }
            else if(event instanceof SetPassword){
                passwords.put(event.nickname, ((SetPassword) event).getPassword());
                getMVListenerByNickname(nickname).addEvent(new AckResponse(nickname, null, (GenericResponse) event, true));
                //String p = passwords.get(event.nickname);
                synchronized (flux_lock) {wait++;}
            }
    }

    /**
     * Getter for a specific player in the game
     * @param nickname the player's nickname
     * @return the player
     */
    public Player getPlayerByNickname(String nickname){
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

        for(String nickname : server.getClients().keySet()) getMVListenerByNickname(nickname).addEvent(event);
    }

    public ModelViewListener getMVListenerByNickname(String nickname){
        try {
            for(int i = MVListeners.size() - 1; i >= 0; i--){
                if(MVListeners.get(i).nickname.equals(nickname)){
                    return MVListeners.get(i);
               }
            }
            throw new RuntimeException();
        } catch (NullPointerException e) { //SHOULDN'T HAPPEN
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

    private void nextPlayer() throws RemoteException {
        if(model.turnPhase!=2) System.out.println("Something went wrong with previous player's turn");
        //end turn event
        EndTurn endTurn=new EndTurn(model.getCurrentPlayer(),model.players[model.getCurPlayerPosition()].getNickname(),model.clone());
        sendEventToAll(endTurn);
        if(model.getRemainingTurns() == 0) model.checkWinner();
        else model.nextPlayer(model.players[model.getCurPlayerPosition()]);
    }

    public void disconnectPlayer(String nickname){
        model.disconnectPlayer(getPlayerByNickname(nickname));
    }

    public void deleteClient(Client client) throws RemoteException {
        ModelViewListener listener = getMVListenerByNickname(client.getNickname());
        listener.isActive = false;
        server.getClients().remove(client.getNickname());
        MVListeners.remove(listener);
    }
}
