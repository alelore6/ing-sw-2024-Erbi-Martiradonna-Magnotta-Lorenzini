package it.polimi.ingsw.model;

public class WrongPlayException extends Throwable{
    Player player;
    String message;
    Card card;
    WrongPlayException(Player player,int x,int y, Card card){
        this.player=player;
        this.card=card;
        if (x==-1 && y==-1){
            message= "Error: the card cant be found in the hand of the player";
        } else if (x==-2 && y==-2) {
            message= "Error: the card doesnt overlap any corner";
        } else if (x==-3 && y==-3) {
            message="Error: the player doesnt have enough resources to play that card";
        } else message= "Error: the card cant be played in the position: "+x+","+y;
    }
}
