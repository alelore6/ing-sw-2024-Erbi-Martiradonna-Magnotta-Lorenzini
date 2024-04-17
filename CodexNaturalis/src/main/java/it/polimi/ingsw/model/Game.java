package it.polimi.ingsw.model;

public class Game {

    private final int numPlayers;
    private int turnCounter;
    private boolean isFinished;
    private int remainingTurns;

    private int curPlayerPosition;  //TODO ORA player ha l'attributo position quindi FIXARE TUTTO DI
                                    // CONSEGUENZA (ENDGAME per il calcolo turni, nextplayer ecc..)

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

    public void startGame(int numPlayers) throws RuntimeException, WrongPlayException{

        tablecenter = new TableCenter(new ResourceDeck(), new GoldDeck(), new ObjectiveDeck(), this);

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

            try {
                p.getHand().DrawFromDeck(tablecenter.getResDeck());
                p.getHand().DrawFromDeck(tablecenter.getResDeck());   //RIEMPIO LA MANO DEL GIOCATORE 2 carte res e 1 gold
                p.getHand().DrawFromDeck(tablecenter.getGoldDeck());
            } catch(isEmptyException | HandFullException e){
                //should not happen
                throw new RuntimeException(e);
            }
            p.chooseObjective(tablecenter.getObjDeck().draw(), tablecenter.getObjDeck().draw());
            //ogni giocatore riceve 2 carte obiettivo, le guarda e ne sceglie 1, quello sarà il suo obiettivo segreto


        }

        //DECISIONE RANDOMICA PRIMO GIOCATORE, genero int da 0 a numplayer
        int firstPlayerPos = (int) (Math.random()*numPlayers);
        players[firstPlayerPos].position = 0;  //setto il player come primo

        int j = firstPlayerPos;
        for(int i = 0; i < numPlayers; i++){  //loop per settare la posizione in senso orario (da sinistra a destra) di tutti i player
            if (j >= numPlayers) {j = 0;}
            players[j].position = i;
            j++;
        }

        curPlayerPosition = firstPlayerPos;
        nextPlayer(players[firstPlayerPos]); //INIZIO IL GIOCO CHIAMANDO IL METODO NEXTPLAYER SUL PRIMO GIOCATORE

    }
    public void endGame(){

        remainingTurns = numPlayers + (players[curPlayerPosition].position); //calcolo turni rimanenti
        for(int i = 0; i < remainingTurns; i++){
            nextPlayer(players[curPlayerPosition]);
        }
        isFinished = true;
        checkWinner();
    }

    public Player checkWinner(){ //TODO possibilità di ritornare più player (array?)
        //TODO ogni giocatore conta i punti extra delle carte obiettivo (comuni + obiettivo segreto)
        // e li aggiunge al segnapunti. MAX 29 PUNTI. in caso di parità vince chi ha fatto piu' carte obiettivo.
        // se parità persiste allora i giocatori condividono la vittoria!

        Player winner = null;
        int punteggi[] = new int[numPlayers];

        for(int i = 0; i < numPlayers; i++){ //ciclo per iterare su ogni player. calcolo punti per ogni player
            punteggi[i] = players[i].getToken().getScoreTrackPos();

            if(players[i].getObjective() instanceof  ObjectiveCard1){ //calcolo punti a seconda del tipo di obj card

            }
            else if(players[i].getObjective() instanceof  ObjectiveCard2){

            }
            else{ // ObjectiveCard3

            }
        }

        int maxpoints = 0;
        for(int i = 0; i < numPlayers; i++)  //ciclo e calcolo vincitore
        {
            if(punteggi[i] > maxpoints){
                winner = players[i];
                maxpoints = punteggi[i];
            }

        }
        return winner;
    }

    public void nextPlayer(Player PreviousPlayer){
        int nextPlayerIndex;
        for(nextPlayerIndex = 0; nextPlayerIndex< numPlayers; nextPlayerIndex++){  //trovo l'indice del giocatore prossimo
            if(players[nextPlayerIndex] == PreviousPlayer){break;}
        }

        if(nextPlayerIndex == numPlayers-1){ nextPlayerIndex = 0;}
        else{nextPlayerIndex++;}

        curPlayerPosition = nextPlayerIndex;
        // players[nextPlayerIndex].getHand().DrawFromDeck();
        // players[nextPlayerIndex].getHand().DrawPositionedCard();
        //TODO da input utente serve sapere dove deve pescare. forse serve un metodo Draw generico contentente i due tipi di draw?
        //TODO stessa cosa per play card

        //nextPlayer(players[nextPlayerIndex]);
        //Sarebbe da chiamare il metodo nextplayer ma sta al controllore farlo perché sennò diventa ricorsivo credo
    }
}
