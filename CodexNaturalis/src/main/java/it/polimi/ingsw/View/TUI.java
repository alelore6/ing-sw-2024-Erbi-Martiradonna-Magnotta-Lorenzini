package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TUI extends UI {

    private final Scanner in;
    private final PrintStream out;
    private Queue<GenericEvent> inputMessages;

    private final Object queueLock;

    public TUI(String nickname) {
        super(nickname);

        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
        inputMessages = new LinkedList<>();

        queueLock = new Object();
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
    // This method allows to receive the user's choice between n distinct and ordered options.
    // It runs until a proper answer isn't given.
    // WATCH OUT! This method is meant only for inputs. The proper output must be implemented elsewhere
    //                                                  (e.g. let the user know the mapping between 1:n and choices)
    private final int choose(int n){
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

            if(choice > 0 && choice <= n)   isValid = true;
            if(!isValid)                    out.println("Wrong number inserted. Try again:");
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

        return choose(2);
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

        out.println("Choose between RMI (1) or Socket (2) connection type");

        int networkType = choose(2);

        return networkType;
    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI

        // login

        new Thread(){

        }.start();



        // ...
    }
}
