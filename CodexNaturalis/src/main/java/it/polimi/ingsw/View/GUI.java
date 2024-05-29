package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.ImageDialog;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.Model.TokenColor;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class GUI extends UI{

    private static MainFrame f;
    private final ImageIcon icon;

    public GUI(ClientImpl client) {
        super(client);
        f = new MainFrame("CodexNaturalis");
        //f.setVisible(true);

        //icon for dialogs
        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/rulebook/01.png"));
        Image imgResized = icon.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
        this.icon=new ImageIcon(imgResized);
        //TODO sistemare mainframe: aggiungere sfondo e levare print card
    }


    @Override
    protected void printCard(Card card) {

    }

    @Override
    protected void printCard(ObjectiveCard card) {

    }

    public String chooseNickname() {
        String s=null;
        while(s==null || s.length()<4 || s.contains(" ")) {
            s = (String) JOptionPane.showInputDialog(f, "Enter your nickname: \n At least 4 characters and no space allowed.", "Choose nickname", JOptionPane.PLAIN_MESSAGE, icon, null, null);
        }
        return s.trim();
    }

    @Override
    public void printErr(String s) {
        System.out.println(s);
    }

    @Override
    public void printOut(String s) {
        System.out.println(s);
    }


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

        @Override
    public void update(GenericEvent e){
        synchronized (inputEvents) {
            inputEvents.add(e);
            System.out.println("[DEBUG] received: "+ e.getClass().getName());
        }
    }

    @Override
    public void notifyListener(GenericEvent e) {
        listener.addEvent(e);
    }


    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                while(true){
                    synchronized (inputEvents) {
                    if(inputEvents.isEmpty())   continue;}
                    GenericEvent ev = inputEvents.poll();
                    // Ignore all other player's events
                    if(!ev.nickname.equals(client.getNickname()) && !ev.nickname.equals("everyone")) continue;

                    int n=0;
                    GenericEvent newEvent=null;
                    String s=null;
                    ArrayList<Object> possibilities=null;
                    String message= ev.getMessage();


                    switch(ev){
                        //TODO per eventi di gioco devo anche aggiornare il frame
                        case NumPlayersRequest e :
                            possibilities = new ArrayList<Object>();
                            possibilities.add("2");
                            possibilities.add("3");
                            possibilities.add("4");
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Number of players", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), null);
                            }
                            newEvent = new NumPlayersResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case ChooseObjectiveRequest e :
                            n=0;
                            try {
                                while(n==0){
                                    ImageDialog dialog = new ImageDialog(f, e.msgOutput(),getCardPath(e.objCard1.getID(),false), getCardPath(e.objCard2.getID(),false),false);
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
                            if(!((DrawCardRequest) ev).goldDeckEmpty)    possibilities.add("Gold deck");
                            if(!((DrawCardRequest) ev).resDeckEmpty)    possibilities.add("Resource deck");
                            n=1;
                            for (PlayableCard c : ((DrawCardRequest) ev).tableCenterView.centerCards){
                                if(c!=null) possibilities.add("Card "+n);
                                n++;
                            }

                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Draw a card", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), null);
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
                            //TODO sistemare quando frame sarà pronto
                            int posx=-1, posy=-1;
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Play a card: choose card", JOptionPane.PLAIN_MESSAGE, icon,null, null);
                            }
                            while(posx==-1) {
                                posx = Integer.parseInt((String) JOptionPane.showInputDialog(f, message, "Play a card: choose posx", JOptionPane.PLAIN_MESSAGE, icon,null, null));
                            }
                            while(posy==-1) {
                                posy = Integer.parseInt((String) JOptionPane.showInputDialog(f, message, "Play a card: choose posy", JOptionPane.PLAIN_MESSAGE, icon,null, null));
                            }
                            newEvent = new PlayCardResponse( client.getNickname(),e.playerView.hand.handCards[Integer.parseInt(s)-1] ,posx,posy);
                            notifyListener(newEvent);
                            break;

                        case SetTokenColorRequest e :
                            possibilities=new ArrayList<Object>();
                            for(TokenColor c : (((SetTokenColorRequest)ev).availableColors)){
                                possibilities.add(c.name());
                            }
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Token color", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), null);
                            }
                            newEvent = new SetTokenColorResponse(TokenColor.valueOf(s).ordinal()+1 , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case JoinLobby e :
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Set password", JOptionPane.PLAIN_MESSAGE, icon, null, null);
                            }
                            newEvent = new SetPassword( client.getNickname(),s.trim());
                            notifyListener(newEvent);
                            break;

                        case PlaceStartingCard e :
                            n=0;
                            try {
                                while(n==0){
                                    ImageDialog dialog = new ImageDialog(f, e.msgOutput(),getCardPath(e.startingCard.getID(),false), getCardPath(e.startingCard.getID(),true),true);
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
                            //f.reactStartGame((StartGame) ev);
                            break;

                        case EndTurn e:
                            //show message + update view
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case TurnOrder e:
                            //show message +  update view
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case ReconnectionRequest e:
                            while(s==null || s.length()<4 || s.contains(" ")) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Reconnection", JOptionPane.PLAIN_MESSAGE, icon, null, null);
                            }
                            newEvent = new ReconnectionResponse( client.getNickname(),s.trim());
                            notifyListener(newEvent);
                            break;

                        case AckResponse e:
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
