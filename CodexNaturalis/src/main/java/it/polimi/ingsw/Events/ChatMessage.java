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
        return "\nCHAT | " + nickname + (mustBeSentToAll ? " says: " : " whispers: ") + message;
    }
}
