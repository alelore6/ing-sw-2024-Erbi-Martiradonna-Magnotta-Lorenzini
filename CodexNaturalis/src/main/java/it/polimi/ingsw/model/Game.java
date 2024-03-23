package it.polimi.ingsw.model;

public class Game {

    private int numPlayers;
    private int turnCounter;
    private boolean isFinished;
    private int remainingTurns;
    Player[] players;
    Tablecenter tablecenter;

    Game(int numPlayers, int turnCounter, boolean isFinished, int remainingTurns)
    {
        this.numPlayers = numPlayers;
        this.turnCounter = turnCounter;
        this.isFinished = isFinished;
        this.remainingTurns = remainingTurns;
        tablecenter = new Tablecenter();
    }

    public Tablecenter getTablecenter() {return tablecenter;}
    public Player[] getPlayers() {return players;}
    public int getNumPlayers() {return numPlayers;}
    public int getRemainingTurns() {return remainingTurns;}
    public int getTurnCounter() {return turnCounter;}

    public void startGame(int numPlayers){

    }
    public void endGame(){

    }
    public Player checkWinner(){

    }

    public void nextPlayer(){

    }


}
