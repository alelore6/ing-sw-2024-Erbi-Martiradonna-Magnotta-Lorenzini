package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.ObjectiveCard;

public class ChooseObjective extends GenericEvent {
    private final ObjectiveCard objCard1;
    private final ObjectiveCard objCard2;
    private int choice;

    public ChooseObjective(ObjectiveCard objCard1, ObjectiveCard objCard2){
        message="Choose your secret objective card: ";
        this.objCard1 = objCard1;
        this.objCard2 = objCard2;
    }

    public void setChoice(int choice){
        this.choice=choice;
    }

    public ObjectiveCard getChosenObjCard() {
        if(choice==1)
            return objCard1;
        else if(choice==2)
            return objCard2;
        else throw new RuntimeException("Error in choose objective event: choice hasn't been set");
    }
}
