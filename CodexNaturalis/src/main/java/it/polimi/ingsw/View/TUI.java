package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Model.*;

import java.io.PrintStream;
import java.util.*;

public class TUI extends UI {

    private final Scanner in;
    private final PrintStream out;
    private final PrintStream outErr;
    private volatile boolean isActive;
    private Object lock_events = new Object();

    public TUI(ClientImpl client) {
        super(client);
        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
        outErr = new PrintStream(System.err, true);
        inputEvents = new LinkedList<>();
        isActive = true;
    }

    // This method allows to receive the user's choice between min and max included.
    // It runs until a proper answer isn't given.
    // WATCH OUT! This method is meant only for inputs. The proper output must be implemented elsewhere
    //                                                  (e.g. let the user know the mapping between min:max and choices)
    private final int chooseInt(int min, int max){
        int     choice  = 0;
        boolean isValid = false;

        while(!isValid){
            try{
                choice = in.nextInt();
            } catch (InputMismatchException e) {
                // to skip the wrong input and try with the next one.

                in.nextLine();
                printOut(inputError());

                continue;
            }

            if(choice >= min && choice <= max)  isValid = true;
            if(!isValid)                        printOut(inputError());
        }

        return choice;
    }

    public final void notifyListener(GenericEvent e) {
        listener.addEvent(e);
    }

    public final static void clearConsole(){
        try{
            final String so = System.getProperty("os.name");

            if (so.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (Exception ignored){

        }
    }

    public String setNickname() {
        return setString("nickname");
    }

    private final String setString(String s){
        // TODO: capire se alcuni caratteri non sono permessi (tipo ' ')
        //       e aggiornare di conseguenza

        boolean isValid = false;
        String tempPassword = null;

        printOut("Insert " + s + ": ");

        while(!isValid){
            tempPassword = in.next();

            // Here we can put controls con the characters of the string inserted
            if(true) // se tutto ok allora
                isValid = true;
        }

        return tempPassword;
    }

    public final void printOut(String s){
        out.println(s);
    }

    public final void printErr(String err){
        outErr.println(err);
    }

    public void stop(){
        isActive = false;

        // Do the listeners have to be notified?
    }

    private static String inputError(){
        return "Input not allowed. Please try again";
    }

    public final void update(GenericEvent e){
        synchronized(lock_events){
            inputEvents.add(e);
        }
    }

    protected void printCard(Card card){
        printOut("| CARD NUMBER " + card.getID() + "'S DESCRIPTION:");
        if(card instanceof PlayableCard){
            printOut("Color: " + ((PlayableCard) card).getColor() +
                        "Visible corners: ");
            Arrays.stream(card.getCorners()).forEach(c -> printOut(
                    c.getPosition() + ": " + c.getResource() != null ? c.getResource().toString() : "empty"));

            if(card instanceof GoldCard){
                // the number of total distinct resources
                int NUM_RES = 7;

                printOut("Requirements:");
                for(Resource res : ((GoldCard) card).getReq().keySet()){
                    if(((GoldCard) card).getReq().get(res) > 0)
                        printOut(((GoldCard) card).getReq().get(res) + res.toString());
                }
            }

            if(((PlayableCard) card).getPoints() == 0)  return;

            if(((PlayableCard) card).getPoints() > 0){
                printOut("\nReward:\t" + ((PlayableCard) card).getPoints() + "points");
                if(card instanceof GoldCard){
                    printOut("\t for every " + (((GoldCard) card).isRPointsCorner() ? "covered corner." : ((GoldCard) card).getRPoints().toString()) + ".");
                }
            }
            printOut("The back of the card has four (all) empty corners with a resource in the center (of the corresponding color).");
        }
        else if(card instanceof StartingCard){

        }
    }

    @Override
    public void run() {

        clearConsole();

        new Thread(){
            @Override
            public void run() {
                while(isActive){
                    GenericEvent ev = null;

                    synchronized(lock_events){
                        if(inputEvents.isEmpty())   continue;

                        ev = inputEvents.poll();
                    }

                    // Ignore all other player's events
                    if(!ev.nickname.equals(client.getNickname())) continue;

                    printOut(ev.msgOutput());

                    int n;
                    GenericEvent newEvent;

                    switch(ev){
                        case DrawCardRequest e :
                            newEvent = new DrawCardResponse(chooseInt(1,2),client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case ChooseObjectiveRequest e :
                            newEvent = new ChooseObjectiveResponse(e.getChosenCard(chooseInt(1,2)), client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case NumPlayersRequest e :
                            newEvent = new NumPlayersResponse(chooseInt(2,4), client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case PlayCardRequest e :
                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,80); // 80 is a secure upper bound for this choice (lower would be dangerous).
                            }while(!e.choiceIsValid(n));

                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.handCards[n-1].isFacedown = true;

                            printOut(e.msgOutput3());
                            newEvent = new PlayCardResponse(client.getNickname(), e.handCards[n-1], chooseInt(0, 80), chooseInt(0, 80));
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());

                            break;
                        case SetTokenColorRequest e :
                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,4);
                            }while(!e.choiceIsValid(n));

                            newEvent = new SetTokenColorResponse(chooseInt(1,4), client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());

                            break;
                        case JoinLobby e :
                            newEvent = new SetPassword(client.getNickname(), setString("password"));
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());

                            break;
                        case PlaceStartingCard e :
                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.startingCard.isFacedown = true;

                            newEvent = new PlayCardResponse(client.getNickname(), e.startingCard, 40, 40);
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());

                            break;
                        case AckResponse ack :
                            if(!ack.ok){
                                synchronized(lock_events){
                                    inputEvents.addFirst(ack.event);
                                }
                            }

                            break;
                        default :

                            break;
                    }
                }
            }
        }.start();
    }
}
