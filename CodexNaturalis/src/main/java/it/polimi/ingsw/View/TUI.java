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

    public void update(GenericEvent e) throws RemoteException {

        synchronized (queueLock){
            this.inputMessages.add(e);
        }

        // Should Listeners be notified that the message has been received?
    }

    private GenericEvent pollMsg(){
        synchronized (queueLock){
            return this.inputMessages.poll();
        }
    }
    // This method allows to receive the user's choice between n distinct and ordered options.
    // It runs until a proper answer isn't given.
    // WATCH OUT! This method is meant only for inputs. The proper output must be implemented elsewhere
    //                                                  (e.g. let the user know the mapping between 1:n and choices)
    public int choose(int n){
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

    public void notifyListener(ViewControllerListener listener, GenericEvent e) {
        listener.addEvent(e);
    }

    @Override
    public void notifyAll(GenericEvent e) {
        for (ViewControllerListener l : listeners) {
            notifyListener(l,e);
        }
    }

    private boolean isMessagesQueueEmpty(){
        synchronized (queueLock){
            return this.inputMessages.isEmpty();
        }
    }

    public final static void clearConsole(){
        try{
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
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
    public void addListener(ViewControllerListener listener) {
        listeners.add(listener);
    }

    private final boolean chooseConnection(){

        out.println("Choose between Socket (1) or RMI (2) connection type");

        int connectionType = choose(2);

        if(connectionType == 1){

            out.println("Setup RMI...");
            try{
                this.setUpRMIClient(ip);
            }catch (RemoteException | NotBoundException | InvalidIPAddress ex ){
                out.println("Cannot connect with RMI. Make sure the IP provided is valid and try again later...");
                return false;
            }
        }else{

            out.println("Connecting with socket...");
            try{
                this.setUpSocketClient(ip);
            }catch (RemoteException | InvalidIPAddress ex ){
                out.println("Cannot connect with socket. Make sure the IP provided is valid and try again later...");
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI

        // wait until the connection type has been chosen

        // login

        new Thread(){

        }.start();



        // ...
    }
}
