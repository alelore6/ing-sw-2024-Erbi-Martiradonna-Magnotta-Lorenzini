package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.ObjectiveCard;

/**
 * event that represent the model request to choose between 2 objective card.
 */
public class ChooseObjectiveRequest extends GenericRequest {
    /**
     * the first objective card
     */
    public  final ObjectiveCard objCard1;
    /**
     * the second objective card
     */
    public  final ObjectiveCard objCard2;

    /**
     * Constructor
     * @param objCard1 the first objective card
     * @param objCard2 the second objective card
     * @param nickname the receiver of the event
     */
    public ChooseObjectiveRequest(ObjectiveCard objCard1, ObjectiveCard objCard2, String nickname){
        super("Choose your secret objective card from these two:\n", nickname);
        this.objCard1 = objCard1;
        this.objCard2 = objCard2;
    }

    @Override
    public String msgOutput() {
        return super.msgOutput() + "insert (1) for card number " + objCard1.getID()
                                 + " or insert (2) for card number " + objCard2.getID() + ": ";
    }

    public ObjectiveCard getChosenCard(int n){
        if(n == 1)  return objCard1;
        if(n == 2)  return objCard2;
        else        return     null;
    }
}
