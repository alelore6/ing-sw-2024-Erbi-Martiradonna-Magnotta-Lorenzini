package it.polimi.ingsw.Model;

import java.io.Serializable;

public abstract class Card implements Serializable {
     protected int ID;

    // Everything about the back of the card is based on this boolean:
     public boolean isFacedown;
     protected Corner[] corners;

     protected Card(int ID) {
        this.ID = ID;
        isFacedown = false;
    }

     public Card getCard(){
         return this;
     }
     public int getID(){
          return this.ID;
     }
     public Corner[] getCorners(){
         return this.corners;
     }
}
