package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.ObjectiveCard;

public class ChooseObjectiveRequest extends GenericEvent {
    public final ObjectiveCard objCard1;
    public final ObjectiveCard objCard2;

    public ChooseObjectiveRequest(ObjectiveCard objCard1, ObjectiveCard objCard2,String nickname){
        super("Choose your secret objective card: ",nickname);
        this.objCard1 = objCard1;
        this.objCard2 = objCard2;
    }

}
