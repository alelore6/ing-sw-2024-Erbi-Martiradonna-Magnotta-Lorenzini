package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;


public class Game {

    private int numPlayers;
    private int turnCounter;
    private boolean isFinished;
    private int remainingTurns;
    Player[] players;
    TableCenter tablecenter;


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

    public void startGame(int numPlayers) throws isEmptyException {

        tablecenter = new TableCenter(new ResourceDeck(), new GoldDeck(), new ObjectiveDeck()); //TODO FIXARE object

        for(int i = 0; i < 4; i++){  //Riempio le carte a terra per la prima volta dai deck
            if(i < 2){
                try {
                    tablecenter.getCenterCards()[i] = tablecenter.getResDeck().draw();
                } catch (isEmptyException e) {
                    throw new RuntimeException(e);
                }
                tablecenter.getObjCards()[i] = tablecenter.getObjDeck().draw();
            }
            if(i > 2){
                try {
                    tablecenter.getCenterCards()[i] = tablecenter.getGoldDeck().draw();
                } catch (isEmptyException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        for(Player p: players){
            //TODO DISTRIBUIRE STARTING CARD DA STARTING DECK
             //TODO SCELTA TOKEN DA PARTE DEL GIOCATORE TRAMITE INPUT (colore) e quindi poi instanzio nuova classe token
            p.getHand().DrawFromDeck(tablecenter.getResDeck(), 0 );
            p.getHand().DrawFromDeck(tablecenter.getResDeck(), 1 );   //RIEMPIO LA MANO DEL GIOCATORE 2 carte res e 1 gold
            p.getHand().DrawFromDeck(tablecenter.getGoldDeck(), 2 );
            p.chooseObjective(tablecenter.getObjDeck().draw(), tablecenter.getObjDeck().draw());
            //ogni giocatore riceve 2 carte obiettivo, le guarda e ne sceglie 1, quello sarà il suo obiettivo segreto


        }

        //DECISIONE RANDOMICA PRIMO GIOCATORE, genero int da 0 a numplayer
        int firstPlayerPos = (int) (Math.random()*numPlayers);
        players[firstPlayerPos].setisFirst();  //setto il player come primo
    }
    public void endGame(){
        //TODO da implementare logica
        isFinished = true;
    }
    public Player checkWinner(){
        //TODO ogni giocatore conta i punti extra delle carte obiettivo (comuni + obiettivo segreto)
        // e li aggiunge al segnapunti. MAX 29 PUNTI. in caso di parità vince chi ha fatto piu' carte obiettivo. se parità persiste allora i giocatori condividono la vittoria!
        Player winner;
        int punteggi[] = new int[numPlayers];

        for(int i = 0; i < numPlayers){

        }
    }

    public void nextPlayer(){

    }


}
