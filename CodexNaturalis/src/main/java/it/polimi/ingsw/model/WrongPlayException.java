package it.polimi.ingsw.model;

public class WrongPlayException extends Throwable{
    Player player;
    int x;
    int y;
    Card card;
    WrongPlayException(Player player,int x,int y, Card card){
        this.player=player;
        this.x=x;
        this.y=y;
        this.card=card;
    }
}
