package it.polimi.ingsw.model;

public abstract class Card {

     final int ID;
     public boolean isFacedown;
     public Corner[] corners;

     protected Card(int ID) {
        this.ID = ID;
        isFacedown = false;

        // 0,4: UP_SX; 1,5: UP_DX; 2,6: DOWN_SX; 3,7: DOWN_DX (4,5,6,7 sono del retro)
        corners = new Corner[8];
        //% insert corner data from DB (if there's no corner, it's NULL)
    }

    // everything about the back of the card is based on this boolean:
    // everything is stored and the game uses a side of the card based ONLY on this
     public abstract void flip();
     public abstract Card getCard();
     public int getID(){
          return this.ID;
     }
}
