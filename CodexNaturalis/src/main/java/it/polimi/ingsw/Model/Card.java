package it.polimi.ingsw.Model;

import java.io.Serializable;

public abstract class Card implements Serializable {
     protected int ID;

    // Everything about the back of the card is based on this boolean:
     public boolean isFacedown = false;
     protected Corner[] frontCorners;
     protected Corner[] backCorners;

     public Card getCard(){
         return this;
     }
     public int getID(){
          return this.ID;
     }
     public Corner[] getFrontCorners(){
         return frontCorners;
     }
     public Corner[] getBackCorners(){return backCorners;}
}
