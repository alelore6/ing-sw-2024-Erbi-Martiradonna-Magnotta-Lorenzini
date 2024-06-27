package it.polimi.ingsw.Events;

/**
 * Ack for chat messages.
 */
public class ChatAck extends ChatMessage{
    /**
     * represent if the relative chat message has been sent correctly
     */
    private final boolean isOk;

    /**
     * Constructor
     *
     * @param msg the chat message describing the event.
     * @param isOk the ack's outcome.
     */
    public ChatAck(ChatMessage msg, boolean isOk) {
        super(msg.message, msg.nickname, msg.recipient);
        this.isOk = isOk;
    }
    /**
     * Getter for the event message in a cli friendly format
     * @return the message
     */
    @Override
    public String msgOutput() {
        return isOk ? "The message has been sent correctly " + (recipient == null ? "to everyone." : "to " + recipient + ".") : "\u001B[31m" + "The message has not been sent: recipient not found." + "\u001B[0m";
    }
}
