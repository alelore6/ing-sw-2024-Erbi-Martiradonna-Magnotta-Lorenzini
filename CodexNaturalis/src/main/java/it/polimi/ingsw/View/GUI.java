package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.Card;

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
        /*
        String s=null;
        while(s==null) {
            s = (String) JOptionPane.showInputDialog(f, "Choose your nickname:", "Choose nickname", JOptionPane.PLAIN_MESSAGE, null, null, null);
        }
        return s;*/
        return "test";
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
    //nickname settato in setNickname dal costruttore di clientimpl
    //password risposta a join lobby
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
                    printOut(ev.msgOutput());
                    int n;
                    GenericEvent newEvent;
                    switch(ev){

                        case NumPlayersRequest e :
                            Object[] possibilities = {"2", "3", "4"};
                            String s=null;
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, ev.msgOutput(), "Set num players", JOptionPane.PLAIN_MESSAGE, null, possibilities, "4");
                            }
                            newEvent = new NumPlayersResponse(Integer.parseInt(s) , client.getNickname());
                            notifyListener(newEvent);
                            break;
                        /*
                        case ChooseObjectiveRequest e :
                            //mostrare dati evento aggionando il frame
                            //quando decisione presa mando evento risposta
                            //newEvent = new ChooseObjectiveResponse( , client.getNickname());
                            //notifyListener(listener, newEvent);
                            //printOut(newEvent.msgOutput());
                            break;

                        case DrawCardRequest e :
                            break;

                        case PlayCardRequest e :
                            break;

                        case SetTokenColorRequest e :
                            break;

                        case JoinLobby e :
                            //rispondere con password
                            break;

                        case PlaceStartingCard e :
                            break;

                        case
                        */
                        default:
                            //solo messaggio da mostrare
                            JOptionPane.showMessageDialog(f, ev.msgOutput());
                    }
                }
            }
        });
    }
}
