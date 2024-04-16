package it.polimi.ingsw.model;

import java.util.Optional;

public abstract class Card {

     final int ID;
     public boolean isFacedown;
     public Corner[] corners;

     protected Card(int ID) {
        this.ID = ID;
    }

    // everything about the back of the card is based on this boolean:
    // everything is stored and the game uses a side of the card based ONLY on this
     public abstract void flip();
     public abstract Card getCard();
     public int getID(){
          return this.ID;
     }
}
