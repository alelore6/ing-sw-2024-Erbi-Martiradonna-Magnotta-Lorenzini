package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.CardInfoRequest;
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

    private void getCardInfo(int ID){

        CardInfoRequest ev = new CardInfoRequest(ID, this.nickname);

        // notify Controller
    }

    private int choose(int n){
        int c = 0;
        boolean b = true;

        while(b){

            try{
                c = in.nextInt();
            } catch (InputMismatchException e) {
                // to skip the wrong input and try with the next one.
                in.nextLine();
                out.println("Input error. Try again:");

                continue;
            }

            if(c > 0 && c <= n)     b = false;
            if(b)                   out.println("Wrong number inserted. Try again:");
        }

        return c;
    }

    @Override
    public void notifyAll(GenericEvent e) {

    }

    private boolean isMessagesQueueEmpty(){
        synchronized (queueLock){
            return this.inputMessages.isEmpty();
        }
    }

    @Override
    public void addListener(ViewControllerListener listener) {

    }


    public void notifyListeners(View view, GenericEvent e) {

    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI
    }
}
