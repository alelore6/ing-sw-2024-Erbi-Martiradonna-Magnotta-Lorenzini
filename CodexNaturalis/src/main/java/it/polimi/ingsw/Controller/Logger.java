package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.GenericEvent;

import java.io.PrintStream;

public class Logger{
    private final PrintStream out = new PrintStream(System.out, true);
    private final PrintStream outErr = new PrintStream(System.err, true);

    public Logger() {
    }

    public void addLog(GenericEvent event, Severity s){
        if(event == null){ // it means it is receiving an event soon
            // useless for me
            // out.println("[" + s + "] listening for an event...");
        }
        else if(s == Severity.FAILURE)   outErr.println("[FAILURE] " + event.nickname + "'s " + event.getClass() + " event: " + event.msgOutput() + "\n");
        else{
            out.println("[" + s + "] " + event.nickname + "'s " + event.getClass());
        }
    }

    public void addLog(String message, Severity s){
        out.println("[" + s + "] " + message);
    }
}
