package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.Hand;
import it.polimi.ingsw.Model.PlayableCard;

import java.io.Serializable;

public class HandView implements Serializable {
    public final PlayableCard[] handCards;
    public final Card[][] playedCards;

    public HandView(Hand hand){
        this.handCards=hand.getHandCards();
        this.playedCards= hand.getDisplayedCards();
    }
}
