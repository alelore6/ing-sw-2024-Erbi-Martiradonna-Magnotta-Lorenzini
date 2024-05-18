package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.TokenColor;

import javax.swing.*;

import java.util.LinkedList;

import static java.lang.String.valueOf;

public class GUI extends UI{

    private static MainFrame f;

    public GUI(ClientImpl client) {
        super(client);
        inputEvents = new LinkedList<>();
        f = new MainFrame("CodexNaturalis");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1280,720);
        f.setVisible(true);
        //TODO sistemare mainframe: aggiungere sfondo e levare print card
        printCard(102,false,100,100,0.6);
    }


    @Override
    protected void printCard(Card card) {

    }

    public String setNickname() {
        String s=null;
        while(s==null) {
            s = (String) JOptionPane.showInputDialog(f, "Choose your nickname:", "Choose nickname", JOptionPane.PLAIN_MESSAGE, null, null, null);
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

    protected void printCard(int id, boolean isFacedown, int x, int y, double scale) {
        String idString;
        if(id < 100){
            idString = valueOf(id);
            idString = "0" + idString;

            if(id<10){
                idString = "0" + idString;
            }
        }
        else{
            idString = valueOf(id);
        }

        f.setPrintPath("assets/images/cards/" + (isFacedown ? "back" : "front") + "/" + idString + ".png");
        f.setCoord(x, y, scale);
    }

    @Override
    public void update(GenericEvent e){
        inputEvents.add(e);
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
                    if(inputEvents.isEmpty())   continue;
                    GenericEvent ev = inputEvents.poll();
                    // Ignore all other player's events
                    if(!ev.nickname.equals(client.getNickname())) continue;

                    //debug
                    //printOut(ev.msgOutput());
                    int n=0;
                    GenericEvent newEvent=null;
                    String s=null;
                    Object[] possibilities=null;
                    String message= ev.msgOutput();
                    switch(ev){
                        //TODO per eventi di gioco devo anche aggiornare il frame
                        case NumPlayersRequest e :
                            possibilities = new Object[]{"2", "3", "4"};
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Number of players", JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
                            }
                            newEvent = new NumPlayersResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case ChooseObjectiveRequest e :
                            //mostrare dati evento aggionando il frame
                            possibilities = new Object[]{"Objective card 1", "Objective card 2"};
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Choose objective", JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
                            }
                            ObjectiveCard choice= s.equals("Objective card 1")?((ChooseObjectiveRequest) ev).objCard1 : ((ChooseObjectiveRequest) ev).objCard2;
                            newEvent = new ChooseObjectiveResponse( choice, client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case DrawCardRequest e :
                            //TODO sarebbero da togliere delle possibilità di scelta in base alle info dell'evento
                            possibilities = new Object[]{"Resource deck","Gold deck","card 1", "card 2", "card 3", "card 4"};
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Draw a card", JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
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
                            //cosa fare???
                            break;

                        case SetTokenColorRequest e :
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Token color", JOptionPane.PLAIN_MESSAGE, null,((SetTokenColorRequest)ev).availableColors.toArray(), null);
                            }
                            newEvent = new SetTokenColorResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case JoinLobby e :
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Set password", JOptionPane.PLAIN_MESSAGE, null, null, null);
                            }
                            newEvent = new SetPassword(s.trim() , client.getNickname());
                            notifyListener(newEvent);
                            break;

                        case PlaceStartingCard e :
                            // can rotate card
                            JOptionPane.showMessageDialog(f, message);
                            notifyListener(ev);
                            break;

                        case StartGame e:
                            //switch to game frame
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case ReturnDrawCard e:
                            //update view
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case ReturnPlayCard e:
                            //update view
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case EndTurn e:
                            //update view
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case FinalRankings e:
                            //show message + rankings
                            n=1;
                            for (String player : ((FinalRankings) ev).Rankings.keySet()){
                                message.concat("\n"+n+". "+player +": " + ((FinalRankings) ev).Rankings.get(player)+ " points");
                                n++;
                            }
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case TurnOrder e:
                            //show message +  order
                            n=1;
                            for (String player : ((TurnOrder) ev).order){
                                message.concat("\n"+n+". "+player);
                                n++;
                            }
                            JOptionPane.showMessageDialog(f, message);
                            break;

                        case AckResponse e:
                            //TODO in alcuni casi aggiorno view e negli altri nulla???
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
