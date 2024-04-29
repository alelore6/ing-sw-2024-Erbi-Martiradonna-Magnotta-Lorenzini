package it.polimi.ingsw.Events;

public class EndGameTriggered extends GenericEvent{
    private final int occasion;

    public EndGameTriggered(int occasion) {
        this.occasion = occasion;
        message="End Game has been triggered";
    }
}
