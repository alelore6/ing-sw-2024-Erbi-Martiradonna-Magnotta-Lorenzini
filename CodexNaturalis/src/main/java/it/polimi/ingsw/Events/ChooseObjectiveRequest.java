package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.ObjectiveCard;

public class ChooseObjectiveRequest extends GenericEvent {

    public ChooseObjectiveRequest(ObjectiveCard objCard1, ObjectiveCard objCard2,String nickname){
        super("Choose your secret objective card from these two:\n"
                + "insert (1) for card number " + objCard1.getID()
                + "or insert (2) for card number " + objCard2.getID(), nickname);
    }

    public String msgOutput() {
        return message;
    }
}
