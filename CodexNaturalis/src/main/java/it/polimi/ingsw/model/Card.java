package it.polimi.ingsw.model;

public abstract class Card {
     protected int ID;

    // Everything about the back of the card is based on this boolean:
     public boolean isFacedown;
     public Corner[] corners;

     protected Card(int ID) {
        this.ID = ID;
    }

    // Everything is stored and the game uses a side of the card based ONLY on this
     public abstract void flip();
     public abstract Card getCard();
     public int getID(){
          return this.ID;
     }
}
