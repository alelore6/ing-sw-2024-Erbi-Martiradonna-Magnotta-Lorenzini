package it.polimi.ingsw.model;

import java.util.Collections;

public class StartingDeck {

    int NCards = 6;
    private StartingCard[] startcards;

    public StartingDeck() {
        startcards = new StartingCard[6];
        for (int i = 0; i < 6; i++) {
            startcards[i] = new StartingCard(6 + i);
        }

    }
    public StartingCard draw() throws isEmptyException {        //TODO: FIXARE ERRORI
        if (NCards == 0) {
            throw new isEmptyException(this);
        } else {
            StartingCard drawnCard = startcards[--NCards];
            return drawnCard;
        } //Not removing from array,
    }
        public void shuffle () {
            Collections.shuffle(startcards);
        }

    }
}