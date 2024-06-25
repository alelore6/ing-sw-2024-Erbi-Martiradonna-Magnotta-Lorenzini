package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.CardComponent;
import it.polimi.ingsw.Graphical.ImageDialog;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.*;

import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

import static java.lang.String.valueOf;

/**
 * Represent the logic behind the connection between the server and the client's  graphic interface
 */
public class GUI extends UI{
    /**
     * the frame that contains the GUI
     */
    private MainFrame f;
    /**
     * lock for synchronizations
     */
    private final Object lock=new Object();

    /**
     * Constructor: calls the constructor of the mainFrame class
     * @param client the owner of the GUI
     */
    public GUI(ClientImpl client) {
        super(client);
        f = new MainFrame(this);
    }

    /**
     * method that allows the player to set his nickname creating an event that is handled in the run method.
     * This method is blocking that means that until the player enter his nickname everything will be waiting (see also run method).
     * @return the player's nickname
     */
    public String chooseNickname() {
        update(new ChooseNickname("message","everyone"));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread with return parameter
        Callable<String> task = () -> {
            synchronized(lock) { lock.wait();}
            //System.out.println("Nickname: "+nickname);
            return nickname;
        };

        //start the thread and save his return in a future type
        Future<String> future = executor.submit(task);

        try {
            f.setNickname(future.get());
            return future.get();
        } catch (InterruptedException |ExecutionException  e) {
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
     * When an event is received from the server, it is added to the queue where it will be handled in the run method.
     * Asynchronous event are handled directly from here.
     * @param e the received event
     */
    @Override
    public void update(GenericEvent e){
        if (e instanceof ChatMessage){ if(!e.nickname.equals(nickname))f.addChatMessage(e.nickname,e.getMessage());}
        else {
            if (e instanceof FinalRankings) {
                f.addChatMessage("game", e.getMessage());
                JOptionPane.showMessageDialog(f,e.getMessage());
                notifyListener(new AckResponse(nickname, (FinalRankings) e));
            }
            else if(e instanceof EndGameTriggered){
                    f.addChatMessage("game", e.getMessage());
                    JOptionPane.showMessageDialog(f, e.getMessage());
            }
            else if( e instanceof  PlayerDisconnected) f.addChatMessage("game", e.getMessage());
            else synchronized (inputEvents) {
                    inputEvents.add(e);
                    System.out.println("[DEBUG] received: "+ e.getClass().getName());
            }
        }
    }

    /**
     * notify view-controller listener on a new chat message from this player
     * @param message the text message
     */
    public void sendChatMessage(String message){
        listener.addEvent(new ChatMessage(message,nickname,null));
    }

    /**
     * notify view-controller listener on a new private chat message from this player
     * @param message the text message
     * @param nickname the receiver of the message
     */
    public void sendPrivateChatMessage(String message, String nickname){listener.addEvent(new ChatMessage(message,this.nickname,nickname));}
    /**
     * Represent the logic behind the client's user interface and his connection to the game.
     * Here event are taken from the queue and handled based on their type
     */
    @Override
    public void run() {

        Thread GUI = new Thread(){
            @Override
            public void run(){
        while(running){
                    synchronized (inputEvents) {
                    if(inputEvents.isEmpty())   continue;}
                    GenericEvent ev = inputEvents.poll();
                    // Ignore all other player's events
                    if(!ev.mustBeSentToAll && !ev.nickname.equals(client.getNickname())) continue;

                    int n=0;
                    GenericEvent newEvent=null;
                    String s=null;
                    ArrayList<Object> possibilities=null;
                    String message= ev.getMessage();


                    switch(ev){

                        case ChooseNickname e:
                            synchronized(lock) {
                                while (nickname == null || nickname.length() < 4 || nickname.contains(" ")) {
                                    nickname = f.showInputDialog("Choose nickname", "Enter your nickname: \n At least 4 characters and no space allowed.", null);
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
                                s = f.showInputDialog("Number of players",message, possibilities.toArray());
                            }
                            newEvent = new NumPlayersResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case ChooseObjectiveRequest e :
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
                            f.update(e.gameView, 2);
                            JOptionPane.showMessageDialog(f, message);
                            synchronized(f.getLock()) {
                                try {
                                    f.getLock().wait();
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            newEvent = new DrawCardResponse( f.getDrawChoice(), client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case PlayCardRequest e :
                            f.update(e.gameView, 1);
                            synchronized(f.getLock()) {
                                try {
                                    f.getLock().wait();
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            CardComponent card=f.getPlayChoice();
                            if (card.isFlipped()) e.getPlayerView(e.nickname).hand.handCards[card.getCardID()].isFacedown=true;
                            newEvent = new PlayCardResponse( client.getNickname(),e.getPlayerView(e.nickname).hand.handCards[card.getCardID()] ,card.getRow(),card.getCol());
                            notifyListener(newEvent);
                            break;

                        case SetTokenColorRequest e :
                            possibilities=new ArrayList<Object>();
                            for(TokenColor c : e.availableColors){
                                possibilities.add(c.name());
                            }
                            while(s==null) {
                                s = f.showInputDialog("Choose token color",message, possibilities.toArray());
                            }
                            switch (TokenColor.valueOf(s)){
                                case  TokenColor.RED-> n=1;
                                case TokenColor.YELLOW -> n=2;
                                case TokenColor.GREEN -> n=3;
                                case TokenColor.BLUE -> n=4;
                            }
                            newEvent = new SetTokenColorResponse(n , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case JoinLobby e :
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = f.showInputDialog("Set password",message+"\n Nickname: "+ e.getNewNickname(), null);
                            }
                            f.setNickname(e.getNewNickname());
                            f.addChatMessage("game", "You have joined the game. Waiting for other players to start the game.");
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
                            break;
                        case StartTurn e:
                            //show message + update view
                            f.addChatMessage("game",e.gameView.tableCenterView.scoreTrack.points.toString());
                            if(e.turnPlayer.equals(nickname)) JOptionPane.showMessageDialog(f, message);
                            else f.addChatMessage("game", e.getMessage());
                            f.update(e.gameView,0);
                            break;

                        case EndTurn e:
                            //show message + update view
                            f.addChatMessage("game", e.getMessage());
                            f.update(e.gameView, 0);
                            break;

                        case TurnOrder e:
                            f.addChatMessage("game", e.getMessage());
                            f.reactStartGame(e.gameView);
                            break;

                        case ReconnectionRequest e:
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = f.showInputDialog("Reconnection",message, null);
                            }
                            newEvent = new ReconnectionResponse( client.getNickname(),s.trim());
                            notifyListener(newEvent);
                            break;

                        case ErrorJoinLobby e:
                            n=JOptionPane.showConfirmDialog(f,e.getMessage()+"\nDo you want to try to connect again?");
                            if (n == 0)  notifyListener(new ClientRegister(client));
                            else        System.exit(1);
                            break;

                        case AckResponse e:
                            if (e.receivedEvent instanceof PlayCardResponse || e.receivedEvent instanceof DrawCardResponse) {
                                if(e.ok) f.update(e.gameView,0);
                                else JOptionPane.showMessageDialog(f, message);
                            }
                            if(e.receivedEvent!=null)
                                System.out.println("Received ack for "+ e.receivedEvent.getClass().getName());
                            break;
                        default:
                            //do nothing
                            break;
                    }

                }

            }};
        GUI.start();
    }
}
