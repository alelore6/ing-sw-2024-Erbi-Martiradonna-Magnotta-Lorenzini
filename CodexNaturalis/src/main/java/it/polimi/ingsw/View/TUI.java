package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.PrivateSocket;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.*;

public class TUI extends UI {

    private final Scanner in;
    private final PrintStream out;
    private final PrintStream outErr;
    private Queue<GenericEvent> inputMessages;
    private volatile boolean isActive;

    private final Object queueLock;

    public TUI(String nickname) {
        super(nickname);

        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
        outErr = new PrintStream(System.err, true);
        inputMessages = new LinkedList<>();
        queueLock = new Object();
        isActive = true;
    }

    public final void update(GenericEvent e) throws RemoteException {

        synchronized (queueLock){
            this.inputMessages.add(e);
        }

        // Should Listeners be notified that the message has been received?
    }

    private final GenericEvent pollMsg(){
        synchronized (queueLock){
            return this.inputMessages.poll();
        }
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
                out.println("Input error. Try again:");

                continue;
            }

            if(choice >= min && choice <= max)  isValid = true;
            if(!isValid)                        out.println("Wrong number inserted. Try again:");
        }

        return choice;
    }

    public final void notifyListener(ViewControllerListener listener, GenericEvent e) {
        listener.addEvent(e);
    }

    @Override
    public final void notifyAll(GenericEvent e) {
        for (ViewControllerListener l : listeners) {
            notifyListener(l,e);
        }
    }

    private final boolean isMessagesQueueEmpty(){
        synchronized (queueLock){
            return this.inputMessages.isEmpty();
        }
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

    @Override
    public final void addListener(ViewControllerListener listener) {
        listeners.add(listener);
    }

    public final int chooseView(){
        out.println("Choose if you wanna play from CLI or GUI: 1 for CLI and 2 for GUI:");

        return chooseInt(1,2);
    }

    public final String chooseNickname(){
        // TODO: capire se alcuni caratteri non sono permessi (tipo ' ')
        //       e aggiornare di conseguenza
        boolean isValid = false;
        String tempNickname = null;

        out.println("Insert your nickname:");

        while(!isValid){
            tempNickname = in.nextLine();

            // Here we can put controls con the characters of the string inserted
            if(true) // se tutto ok allora
                isValid = true;
        }

        return tempNickname;
    }

    public final int chooseConnection(){

        out.println("Choose between RMI (1) or PrivateSocket (2) connection type");

        int networkType = chooseInt(1,2);

        return networkType;
    }

    public final PrivateSocket setupSocket(){

        int PORT_MAX = 65536;

        out.println("Enter server IP address: ");

        // Do we suppose that this input is always correct?
        String ip = in.nextLine();

        out.println("Enter server port number (between 0 and 65536 included): ");

        int port = chooseInt(0, PORT_MAX);

        return new PrivateSocket(ip, port);
    }

    public final void printSomething(String s){
        out.println(s);
    }

    public final void printErr(String err){
        outErr.println(err);
    }

    public void login(){

    }

    public void stop(){
        isActive = false;

        // Do the listeners have to be notified?
    }

    @Override
    public void run() {
        while(isActive){
            if(inputMessages == null)   continue;

            GenericEvent ev = inputMessages.poll();

            printSomething(ev.msgOutput());

            switch(ev){
                case DrawCardRequest e :
                    chooseInt(1,2);
                    break;
                case ChooseObjectiveRequest e :
                    chooseInt(1,2);
                    break;
                case ChooseObjectiveResponse e :
                    chooseInt(2,4);
                    break;
                case PlayCardRequest e :
                    chooseInt(0, 80);
                    chooseInt(0, 80);
                    break;
                case SetTokenColorRequest e :
                    int n = -1;
                    do{
                        if(n != -1) printSomething("Incorrect choice. Please, try again: ");
                        n = chooseInt(1,4);
                    }while(!e.choiceIsValid(n));
                    break;
                default :
                    break;
            }
        }
    }
}
