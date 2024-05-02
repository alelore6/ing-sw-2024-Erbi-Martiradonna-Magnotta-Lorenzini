package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.ObjectiveCard;

public class ChooseObjectiveResponse extends GenericEvent{

    public final ObjectiveCard objectiveCard;

    public ChooseObjectiveResponse(ObjectiveCard objectiveCard, String nickname) {
        super("Objective card chosen!",nickname);
        this.objectiveCard = objectiveCard;
    }
}
