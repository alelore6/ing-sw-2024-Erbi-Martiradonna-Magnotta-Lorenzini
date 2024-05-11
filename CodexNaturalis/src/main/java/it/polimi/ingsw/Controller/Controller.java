package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.Exceptions.WrongPlayException;
import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.model.Player;
import org.springframework.ui.Model;

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
    private ArrayList<ModelViewListener> mvListeners = new ArrayList<ModelViewListener>();

    /**
     * Constructor
     * @param server the server that handles the connections
     */
    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();
    }

    /**
     * Creates and starts the actual game
     */
    protected void createGame(){
        String[] nicknames = new String[lobby.getPlayers().size()]; //crea l'array di nicknames dei player
        nicknames = lobby.getPlayers().toArray(nicknames); //fills the nicknames array
        model = new Game(lobby.getNumPlayers(), nicknames, mvListeners);


        for(int i = 0; i < mvListeners.size(); i++){
            //NOTIFY ALL LISTENERS OF STARTGAME EVENT
            mvListeners.get(i).addEvent(new StartGame(lobby.getPlayers().get(i)));
        }

        getGame().startGame();
    }

    public void endGame(int occasion){
        //( finchè non spostiamo)endGame viene gestito all'interno del model
        // al di fuori arriva solo la notifica che è stato triggerato
        //forse si può utilizzare quando viene conclusa del tutto la partita
        // dopo checkWinner per chiudere del tutto la partita e dare la possibilità di una nuova.
        getGame().endGame(occasion);
    }

    /**
     * Create the listener for the client that is trying to connect
     * if lobby is empty the number of players for the game must be set
     * Calls lobby.addPlayer() if lobby isn't full
     * communicate the result of the action to the client through his listener
     * @param nickname
     */
    public void addPlayerToLobby(String nickname) throws RemoteException {
        boolean ok = false;

        // creo listener
        ModelViewListener mvListener = new ModelViewListener(server);
        mvListener.handleEvent();

        //check game hasn't started
        if (model == null) {
            if (lobby.getNumPlayers() == 1) {
                // se la lobby è vuota evento SetNumPlayer
                NumPlayersRequest numPlayersRequest = new NumPlayersRequest(nickname);
                mvListener.addEvent(numPlayersRequest);
            }
            if(!lobby.addPlayer(nickname))
                mvListener.addEvent(new ErrorJoinLobby(nickname));
            else {
                mvListener.addEvent(new JoinLobby(nickname));
                mvListeners.add(mvListener);
            }
        }
        else mvListener.addEvent(new ErrorJoinLobby(nickname));
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
            //TODO ackresponse necessario anche dove non lancio eccezioni (tipo qua)? Non credo
            // qui anche no perchè non dovrebbe mai dare errore
            // ma in settokencolor si perchè può avere errori (setToken() ritorna un booleano -> usa quello)
            if(event instanceof NumPlayersResponse){
                lobby.setNumPlayers(((NumPlayersResponse) event).numPlayers);
            }

            else if(event instanceof ChooseObjectiveResponse) {
                getPlayerByNickname(nickname).chooseObjective(((ChooseObjectiveResponse) event).objectiveCard);
            }

            else if(event instanceof DrawCardResponse){
                //TODO mandare evento returnDrawCard solo al giocatore che compie l'azione quando esito positivo
                int chosenPosition = ((DrawCardResponse)event).position;
                //If position between 0 and 3 the player draws from the centered cards in the table center.
                if(chosenPosition <= 3){
                    try {
                        //make the draw
                        getPlayerByNickname(nickname).getHand().DrawPositionedCard(model.getTablecenter().getCenterCards()[chosenPosition]);
                        // send ack
                        for(int i = 0; i < mvListeners.size(); i++){
                            mvListeners.get(i).addEvent(new AckResponse(true, nickname, event));
                        }
                        //send updated handcards
                        getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));
                    } catch (HandFullException | isEmptyException e) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event));
                    }
                }
                //else if position is 4 or 5 (exceeds the centered cards array) it means the player
                //wants to draw either from the ResourceDeck or the GoldDeck

                else if(chosenPosition == 4){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());
                        for(int i = 0; i < mvListeners.size(); i++){
                            mvListeners.get(i).addEvent(new AckResponse(true, nickname, event));
                        }
                        getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));

                    } catch (HandFullException | isEmptyException e) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event));
                    }
                }
                else if(chosenPosition == 5){
                    try {
                        getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());
                        for(int i = 0; i < mvListeners.size(); i++){
                            mvListeners.get(i).addEvent(new AckResponse(true, nickname, event));
                        }
                        getMVListenerByNickname(nickname).addEvent(new ReturnDrawCard(getPlayerByNickname(nickname).getHand().getHandCards().clone(),nickname));

                    } catch (HandFullException | isEmptyException e ) {
                        getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event));
                    }
                }
            }

            else if(event instanceof PlayCardResponse){
                try {
                    getPlayerByNickname(nickname).getHand().playCard(((PlayCardResponse)event).card, ((PlayCardResponse)event).posX, ((PlayCardResponse)event).posY);
                    for(int i = 0; i < mvListeners.size(); i++){
                        mvListeners.get(i).addEvent(new AckResponse(true, nickname, event));
                    }
                    getMVListenerByNickname(nickname).addEvent(new ReturnPlayCard(nickname,getPlayerByNickname(nickname).getHand().getDisplayedCards().clone(),getPlayerByNickname(nickname).getCurrentResources()));
                } catch (WrongPlayException e) {
                    getMVListenerByNickname(nickname).addEvent(new AckResponse(false, nickname, event));
                }
            }

            else if(event instanceof SetTokenColorResponse){
               getPlayerByNickname(nickname).setToken(((SetTokenColorResponse)event).tokenColor);
            }
            else if(event instanceof PlaceStartingCard){
                try {
                    getPlayerByNickname(nickname).placeStartingCard(((PlaceStartingCard) event).startingCard);
                } catch (WrongPlayException e) {
                    //non penso serva ackResponse perchè non dovrebbe mai dare errore
                    throw new RuntimeException(e);
                }
            }
            else if(event instanceof SetPassword){
                passwords.put(event.nickname, ((SetPassword) event).getPassword());
                String p = passwords.get(event.nickname);
                System.out.println(p);
            }
            else if(event instanceof ClientRegister){
                server.register(new ClientImpl(((ClientRegister) event).getNickname()));
            }
    }

    /**
     * Getter for a specific player in the game
     * @param nickname the player's nickname
     * @return the player
     */
    //method to get player by nickname not to repeat the same code over and over again
    private Player getPlayerByNickname(String nickname){

        try {
            for (int i = 0; i < model.getNumPlayers(); i++) {
                if (Objects.equals(model.getPlayers()[i].getNickname(), nickname)) {
                    return model.getPlayers()[i];
                }
            }
            throw new PlayerNotFoundException(nickname);
        } catch (PlayerNotFoundException e) {
            System.out.println("Player " + nickname + " not found\n");
            return null;
        }



    }

    private ModelViewListener getMVListenerByNickname(String nickname){
        try {
            for(int i = 0; i < getGame().getPlayers().length; i++){
                if(getGame().getPlayers()[i].getNickname().equals(nickname)){
                    return mvListeners.get(i);
               }
            }
            throw new RuntimeException();
        } catch (Exception e) { //SHOULDN'T HAPPEN
            System.out.println("MVListener not found\n");
            return null;
        }

    }
}
