package it.polimi.ingsw.Events;

public class ChatAck extends ChatMessage{

    public final boolean isOk;

    /**
     * Constructor
     *
     * @param msg       message describing the event
     * @param sender
     * @param recipient
     * @param isForEveryone
     */
    public ChatAck(ChatMessage msg, boolean isOk) {
        super(msg.message, msg.nickname, msg.recipient, msg.mustBeSentToAll);
        this.isOk = isOk;
    }

    @Override
    public String msgOutput() {
        return isOk ? "The message has been sent correctly " + (recipient.equals("everyone") ? "to everyone" : "to " + recipient) : "The message has not been sent: recipient not found";
    }
}
