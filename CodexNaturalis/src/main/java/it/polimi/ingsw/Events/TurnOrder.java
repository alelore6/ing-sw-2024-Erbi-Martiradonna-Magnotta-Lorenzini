package it.polimi.ingsw.Events;

public class TurnOrder extends GenericEvent{
    public final String[] order;
    public TurnOrder(String nickname, String[] order) {
        super("The game turn order is", nickname);
        this.order = order;
    }
}
