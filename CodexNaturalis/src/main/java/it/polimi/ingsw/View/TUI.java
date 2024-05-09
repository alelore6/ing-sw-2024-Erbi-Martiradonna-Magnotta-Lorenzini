package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.PrivateSocket;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.PlayableCard;
import it.polimi.ingsw.model.StartingCard;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class TUI extends UI {

    private final Scanner in;
    private final PrintStream out;
    private final PrintStream outErr;
    private Deque<GenericEvent> inputMessages;
    private volatile boolean isActive;


    public TUI(String nickname) {
        super(nickname);

        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
        outErr = new PrintStream(System.err, true);
        inputMessages = new LinkedList<>();
        isActive = true;
    }

    public TUI(ClientImpl client) {
        super(client);

        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
        outErr = new PrintStream(System.err, true);
        inputMessages = new LinkedList<>();
        isActive = true;
    }

    private final GenericEvent pollMsg(){
        return this.inputMessages.poll();
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

    public final void notifyListeners(ViewControllerListener listener, GenericEvent e) {
        listener.addEvent(e);
    }

    private final boolean isMessagesQueueEmpty(){
        return this.inputMessages.isEmpty();
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

    public final int chooseView(){
        printOut("Choose if you wanna play from CLI or GUI: 1 for CLI and 2 for GUI:");

        return chooseInt(1,2);
    }

    public final String chooseString(String s){
        // TODO: capire se alcuni caratteri non sono permessi (tipo ' ')
        //       e aggiornare di conseguenza

        boolean isValid = false;
        String tempNickname = null;

        printOut("Insert " + s + ": ");

        while(!isValid){
            tempNickname = in.next();

            // Here we can put controls con the characters of the string inserted
            if(true) // se tutto ok allora
                isValid = true;
        }

        return tempNickname;
    }

    public final int chooseConnection(){

        printOut("Choose between RMI (1) or PrivateSocket (2) connection type");

        int networkType = chooseInt(1,2);

        return networkType;
    }

    public final PrivateSocket setupSocket(){

        int PORT_MAX = 65536;

        printOut("Enter server IP address: ");

        // Do we suppose that this input is always correct?
        String ip = in.next();

        printOut("Enter server port number (between 0 and " + String.valueOf(PORT_MAX) + " included): ");

        int port = chooseInt(0, PORT_MAX);

        return new PrivateSocket(ip, port);
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

    public final void update(GenericEvent e) throws RemoteException {
        inputMessages.add(e);
    }

    protected void printCard(Card card){
        if(card instanceof PlayableCard){

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
                    if(inputMessages.size() == 0)   continue;

                    GenericEvent ev = inputMessages.poll();

                    printOut(ev.msgOutput());

                    int n;

                    switch(ev){
                        case DrawCardRequest e :
                            listener.addEvent(new DrawCardResponse(chooseInt(1,2),client.getNickname()));
                            break;
                        case ChooseObjectiveRequest e :
                            listener.addEvent(new ChooseObjectiveResponse(e.getChosenCard(chooseInt(1,2)), client.getNickname()));
                            break;
                        case NumPlayersRequest e :
                            listener.addEvent(new NumPlayersResponse(chooseInt(2,4), client.getNickname()));
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
                            listener.addEvent(new PlayCardResponse(client.getNickname(), e.handCards[n-1], chooseInt(0, 80), chooseInt(0, 80)));
                            break;
                        case SetTokenColorRequest e :
                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,4);
                            }while(!e.choiceIsValid(n));
                            break;
                        case JoinLobby e :
                            listener.addEvent(new SetPassword(client.getNickname(), chooseString("password")));
                            break;
                        case PlaceStartingCard e :
                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.startingCard.isFacedown = true;
                            listener.addEvent(new PlayCardResponse(client.getNickname(), e.startingCard, 40, 40));
                            break;
                        case AckResponse ack :
                            if(!ack.ok){
                                inputMessages.addFirst(ack.event);
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
