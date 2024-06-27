package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Events.GenericEvent;

import java.io.PrintStream;

/**
 * Class that prints on the server the most important operations such as sending/receiving events
 * or some warning/failure.
 */
public class Logger{
    /**
     * Object to print on System.out.
     */
    private final PrintStream out = new PrintStream(System.out, true);
    /**
     * Object to print on System.err.
     */
    private final PrintStream outErr = new PrintStream(System.err, true);

    /**
     * Constructor
     *
     */
    public Logger() {}

    /**
     * Method for adding an event to the logger.
     * @param event the handled event
     * @param s the severity of the event
     */
    public void addLog(GenericEvent event, Severity s){
        if(event == null){}
        else if(s == Severity.FAILURE)   outErr.println("[FAILURE] " + event.nickname + "'s " + event.getClass().getName() + " event: " + event.msgOutput() + "\n");
        else{
            out.println("[" + s + "] " + event.nickname + "'s " + event.getClass().getName().split("\\.")[event.getClass().getName().split("\\.").length - 1] + ".");
        }
    }

    /**
     * Method for adding a string to the logger.
     * @param message the message to be added in the log
     * @param s the severity of the message
     */
    public void addLog(String message, Severity s){
        out.println("[" + s + "] " + message);
    }
}
