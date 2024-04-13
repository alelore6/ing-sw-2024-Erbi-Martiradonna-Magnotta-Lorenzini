package it.polimi.ingsw.model;

public class Game {

    private final int numPlayers;
    private int turnCounter;
    private boolean isFinished;
    private int remainingTurns;

    private int curPlayerPosition;

    private StartingDeck StartingDeck;
    Player[] players; // domanda: ESISTONO PRIMA O DOPO LA CREAZIONE DEL GIOCO? PER COMODITà IN TEORIA PRIMA.
    TableCenter tablecenter;


    Game(int numPlayers, String[] nicknames)
    {
        this.numPlayers = numPlayers;
        this.turnCounter = 0;
        this.isFinished = false;
        this.remainingTurns = -1;
        players = new Player[numPlayers];
        for (int i=0;i<numPlayers;i++ ){
            players[i]= new Player(nicknames[i], this );
        }
        StartingDeck = new StartingDeck();
    }

    public TableCenter getTablecenter() {return tablecenter;}
    public Player[] getPlayers() {return players;}
    public int getNumPlayers() {return numPlayers;}
    public int getRemainingTurns() {return remainingTurns;}
    public int getTurnCounter() {return turnCounter;}

    public void startGame(int numPlayers) throws RuntimeException, WrongPlayException {

        tablecenter = new TableCenter(new ResourceDeck(), new GoldDeck(), new ObjectiveDeck());

        for(int i = 0; i < 4; i++){  //Riempio le carte a terra per la prima volta dai deck
            if(i < 2){
                try {
                    tablecenter.getCenterCards()[i] = tablecenter.getResDeck().draw();
                } catch (isEmptyException e) {
                    throw new RuntimeException(e);
                }
                tablecenter.getObjCards()[i] = tablecenter.getObjDeck().draw();
            }
            if(i >= 2){
                try {
                    tablecenter.getCenterCards()[i] = tablecenter.getGoldDeck().draw();
                } catch (isEmptyException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        for(Player p: players){
            p.PlaceStartingCard(StartingDeck.draw());
             //TODO SCELTA TOKEN DA PARTE DEL GIOCATORE TRAMITE INPUT (colore) e quindi poi istanzio nuova classe token


            p.getHand().DrawFromDeck(tablecenter.getResDeck(), 0 );
            p.getHand().DrawFromDeck(tablecenter.getResDeck(), 1 );   //RIEMPIO LA MANO DEL GIOCATORE 2 carte res e 1 gold
            p.getHand().DrawFromDeck(tablecenter.getGoldDeck(), 2 );
            p.chooseObjective(tablecenter.getObjDeck().draw(), tablecenter.getObjDeck().draw());
            //ogni giocatore riceve 2 carte obiettivo, le guarda e ne sceglie 1, quello sarà il suo obiettivo segreto


        }

        //DECISIONE RANDOMICA PRIMO GIOCATORE, genero int da 0 a numplayer
        int firstPlayerPos = (int) (Math.random()*numPlayers);
        players[firstPlayerPos].setisFirst();  //setto il player come primo

        if(players[0] != players[firstPlayerPos]){ //swap

        }
        curPlayerPosition = 0;
    }
    public void endGame(){
        //TODO da implementare logica

        isFinished = true;
    }
    public Player checkWinner(){
        //TODO ogni giocatore conta i punti extra delle carte obiettivo (comuni + obiettivo segreto)
        // e li aggiunge al segnapunti. MAX 29 PUNTI. in caso di parità vince chi ha fatto piu' carte obiettivo. se parità persiste allora i giocatori condividono la vittoria!
        Player winner = null;
        int punteggi[] = new int[numPlayers];

        for(int i = 0; i < numPlayers; i++){

        }
        return winner;
    }

    public void nextPlayer(Player p){
        //Iterate through array of players calling all players methods such as draw,
        int nextPlayerPos = 0;
        int i;

        for(i = 0; i < numPlayers; i++){
            if(p == players[i]){break;}
        }
    }


}
