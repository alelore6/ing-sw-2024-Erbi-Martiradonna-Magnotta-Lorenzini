package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Controller {

    private final ServerImpl server;
    private final Lobby lobby;
    private Game model;
    private ArrayList<ModelViewListener> mvListeners;

    public Controller(ServerImpl server){
        this.server = server;
        lobby = new Lobby();
    }

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
        getGame().endGame(occasion);
    }

    public void addPlayerToLobby(String nickname){
        boolean ok=false;

        // creo listener
        if(lobby.getNumPlayers()==0 || mvListeners.size() < lobby.getNumPlayers()) {
            ModelViewListener mvListener = new ModelViewListener(server);
            mvListeners.add(mvListener);

            //check game hasn't started
            if (model == null) {
                if (lobby.getNumPlayers()==0) {
                    // se la lobby è vuota evento SetNumPlayer
                    NumPlayersRequest numPlayersRequest = new NumPlayersRequest(nickname);
                    mvListener.addEvent(numPlayersRequest);
                }
                if(!lobby.addPlayer(nickname))
                    mvListener.addEvent(new ErrorJoinLobby(nickname));
            }
            else{
                mvListener.addEvent(new ErrorJoinLobby(nickname));
            }

        }
    }

    public Game getGame(){
        return this.model;
    }

    public void updateModel(GenericEvent event, String nickname){
        //TODO agire sul model in base al tipo di evento

            //couldn't do switch case because it supports only primitive types and Strings. (That I know of)
            //besides the code efficiency here is not valued as important as its functionality

            if(event instanceof NumPlayersResponse){
                lobby.setNumPlayers(((NumPlayersResponse) event).numPlayers);
            }

            else if(event instanceof ChooseObjectiveResponse) {
                getPlayerByNickname(nickname).chooseObjective(((ChooseObjectiveResponse) event).objectiveCard);
            }

            else if(event instanceof DrawCardResponse){
                //TODO exception from DrawFromDeck and DrawPositionedCard is not to be dealt here (i think)
                int chosenPosition = ((DrawCardResponse)event).position;
                //If position between 0 and 3 the player draws from the centered cards in the table center.
                if(chosenPosition <= 3){
                    getPlayerByNickname(nickname).getHand().DrawPositionedCard(model.getTablecenter().getCenterCards()[chosenPosition]);
                }
                //else if position is 4 or 5 (exceeds the centered cards array) it means the player
                //wants to draw either from the ResourceDeck or the GoldDeck
                else if(chosenPosition == 4){getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());}
                else if(chosenPosition == 5){getPlayerByNickname(nickname).getHand().DrawFromDeck(model.getTablecenter().getResDeck());}
            }

            else if(event instanceof PlayCardResponse){
               //TODO exception must not be dealt here I think!
               getPlayerByNickname(nickname).getHand().playCard(((PlayCardResponse)event).card, ((PlayCardResponse)event).posX, ((PlayCardResponse)event).posY);
            }

            else if(event instanceof SetTokenColorResponse){
               getPlayerByNickname(nickname).getToken().setColor(((SetTokenColorResponse)event).tokenColor);
            }
    }



    //method to get player by nickname not to repeat the same code over and over again
    Player getPlayerByNickname(String nickname){
        for (int i = 0; i < model.getNumPlayers(); i++) {
            if (model.getPlayers()[i].getNickname() == nickname) {
                return model.getPlayers()[i];
            }
        }
        return null;
    }
}
