package it.polimi.ingsw.Events;

import it.polimi.ingsw.model.ObjectiveCard;

/**
 * Event that represent the response of the client to a choose objective request.
 */
public class ChooseObjectiveResponse extends GenericEvent{
    /**
     * the objective card chosen
     */
    public final ObjectiveCard objectiveCard;

    /**
     * Constructor
     * @param objectiveCard the objective card chosen
     * @param nickname the player that sends the response
     */
    public ChooseObjectiveResponse(ObjectiveCard objectiveCard, String nickname) {
        super("Objective card chosen!",nickname);
        this.objectiveCard = objectiveCard;
    }
}
