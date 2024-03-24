package it.polimi.ingsw.model;

public abstract class Card {

     final int ID;
     public boolean isFacedown;
     //% gestire il retro delle carte (magari con altri corner dichiarati e importati dal DB)
     public Corner[] corners = new Corner[4];

     protected Card(int ID) {
        this.ID = ID;
    }

     public abstract void flip();
     public abstract Card getCard();
     public int getID(){
          return this.ID;
     }
}
