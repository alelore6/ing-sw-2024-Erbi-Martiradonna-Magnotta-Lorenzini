package it.polimi.ingsw.model;

import java.util.Collections;

public class ObjectiveDeck {
    int NCards = 16;
    private ObjectiveCard[] objcards;

    public ObjectiveDeck() {
        objcards = new ObjectiveCard[16];       //TODO: FIXARE ERRORI
        for (int i = 0; i < 16; i++) {
            objcards[i] = new ObjectiveCard(16 + i);
        }
        public ObjectiveCard draw () throws isEmptyException {
            if (NCards == 0) {
                throw new isEmptyException(this);
            } else {
                ObjectiveCard drawnCard = objcards[--NCards];
                return drawnCard;
            }
        }
        public ObjectiveCard draw () {
            return objcards;
        }
        public void shuffle () {
            Collections.shuffle(objcards);
        }
    }
}