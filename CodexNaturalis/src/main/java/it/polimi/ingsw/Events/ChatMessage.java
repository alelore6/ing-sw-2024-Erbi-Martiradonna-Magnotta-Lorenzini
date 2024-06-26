package it.polimi.ingsw.Events;

/**
 * Class representing a chat message.
 */
public class ChatMessage extends GenericEvent{
    public final String recipient;
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param sender nickname of the player that sends the event
     * @param recipient nickname of the receiver of the message. If null message is sent to all.
     */
    public ChatMessage(String message, String sender, String recipient) {
        super(message, sender);
        this.recipient = recipient;
        this.mustBeSentToAll = recipient == null;
    }
    /**
     * Getter for the event message in a cli friendly format
     * @return the message
     */
    @Override
    public String msgOutput(){
        return "\u001B[51mCHAT | " + nickname + (mustBeSentToAll ? "\u001B[0m says \"" : "\u001B[0m whispers \"") + message + "\"";
    }
}
