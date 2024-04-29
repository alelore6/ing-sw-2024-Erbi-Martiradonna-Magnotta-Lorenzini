package it.polimi.ingsw.model;

import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.WrongPlayException;
import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;

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
     * The tablecenter attribute containing the two decks (resource and gold) and the cards on it (2 res, 2 gold, 2 obj)
     */
    TableCenter tablecenter;

    private final ModelViewListener[] mvListeners;

    /**
     * Constructor: initializes the Game class, creating the players, turnCounter, remainingTurns, isFinished and
     * creating the startingDeck instance as well.
     * @param numPlayers number of players in the current game
     * @param nicknames array of nicknames passed by user, used to create the players classes
     */
    public Game(int numPlayers, String[] nicknames) {
        this.mvListeners = new ModelViewListener[numPlayers];

        // TODO: fare riferimento a numPLayers in Lobby e non usare quest'attributo
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

    public void addListener(ModelViewListener listener, String nickname) {
        for (int i=0;i<numPlayers;i++ ){
            if (players[i].getNickname().equals(nickname)){
                mvListeners[i] = listener;
            }
        }
    }

    private void notify(Generic e, ModelViewListener listener){
        listener.handleModelMessage(e);
    }

    private void notifyAll(Generic e){
        for (int i=0;i<numPlayers;i++ ){
            mvListeners[i].handleModelMessage(e);
        }
    }

    /**
     * Getter for tablecenter instance
     * @return Tablecenter instance
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

    public String getCurrentPlayer(){
        return players[curPlayerPosition].getNickname();
    }

    /**
     * After the game has been initialized, the method starts it, laying all the cards on the table
     * and filling each player's hand, making them also choose the objective card between the two given.
     * It also randomly chooses the first player and orders the other ones from left to right.
     * @throws RuntimeException if the decks are empty (should not happen at the beginning)
     * @throws WrongPlayException thrown by the method playStartingCard
     */
    public void startGame() throws RuntimeException{

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
            try {
                p.placeStartingCard(StartingDeck.draw());
            } catch (WrongPlayException e) {
                throw new RuntimeException(e);
            } catch (isEmptyException e) {
                throw new RuntimeException(e);
            }
            TokenColor playercolor = null;
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
     * occasion triggered the endgame status notifying the Controller as well.
     * @param occasion states the case that triggered the endgame process such as:
     * "player X has reached 20 points" or
     * "both decks are empty"
     */
    public void endGame(int occasion){
        switch(occasion){
            // a player reached 20 points
            case 0,1,2,3:
                // un giro + i turni rimanenti per completare questo
                remainingTurns = numPlayers + (numPlayers-players[curPlayerPosition].position); //calcolo turni rimanenti
                System.out.println("Player " + occasion + " has reached 20 points. Starting endgame process");
            //both decks are found empty simultaneously
            case 4:
                tablecenter.getGoldDeck().AckEmpty=true;
                tablecenter.getResDeck().AckEmpty=true;
                remainingTurns = numPlayers + (numPlayers-players[curPlayerPosition].position); //calcolo turni rimanenti
                System.out.println("Zero cards left! Starting endgame process");
            //gold deck is found empty
            case 5:
                // if i already had this information do nothing
                if(tablecenter.getGoldDeck().AckEmpty)
                    break;
                //else set AckEmpty to true
                tablecenter.getGoldDeck().AckEmpty=true;
                //check if resource deck is known empty
                if(tablecenter.getResDeck().AckEmpty){
                    //both decks are empty: same as case 4
                    remainingTurns = numPlayers + (numPlayers-players[curPlayerPosition].position);
                    System.out.println("Zero cards left! Starting endgame process");
                }
            //resource deck is found empty
            case 6:
                //same as case 5 but decks are inverted
                if( tablecenter.getResDeck().AckEmpty){
                    break;
                }
                tablecenter.getResDeck().AckEmpty=true;
                if (tablecenter.getGoldDeck().AckEmpty){
                    remainingTurns = numPlayers + (numPlayers-players[curPlayerPosition].position);
                    System.out.println("Zero cards left! Starting endgame process");
                }
        }
        //TODO: notificare il controllore e la condizione su cui è stato chiamato

    }

    /**
     * Calculates the total points of each player, adding the total points to the points acquired by the player's objective card
     * two different ways of calculating the points, one for each type of objective card.
     * The first one simply requires to check how many times the player has the required set of resources
     * The second one checks through matrix operations if the players respected a certain card pattern
     * @return winning player or players
     */
    public Player[] checkWinner(){
        // possibilità di ritornare più player, array con dimensione generata dinamicamente
        // a seconda di quanti player hanno lo score più alto e uguale

        isFinished = true;


        int[] punteggi = new int[numPlayers];

        for(int i = 0; i < numPlayers; i++) { //inizializzazione array punteggi tutti a 0
            punteggi[i] = 0;
        }

        for(int i = 0; i < numPlayers; i++){ //ciclo per iterare su ogni player. calcolo punti per ogni player
            punteggi[i] = players[i].getToken().getScoreTrackPos();


            punteggi[i] += checkObjectivePoints(getTablecenter().getObjCards()[0], i);
            punteggi[i] += checkObjectivePoints(getTablecenter().getObjCards()[1], i);
            punteggi[i] += checkObjectivePoints(players[i].getObjective(), i);

            }


        int maxpoints = 0;
        for(int i = 0; i < numPlayers; i++)  //ciclo e calcolo vincitore
        {
            if(punteggi[i] >= maxpoints){
                maxpoints = punteggi[i];
            }

        }
        int pareggi = 0;

        for(int i = 0; i < numPlayers; i++)  //ciclo e calcolo il numero di pareggi
        {
            if(punteggi[i] == maxpoints){
                pareggi++;
            }

        }
        Player[] winners = new Player[pareggi];

        int j = 0;
        for(int i = 0; i < numPlayers; i++){

            if(punteggi[i]==maxpoints){
                winners[j] = players[i];
                j++;
            }
        }

        return winners;
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

        // TODO play card from input
        //if both deck are not empty, a draw will be requested
        if (!tablecenter.getResDeck().AckEmpty && !tablecenter.getGoldDeck().AckEmpty){
            //TODO draw card from input
        }

        turnCounter++;
        remainingTurns--;
        if(remainingTurns == 0) checkWinner();

        else nextPlayer(players[nextPlayerIndex]);


    }

    protected static Card[][] getSubmatrix(Card[][] matrix, int row, int col) {
        Card[][] submatrix = new Card[3][3];
        for (int i = row, x = 0; i < row + 3; i++, x++) {
            for (int j = col, y = 0; j < col + 3; j++, y++) {
                submatrix[x][y] = matrix[i][j];
            }
        }
        return submatrix;
    }

    protected int checkObjectivePoints(ObjectiveCard objectiveCard, int playerPos) {


        if (objectiveCard instanceof ObjectiveCard2) {
            int minPoints = 1000;
            //calcolo punti a seconda del tipo di obj card
            for (Resource resource : ((ObjectiveCard2) objectiveCard).getObjectivecard2Map().keySet()) {
                //controllo le risorse necessarie per i punti
                int required = ((ObjectiveCard2) objectiveCard).getObjectivecard2Map().get(resource);
                //in pratica controllo per ogni risorsa nelle currentersources quante volte ne ha per i requisiti della carta
                //e prendendo il minimo di ogni risorsa sono sicuro di prendere il massimo numero  di punti che il giocatore
                //avrà totalizzato
                if (players[playerPos].getCurrentResources().currentResources.get(resource) / required < minPoints) {
                    minPoints = players[playerPos].getCurrentResources().currentResources.get(resource) / required;
                }


            }
            return minPoints; //aggiungo il punteggio all'array posizionalmente


        } else {// ObjectiveCard1
            //Serve scannerizzare l'intera matrice del player 81x81 in sottomatrici 3x3 e se trovo la pattern indicata
            //allora setto un attributo found sulla carta ad 1. SE l'attributo found è a 1 sulle carte trovate (ne basta una)
            //allora i punti NON SARANNO VALIDI!!! 0 punti.
            int totalpoints = 0;
            int rows = 81;
            int columns = 81;
            for (int k = 0; k < rows - 3; k++) {
                for (int j = 0; j < columns - 3; j++) {
                    //get the 3x3 submatrix needed to perform operations on (checking obj cards requisites)
                    Card[][] subMatrix = getSubmatrix(players[playerPos].getHand().getDisplayedCards(), k, j);
                    boolean found = true;
                    Card[] savedCards = new Card[3];
                    //PER OGNI 3X3 MATRICE SVOLGO
                        for (int index = 0; index < 3; index++) {
                            int x = 0;
                            int y = 0;
                            //switch case to translate position into matrix position[][]
                            switch (((ObjectiveCard1) objectiveCard).getRequiredPositions()[index]) {
                                case 1:
                                    x = 0;
                                    y = 0;
                                case 2:
                                    x = 0;
                                    y = 1;
                                case 3:
                                    x = 0;
                                    y = 2;
                                case 4:
                                    x = 1;
                                    y = 0;
                                case 5:
                                    x = 1;
                                    y = 1;
                                case 6:
                                    x = 1;
                                    y = 2;
                                case 7:
                                    x = 2;
                                    y = 0;
                                case 8:
                                    x = 2;
                                    y = 1;
                                case 9:
                                    x = 2;
                                    y = 2;
                            }

                            if (subMatrix[x][y] == null || subMatrix[x][y] instanceof StartingCard || ((PlayableCard)subMatrix[x][y]).isChecked == 1) {
                                found = false;
                                break;
                            }

                            if (((PlayableCard) subMatrix[x][y]).getColor() == ((ObjectiveCard1) objectiveCard).getCardColors()[index]) {
                                savedCards[index] = subMatrix[x][y];
                            } else {
                                found = false;
                                break;
                            }


                        }

                        if(found){
                            for(int z = 0; z < 3; z++){
                                ((PlayableCard)savedCards[z]).isChecked = 1;

                            }
                            totalpoints += objectiveCard.points;

                        }

                    }
                }
            for(int rowz = 0; rowz < 81; rowz++){
                for(int columnz = 0; columnz < 81; columnz++){
                    ((PlayableCard)players[playerPos].getHand().getDisplayedCards()[rowz][columnz]).isChecked = 0;
                }
            }

            return totalpoints;
        }
    }
}









