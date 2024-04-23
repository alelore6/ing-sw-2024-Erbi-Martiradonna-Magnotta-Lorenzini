package it.polimi.ingsw.model;
/**
 * Class that manages the game life cycle, from the start to end.
 */
public class Game {
    /**
     * number of players in the current game
     */
    private final int numPlayers;
    /**
     * attribute that keeps count of the number of turns completed since the beginning
     */
    private int turnCounter;
    /**
     * boolean that states if the game is either finished or still in act
     */
    private boolean isFinished;
    /**
     * attribute that keeps count of remaining turns when the ending stage of the game is triggered
     */
    private int remainingTurns;
    /**
     * integer representing the current player position in the array of players
     */
    private int curPlayerPosition;

    /**
     * The starting cards deck
     */
    private StartingDeck StartingDeck;
    /**
     * The dynamic array containing the players of this current game
     */
    Player[] players;
    /**
     * The tablecenter attribute containing the two decks (resource and gold) and aswell the cards
     * layed on it (2 res, 2 gold, 2 obj)
     */
    TableCenter tablecenter;

    /**
     * Constructor: initializes the Game class, creating the players, turnCounter, remainingTurns, isFinished and
     * creating the startingDeck instance aswell.
     * @param numPlayers number of players in the current game
     * @param nicknames array of nicknames passed by user, used to create the players classes
     */
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

    /**
     * Getter for tablecenter instance
     * @return Tablecenter istance
     */
    public TableCenter getTablecenter() {return tablecenter;}
    /**
     * Getter for array of players
     * @return array of players
     */
    public Player[] getPlayers() {return players;}
    /**
     * Getter for number of players
     * @return number of players
     */
    public int getNumPlayers() {return numPlayers;}
    /**
     * Getter for remainingTurns attribute
     * @return remainingTurns
     */
    public int getRemainingTurns() {return remainingTurns;}
    /**
     * Getter for turnCounter attribute
     * @return turnCounter
     */
    public int getTurnCounter() {return turnCounter;}

    /**
     * After the game has been initialized, the method starts it, laying all the cards on the table
     * and filling each player's hand, making them also choose the objective card between the two given.
     * It also randomly chooses the first player and orders the other ones from left to right.
     * @param numPlayers number of players in this game
     * @throws RuntimeException if the decks are empty (should not happen at the beginning)
     * @throws WrongPlayException thrown by the method playStartingCard
     */
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
            p.placeStartingCard(StartingDeck.draw());
            TokenColor playercolor;
             //TODO il colore deve essere passato come input dal player!
            p.setToken(new Token(playercolor, tablecenter.getScoretrack(), p)); //set token

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

    /**
     * sets the parameter remainingTurns accordingly, displays a message on the user screen stating which
     * occasion triggered the endgame status notifying the controller aswell.
     * @param occasion states the case that triggered the endgame process such as:
     * "player X has reached 20 points" or
     * "both decks are empty"
     */
    public void endGame(int occasion){
        //0 : causato da player pos 0 che arriva a 20 pt
        //1: player 1 ...
        //2: player 2 ...
        //3: player 3 ...
        //4: entrambi i mazzi finiti
        //5: mazzo gold finito
        //6: mazzo risorsa finito
        remainingTurns = numPlayers + (players[curPlayerPosition].position); //calcolo turni rimanenti

        switch(occasion){
            case 0,1,2,3:
                System.out.println("Player " + occasion + " has reached 20 points. Starting endgame process");
            case 4:
                System.out.println("Zero cards left! Starting endgame process");
        }
        //TODO: notificare il controllore e la condizione su cui è stato chiamato

    }

    /**
     * //TODO
     * Calculates the total points of each player, adding the total points to the points acquired by the player's objective card
     * two different ways of calculating the points, one for each type of objective card.
     * The first one simply requires to check how many times the player has the required set of resources
     * The second one checks through matrix operations if the players respected a certain card pattern
     * @return winning player or players
     */
    public Player checkWinner(){ //TODO possibilità di ritornare più player (array?)
        //TODO ogni giocatore conta i punti extra delle carte obiettivo (comuni + obiettivo segreto)
        // e li aggiunge al segnapunti. MAX 29 PUNTI. in caso di parità vince chi ha fatto piu' carte obiettivo.
        // se parità persiste allora i giocatori condividono la vittoria!
        isFinished = true;

        Player winner = null;
        int[] punteggi = new int[numPlayers];

        for(int i = 0; i < numPlayers; i++){ //ciclo per iterare su ogni player. calcolo punti per ogni player
            punteggi[i] = players[i].getToken().getScoreTrackPos();
            int minPoints = 1000;

            if(players[i].getObjective() instanceof  ObjectiveCard2){ //calcolo punti a seconda del tipo di obj card
                for( Resource resource : ((ObjectiveCard2) players[i].getObjective()).getObjectivecard2Map().keySet()){
                    //controllo le risorse necessarie per i punti
                    int required = ((ObjectiveCard2) players[i].getObjective()).getObjectivecard2Map().get(resource);
                    //in pratica controllo per ogni risorsa nelle currentersources quante volte ne ha per i requisiti della carta
                    //e prendendo il minimo di ogni risorsa sono sicuro di prendere il massimo numero  di punti che il giocatore
                    //avrà totalizzato
                    if(players[i].getCurrentResources().currentResources.get(resource)/ required < minPoints) {
                        minPoints = players[i].getCurrentResources().currentResources.get(resource)/required;
                    }
                    punteggi[i]= minPoints + players[i].getToken().getScoreTrackPos(); //aggiungo il punteggio all'array posizionalmente

                }



            }
            else{// ObjectiveCard1
                //Serve scannerizzare l'intera matrice del player 81x81 in sottomatrici 3x3 e se trovo la pattern indicata
                //allora setto un attributo found sulla carta ad 1. SE l'attributo found è a 1 sulle carte trovate (ne basta una)
                //allora i punti NON SARANNO VALIDI!!! 0 punti.
                int rows = 81;
                int columns = 81;
                for(int k = 0; k < rows - 3; k++){
                    for(int j = 0; j < columns-3 ; j++){
                        //TODO implementare

                    }
                }


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

    /**
     * Calculates the index of the next player cycling through the players array and sets it to the current player index,
     * increments turnCounter and calls checkWinner method if remainingTurns is 0.
     * If remainingTurns are greater than zero, nextPlayer calls itself with the current player passed by parameter
     * @param PreviousPlayer the instance of the player holding the previous turn
     */
    public void nextPlayer(Player PreviousPlayer){
        //find next player index
        int nextPlayerIndex;
        for(nextPlayerIndex = 0; nextPlayerIndex< numPlayers; nextPlayerIndex++){
            if(players[nextPlayerIndex] == PreviousPlayer){break;}
        }
        if(nextPlayerIndex == numPlayers-1){ nextPlayerIndex = 0;}
        else{nextPlayerIndex++;}


        curPlayerPosition = nextPlayerIndex;

        //TODO da input utente serve sapere dove deve pescare. forse serve un metodo Draw generico contentente i due tipi di draw?

        turnCounter++;

        if(remainingTurns == 0) checkWinner();

        else nextPlayer(players[nextPlayerIndex]);


    }
}
