package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Graphical.MainFrame;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.Model.TokenColor;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.String.valueOf;

public class GUI extends UI{

    private static MainFrame f;
    private final ImageIcon icon;

    public GUI(ClientImpl client) {
        super(client);
        inputEvents = new LinkedList<>();
        f = new MainFrame("CodexNaturalis");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1280,720);
        f.setVisible(true);
        printCard(102,false,100,100,0.6);
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

    protected String getCardPath(int id, boolean isFacedown) {
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
            //System.out.println("Event received");
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
                    if(!ev.nickname.equals(client.getNickname())) continue;

                    int n=0;
                    GenericEvent newEvent=null;
                    String s=null;
                    ArrayList<Object> possibilities=null;
                    String message= ev.msgOutput();


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
                            /*
                            JSplitPane dataPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                            dataPane.setResizeWeight(0.5);
                            ObjectiveCard obj1 = ((ChooseObjectiveRequest) ev).objCard1;
                            ObjectiveCard obj2 = ((ChooseObjectiveRequest) ev).objCard2;
                            String path=null;
                            for(int i=0; i<2; i++) {
                                if(i==0) path = getCardPath(obj1.getID(), false);
                                else path = getCardPath(obj2.getID(), false);
                                BufferedImage img=null;
                                try {
                                    img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    return;
                                }
                                dataPane.add("Objective card "+i, new JLabel(new ImageIcon(img)));
                            }

                            JDialog dialog = new JDialog(f, "Choose objective",true);
                            dialog.getContentPane().add(dataPane);

                            JButton b1 = new JButton("Choose 1", null);
                            b1.setVerticalTextPosition(AbstractButton.BOTTOM);
                            b1.setHorizontalTextPosition(AbstractButton.CENTER);
                            b1.setActionCommand("Confirm");
                            JButton b2 = new JButton("Choose 2", null);
                            b2.setVerticalTextPosition(AbstractButton.BOTTOM);
                            b2.setHorizontalTextPosition(AbstractButton.CENTER);
                            b2.setActionCommand("Confirm");
                            //Listen for actions on buttons
                            //b1.addActionListener();
                            //b2.addActionListener();
                            dialog.getContentPane().add(b1);
                            dialog.getContentPane().add(b2);
                            */
                            //mostrare dati evento
                            possibilities = new ArrayList<Object>();
                            possibilities.add("Objective card 1");
                            possibilities.add("Objective card 2");
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Choose objective", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), null);
                            }
                            ObjectiveCard choice= s.equals("Objective card 1")?((ChooseObjectiveRequest) ev).objCard1 : ((ChooseObjectiveRequest) ev).objCard2;
                            newEvent = new ChooseObjectiveResponse( choice, client.getNickname());
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
                            //cosa fare???
                            break;

                        case SetTokenColorRequest e :
                            possibilities=new ArrayList<Object>();
                            for(TokenColor c : (((SetTokenColorRequest)ev).availableColors)){
                                possibilities.add(c.name());
                            }
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Token color", JOptionPane.PLAIN_MESSAGE, icon, possibilities.toArray(), null);
                            }
                            newEvent = new SetTokenColorResponse(Integer.parseInt(s) , client.getNickname());
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
                            possibilities=new ArrayList<Object>();
                            possibilities.add("Front");
                            possibilities.add("Back");
                            while(s==null) {
                                s = (String) JOptionPane.showInputDialog(f, message, "Place starting card", JOptionPane.PLAIN_MESSAGE, icon,possibilities.toArray(), null);
                            }
                            // can rotate card
                            JOptionPane.showMessageDialog(f, message);
                            if (s.equalsIgnoreCase("Back")) e.startingCard.isFacedown=true;
                            notifyListener(ev);
                            break;

                        case StartGame e:
                            //switch to game frame
                            JOptionPane.showMessageDialog(f, message);
                            f.reactStartGame((StartGame) ev);
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
                            //TODO in alcuni casi aggiorno view e negli altri nulla???
                            System.out.println("Received ack for "+ e.event.getClass().getName());
                            break;

                        default:
                            //show message
                            JOptionPane.showMessageDialog(f, message);
                    }

                    //TODO per alcuni eventi bisognerebbe aspettare l'ack prima di andare avanti
                }
            }
        });
    }
}
