package it.polimi.ingsw.Model;

public abstract class Card {
     protected int ID;

    // Everything about the back of the card is based on this boolean:
     public boolean isFacedown;
     protected Corner[] corners;

     protected Card(int ID) {
        this.ID = ID;
        isFacedown = false;
    }

     public abstract Card getCard();
     public int getID(){
          return this.ID;
     }
     public Corner[] getCorners(){
         return this.corners;
     }
}
