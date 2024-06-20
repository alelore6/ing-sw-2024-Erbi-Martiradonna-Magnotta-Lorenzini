package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.CardComponent;
import it.polimi.ingsw.Graphical.ImageDialog;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.*;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

import static java.lang.String.valueOf;

/**
 * Represent the logic behind the connection between the server and the client's interface
 */
public class GUI extends UI{
    /**
     * the frame that contains the GUI
     */
    private MainFrame f;
    /**
     * the nickname of the player
     */
    private String nickname;
    /**
     * lock for synchronizations
     */
    private Object lock=new Object();

    /**
     * Constructor
     * @param client the owner of the GUI
     */
    public GUI(ClientImpl client) {
        super(client);
        f = new MainFrame( );
    }

    /**
     * method that allows the player to set his nickname.
     * This method is blocking that means that until the player enter his nickname everything will be waiting (see also run method)
     * @return the player's nickname
     */
    public String chooseNickname() {
        //mando l'evento per gestirlo dentro run con swingUtilities.invokeLater
        update(new ChooseNickname("message","everyone"));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread speciale che aspetta l'input
        Callable<String> task = () -> {
            synchronized(lock) { lock.wait();}
            //System.out.println("Nickname: "+nickname);
            return nickname;
        };

        //aspetta il return del thread sopra
        Future<String> future = executor.submit(task);

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
    * method to print errors in the console
     */
    @Override
    public void printErr(String s) {
        System.err.println(s);
    }

    /**
     * method to print messages in the console
     * @param s the message to print
     */
    @Override
    public void printOut(String s) {
        System.out.println(s);
    }

    /**
     * static method that build the path to an image based on the card info
     * @param id the card id
     * @param isFacedown the boolean representing if the card is showing his front or his back
     * @return the path to the image of the card
     */
    public static String getCardPath(int id, boolean isFacedown) {
        String idString;
        if (id < 100) {
            idString = valueOf(id);
            idString = "0" + idString;

            if (id < 10) {
                idString = "0" + idString;
            }
        } else {
            idString = valueOf(id);
        }
        return "assets/images/cards/" + (isFacedown ? "back" : "front") + "/" + idString + ".png";

    }

    /**
     * An event is received from the server, it is added to the queue where it will be handled in the run method.
     * @param e th received event
     */
    @Override
    public void update(GenericEvent e){

        //TODO gestire messaggi chat asincroni
        synchronized (inputEvents) {
            inputEvents.add(e);
            System.out.println("[DEBUG] received: "+ e.getClass().getName());
        }
    }

    /**
     * Every time the player make a play action the listener is notified
     * @param e the event that is sent to the listener
     */
    @Override
    public void notifyListener(GenericEvent e) {
        listener.addEvent(e);
    }

    /**
     * Represent  all the logic behind the user interface.
     * Here event are taken from the queue and handled based on their type
     */
    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                while(true){
                    synchronized (inputEvents) {
                    if(inputEvents.isEmpty())   continue;}
                    GenericEvent ev = inputEvents.poll();
                    // Ignore all other player's events
                    if(!ev.nickname.equals(client.getNickname()) && !ev.nickname.equals("everyone") ) continue;

                    int n=0;
                    GenericEvent newEvent=null;
                    String s=null;
                    ArrayList<Object> possibilities=null;
                    String message= ev.getMessage();


                    switch(ev){
                        //TODO per eventi di gioco devo anche aggiornare il frame

                        case ChooseNickname e:

                            synchronized(lock) {
                                while (nickname == null || nickname.length() < 4 || nickname.contains(" ")) {
                                    nickname = f.showDialog("Choose nickname", "Enter your nickname: \n At least 4 characters and no space allowed.", null);
                                }
                                lock.notifyAll();
                            }
                            break;

                        case NumPlayersRequest e :
                            possibilities = new ArrayList<Object>();
                            possibilities.add("2");
                            possibilities.add("3");
                            possibilities.add("4");
                            while(s==null) {
                                s = f.showDialog("Number of players",message, possibilities.toArray());
                            }
                            newEvent = new NumPlayersResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case ChooseObjectiveRequest e :
                            n=0;
                            try {
                                while(n==0){
                                    ImageDialog dialog = new ImageDialog(f, message,getCardPath(e.objCard1.getID(),false), getCardPath(e.objCard2.getID(),false),false);
                                    dialog.setVisible(true);

                                    n= dialog.getChoice();
                                }
                            } catch (IOException ex) {
                                printErr("Error: " + ex.getMessage());
                            }
                            ObjectiveCard chosenCard= n==1?((ChooseObjectiveRequest) ev).objCard1 : ((ChooseObjectiveRequest) ev).objCard2;
                            newEvent = new ChooseObjectiveResponse( chosenCard, client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case DrawCardRequest e :
                            //tolgo delle possibilità di scelta in base alle info dell'evento
                            possibilities = new ArrayList<Object>();
                            if(((DrawCardRequest) ev).goldCardinDeck > 0)    possibilities.add("Gold deck");
                            if(((DrawCardRequest) ev).resCardinDeck > 0)    possibilities.add("Resource deck");
                            n=1;
                            for (PlayableCard c : ((DrawCardRequest) ev).tableCenterView.centerCards){
                                if(c!=null) possibilities.add("Card "+n);
                                n++;
                            }

                            while(s==null) {
                                s = f.showDialog("Draw a card",message, possibilities.toArray());
                            }
                            if (s.contains("deck")){
                                if (s.contains("Resource")) n=4;
                                else n=5;
                            }
                            else n=Integer.parseInt(String.valueOf(s.charAt(s.length()-1)))-1;
                            newEvent = new DrawCardResponse( n, client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case PlayCardRequest e :
                            f.update(e.gameView, true);
                            try {
                                f.getLock().wait();
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            CardComponent card= f.getPlayChoice();
                            newEvent = new PlayCardResponse( client.getNickname(),e.playerView.hand.handCards[card.getCardID()] ,card.getRow(),card.getCol());
                            notifyListener(newEvent);
                            break;

                        case SetTokenColorRequest e :
                            possibilities=new ArrayList<Object>();
                            for(TokenColor c : (((SetTokenColorRequest)ev).availableColors)){
                                possibilities.add(c.name());
                            }
                            while(s==null) {
                                s = f.showDialog("Choose token color",message, possibilities.toArray());
                            }
                            newEvent = new SetTokenColorResponse(TokenColor.valueOf(s).ordinal()+1 , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case JoinLobby e :
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = f.showDialog("Set password",message+"\n Nickname: "+ e.getNewNickname(), null);
                            }
                            f.setNickname(e.getNewNickname());
                            newEvent = new SetPassword( e.getNewNickname(),s.trim());
                            notifyListener(newEvent);
                            break;

                        case PlaceStartingCard e :
                            n=0;
                            try {
                                while(n==0){
                                    ImageDialog dialog = new ImageDialog(f, message,getCardPath(e.startingCard.getID(),false), getCardPath(e.startingCard.getID(),true),true);
                                    dialog.setVisible(true);

                                    n= dialog.getChoice();
                                }
                            } catch (IOException ex) {
                                printErr("Error: " + ex.getMessage());
                            }
                            if (n==2) e.startingCard.isFacedown=true; //rotate card
                            notifyListener(e);
                            break;

                        case StartGame e:
                            //switch to game frame
                            JOptionPane.showMessageDialog(f, message);
                            f.reactStartGame(e.model);
                            break;

                        case EndTurn e:
                            //show message + update view
                            JOptionPane.showMessageDialog(f, message);
                            f.update(e.gameView, false);
                            break;

                        case TurnOrder e:
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case ReconnectionRequest e:
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = f.showDialog("Reconnection",message, null);
                            }
                            newEvent = new ReconnectionResponse( client.getNickname(),s.trim());
                            notifyListener(newEvent);
                            break;

                        case AckResponse e:
                            f.update(e.gameView,false);
                            if(e.response!=null)
                                System.out.println("Received ack for "+ e.response.getClass().getName());
                            break;

                        default:
                            //show message
                            JOptionPane.showMessageDialog(f, message);
                    }

                }
            }
        });
    }
}
