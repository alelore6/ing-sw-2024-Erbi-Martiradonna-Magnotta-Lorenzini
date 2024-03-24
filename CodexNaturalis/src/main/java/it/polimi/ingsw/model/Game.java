package main.java.it.polimi.ingsw.model;

import jdk.javadoc.internal.doclets.formats.html.Table;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.tools.jconsole.Tab;

public class Game {

    //TODO SOSTITUIRE OBJECTS CON OGGETTI VERI
    private int numPlayers;
    private int turnCounter;
    private boolean isFinished;
    private int remainingTurns;
    Player[] players;
    TableCenter tablecenter;

    //per inizializzare il gioco con startgame ho bisogno come attributi qui da passare
    //i vari deck (?)

    Game(int numPlayers, int turnCounter, boolean isFinished, int remainingTurns)
    {
        this.numPlayers = numPlayers;
        this.turnCounter = turnCounter;
        this.isFinished = isFinished;
        this.remainingTurns = remainingTurns;
        players = new Player[numPlayers];
    }

    public TableCenter getTablecenter() {return tablecenter;}
    public Player[] getPlayers() {return players;}
    public int getNumPlayers() {return numPlayers;}
    public int getRemainingTurns() {return remainingTurns;}
    public int getTurnCounter() {return turnCounter;}

    public void startGame(int numPlayers){

        tablecenter = new TableCenter(new Object(), new Object(), new Object()); //TODO FIXARE
    }
    public void endGame(){

    }
    public Player checkWinner(){
        throw new NotImplementedException();
    }

    public void nextPlayer(){

    }


}
