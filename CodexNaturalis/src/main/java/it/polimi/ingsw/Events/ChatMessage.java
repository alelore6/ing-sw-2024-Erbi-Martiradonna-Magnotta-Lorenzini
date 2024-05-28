package it.polimi.ingsw.Events;

public class ChatMessage extends GenericEvent{
    public final String recipient;
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ChatMessage(String message, String sender, String recipient, boolean isForEveryone) {
        super(message, sender);
        this.recipient = recipient;
        this.mustBeSentToAll = isForEveryone;
    }

    @Override
    public String msgOutput(){
        return "\u001B[51mCHAT | " + nickname + (mustBeSentToAll ? "\u001B[0m says \"" : "\u001B[0m whispers \"") + message + "\"";
    }
}
