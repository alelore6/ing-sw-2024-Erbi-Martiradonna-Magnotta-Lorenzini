package it.polimi.ingsw.Events;

public class CardInfoRequest extends GenericEvent{

    public CardInfoRequest(int ID, String nickname) {
        super(null, nickname);

        // TODO: send this request to Controller
    }
}
