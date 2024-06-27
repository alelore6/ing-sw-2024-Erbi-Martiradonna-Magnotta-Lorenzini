package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.WrongPlayException;
import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.ModelView.GameView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that manages the game life cycle, from the start to end.
 */
public class Game{
    /**
     * attribute representing if threads of this class are running.
     */
    private volatile boolean running = true;
    /**
     * attribute that indicates if the turn of a disconnected player has been skipped.
     */
    private volatile boolean isTurnSkipped = false;
    /**
     * number of players in the current game
     */
    private volatile int numPlayers;
    /**
     * String that represents the turn order.
     */
    private String turnOrder = "";
    /**
     * attribute that keeps count of the number of turns completed since the beginning
     */
    private int turnCounter;
    /**
     * attribute representing the seconds to wait for another player to continue the game
     */
    public static final int timeoutOnePlayer = 60;
    /**
     * boolean that states if the game is either finished or still in act
     */
    public volatile boolean isFinished;
    /**
     * attribute that keeps count of remaining turns when the ending stage of the game is triggered
     */
    private int remainingTurns;
    /**
     * integer representing the current player position in the array of players
     */
     private int curPlayerPosition;

    public void setCurPlayerPosition(int curPlayerPosition) {
        this.curPlayerPosition = curPlayerPosition;
    }


    /**
     * The starting cards deck
     */
    private final StartingDeck StartingDeck;
    /**
     * The array containing the players of this current game
     */
    public ArrayList<Player> players;
    /**
     * The tablecenter attribute containing the two decks (resource and gold) and the cards on it (2 res, 2 gold, 2 obj)
     */
    public TableCenter tablecenter;

    private ArrayList<ModelViewListener> mvListeners;

    protected final ArrayList<TokenColor> availableTokens;
    /**
     * Attribute that counts how many players have completed a specific action.
     * Usually, the server waits for all the players using this.
     */
    public int waitNumClient = 0;

    public int turnPhase=0;// 0: start turn, 1: play done, 2: draw done
    public boolean isStarted = false;

    public boolean isTriggered = false; //to see if endgame is triggered

    public final Object lock = new Object();
    /**
     * Lock for waiting a certain amount of time when only one player is remaining.
     */
    public final Object OPLLock = new Object();

    /**
     * Constructor: initializes the Game class, creating the players, turnCounter, remainingTurns, isFinished and
     * creating the startingDeck instance as well.
     *
     * @param numPlayers number of players in the current game
     * @param nicknames  array of nicknames passed by user, used to create the players classes
     */
    public Game(int numPlayers, String[] nicknames, ArrayList<ModelViewListener> mvListeners) {
        this.mvListeners =  mvListeners;
        this.numPlayers = numPlayers;
        this.turnCounter = 0;
        this.isFinished = false;
        this.remainingTurns = -1;
        this.curPlayerPosition = -1;
        players = new ArrayList<>(numPlayers);
        for (int i=0;i<numPlayers;i++ ){
            players.add(new Player(nicknames[i], this));
        }
        tablecenter = new TableCenter(new ResourceDeck(), new GoldDeck(), new ObjectiveDeck(), this);
        StartingDeck = new StartingDeck();
        availableTokens = new ArrayList<TokenColor>();
        //all tokens available
        for(TokenColor c: TokenColor.values()){
            availableTokens.add(c);
        }
    }

    /**
     * Setter for the players' array.
     * @param players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Getter for tablecenter instance
     * @return Tablecenter instance
     */
    public TableCenter getTablecenter() {return tablecenter;}

    /**
     * Getter for array of players
     *
     * @return array of players
     */
    public ArrayList<Player> getPlayers() {return players;}

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

    public String getCurrentPlayerNickname(){
        return curPlayerPosition != -1 ? players.get(curPlayerPosition).getNickname() : null;
    }

    public int getCurPlayerPosition() {
        return curPlayerPosition;
    }

    /**
     * After the game has been initialized, the method starts it, laying all the cards on the table
     * and filling each player's hand, making them also choose the objective card between the two given.
     * It also randomly chooses the first player and orders the other ones from left to right.
     * In the end, it loops checking the number of remaining players.
     * @throws RuntimeException if the decks are empty (should not happen at the beginning)
     * @throws WrongPlayException thrown by the method playStartingCard
     */
    public void startGame() throws RuntimeException{

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

        //in un thread perchè si deve aspettare la sincronizzazione dal controller
        new Thread(){
            @Override
            public void run() {
                int pos = 0;
                for (Player p : players) {
                    SetTokenColorRequest setTokenColor = new SetTokenColorRequest(p.getNickname(), availableTokens);
                    getMVListenerByNickname(p.getNickname()).addEvent(setTokenColor);

                    //every player gets to choose between 2 objective cards
                    ChooseObjectiveRequest chooseObjective = new ChooseObjectiveRequest(tablecenter.getObjDeck().draw(), tablecenter.getObjDeck().draw(), p.getNickname());
                    getMVListenerByNickname(p.getNickname()).addEvent(chooseObjective);

                    //every place gets to place his starting card
                    StartingCard startingCard = null;
                    try {
                        startingCard = StartingDeck.draw();
                        getMVListenerByNickname(p.getNickname()).addEvent(new PlaceStartingCard(startingCard, p.getNickname()));
                    } catch (isEmptyException e) {
                        //shouldn't happen
                        throw new RuntimeException(e);
                    }

                    try {
                        p.getHand().DrawFromDeck(tablecenter.getResDeck());
                        p.getHand().DrawFromDeck(tablecenter.getResDeck());   //RIEMPIO LA MANO DEL GIOCATORE 2 carte res e 1 gold
                        p.getHand().DrawFromDeck(tablecenter.getGoldDeck());
                    } catch (isEmptyException | HandFullException e) {
                        //should not happen
                        throw new RuntimeException(e);
                    }


                    pos++;
                }

                while(true) {
                    //wait for everyone to complete the start
                    synchronized (lock){
                        if (waitNumClient == getActivePlayers()) break;
                    }
                }

                //dopo che ho inizializzato tutti
                String[] order= new String[numPlayers];
                //DECISIONE RANDOMICA PRIMO GIOCATORE, genero int da 0 a numplayer
                int firstPlayerPos = (int) (Math.random()*numPlayers);

                int j = firstPlayerPos;
                for(int i = 0; i < numPlayers; i++){
                    //loop per settare la posizione in senso orario (da sinistra a destra) di tutti i player
                    if (j >= numPlayers) {j = 0;}
                    players.get(j).position = i;
                    order[i]= players.get(j).getNickname();
                    j++;
                }


                for (int i=0; i<order.length;i++) {
                    int x=i+1;
                    turnOrder = turnOrder.concat("\n\t"+ x + ". " + order[i] + " ");
                }

                isStarted = true;

                turnPhase=-1;
                //notify all players on turn order
                TurnOrder event = new TurnOrder("every one", turnOrder, Game.this.clone());
                for(ModelViewListener modelViewListener : mvListeners) modelViewListener.addEvent(event);
                //devo trovare il giocatore precedente al primo!
                //perchè next player fa l'avanzamento
                int p= firstPlayerPos-1;
                if (p<0) p=numPlayers-1;
                nextPlayer(players.get(p));
                //INIZIO IL GIOCO CHIAMANDO IL METODO NEXTPLAYER SUL PRIMO GIOCATORE

                // Loop that checks if the active players are less than 2.
                while(running){
                    if     (getActivePlayers() == 1) OPLProcedure(true);
                    else if(getActivePlayers() == 0){
                        // If there's no player left.
                        System.exit(2);
                    }
                    if(isTurnSkipped){
                        isTurnSkipped = false;
                        if(getRemainingTurns() == 0)        checkWinner();
                        else if(getCurrentPlayerNickname() != null) nextPlayer(players.get(getCurPlayerPosition()));
                    }
                }
            }
        }.start();
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
                isTriggered = true;
                // a full round plus the turns remaining of this one
                remainingTurns = numPlayers + (numPlayers-(players.get(curPlayerPosition).position + 1)); //calcolo turni rimanenti
                String nickname = players.get(curPlayerPosition).getNickname();
                //notify all players
                for(int i=0;i<numPlayers;i++){
                    EndGameTriggered event=new EndGameTriggered(nickname + " has reached 20 points. Starting endgame process", players.get(i).getNickname(),clone());
                    if(!players.get(i).isDisconnected) getMVListenerByNickname(players.get(i).getNickname()).addEvent(event);
                }
                break;
            //both decks are found empty simultaneously
            case 4:
                isTriggered = true;
                tablecenter.getGoldDeck().AckEmpty=true;
                tablecenter.getResDeck().AckEmpty=true;
                remainingTurns = numPlayers + (numPlayers-curPlayerPosition); //calcolo turni rimanenti
                //notify all players
                for(int i=0;i<numPlayers;i++){
                    EndGameTriggered event=new EndGameTriggered("Zero cards left! Starting endgame process", players.get(i).getNickname(),clone());
                    getMVListenerByNickname(players.get(i).getNickname()).addEvent(event);
                }
                break;
            //gold deck is found empty
            case 5:
                // if I already had this information do nothing
                if(tablecenter.getGoldDeck().AckEmpty)
                    break;
                //else set AckEmpty to true
                tablecenter.getGoldDeck().AckEmpty=true;
                //if resource deck is known empty
                if(tablecenter.getResDeck().AckEmpty){
                    //both decks are empty: same as case 4
                    remainingTurns = numPlayers + (numPlayers-curPlayerPosition);
                    for(int i=0;i<numPlayers;i++){
                        EndGameTriggered event=new EndGameTriggered("Zero cards left! Starting endgame process", players.get(i).getNickname(),clone());
                        getMVListenerByNickname(players.get(i).getNickname()).addEvent(event);
                    }
                }
                break;
            //resource deck is found empty
            case 6:
                //same as case 5 but decks are inverted
                if( tablecenter.getResDeck().AckEmpty){
                    break;
                }
                tablecenter.getResDeck().AckEmpty=true;
                if (tablecenter.getGoldDeck().AckEmpty){
                    remainingTurns = numPlayers + (numPlayers-curPlayerPosition);
                    for(int i=0;i<numPlayers;i++){
                        EndGameTriggered event=new EndGameTriggered("Zero cards left! Starting endgame process", players.get(i).getNickname(),clone());
                        getMVListenerByNickname(players.get(i).getNickname()).addEvent(event);
                    }
                }
                break;

            case 7:
                isTriggered = true;
                remainingTurns = 0;
                for(int i = 0; i < numPlayers; i++){
                    FinalRankings event = new FinalRankings(players.get(i).getNickname(), null);
                    if(!players.get(i).isDisconnected){
                        ModelViewListener listener = getMVListenerByNickname(players.get(i).getNickname());
                        if(listener != null) listener.addEvent(event);
                    }
                }
                break;
        }

    }

    /**
     * Calculates the total points of each player, adding the total points to the points acquired by the player's objective card
     * two different ways of calculating the points, one for each type of objective card.
     * The first one simply requires to check how many times the player has the required set of resources
     * The second one checks through matrix operations if the players respected a certain card pattern
     * @return winning player or players
     */
    public void checkWinner(){

        HashMap<String,Integer> rankings= new HashMap<>();

        isFinished = true;

        int[] punteggi = new int[numPlayers];

        for(int i = 0; i < numPlayers; i++) { //inizializzazione array punteggi tutti a 0
            punteggi[i] = 0;
        }

        for(int i = 0; i < numPlayers; i++){ //ciclo per iterare su ogni player. calcolo punti per ogni player

            Integer punti = tablecenter.getScoretrack().getRankings().get(players.get(i).getNickname());
            if (punti!=null) {
                punteggi[i] = punti;
            }else{
                punteggi[i] = 0;
            }


            punteggi[i] += checkObjectivePoints(getTablecenter().getObjCards()[0], i);
            punteggi[i] += checkObjectivePoints(getTablecenter().getObjCards()[1], i);
            punteggi[i] += checkObjectivePoints(players.get(i).getObjective(), i);

        }

        for(int i = 0; i < numPlayers; i++){
            rankings.put(players.get(i).getNickname(), punteggi[i]);  //fill the rankings hashmap
        }


        //send FinalRankings event to everyone
        FinalRankings event = new FinalRankings("every one", rankings);

        for(int i = 0; i < numPlayers; i++){
            if(!players.get(i).isDisconnected)  getMVListenerByNickname(players.get(i).getNickname()).addEvent(event);
        }
    }

    /**
     * Calculates the index of the next player cycling through the players array and sets it to the current player index,
     * increments turnCounter and calls checkWinner method if remainingTurns is 0.
     * If remainingTurns are greater than zero, nextPlayer calls itself with the current player passed by parameter
     * @param PreviousPlayer the instance of the player holding the previous turn
     */
    public void nextPlayer(Player PreviousPlayer){
        if(isFinished){
            return;
        }
        //find next player index
        int nextPlayerIndex;
        for(nextPlayerIndex = 0; nextPlayerIndex< numPlayers; nextPlayerIndex++){
            if(players.get(nextPlayerIndex) == PreviousPlayer){break;}
        }
        if(nextPlayerIndex == numPlayers-1){ nextPlayerIndex = 0;}
        else{nextPlayerIndex++;}

        curPlayerPosition = nextPlayerIndex;

        //if player isn't disconnected
        if(!players.get(curPlayerPosition).isDisconnected) {
            //turn is starting
            turnPhase=0;

            //send StartTurn event
            StartTurn startTurn = new StartTurn(getCurrentPlayerNickname(), players.get(curPlayerPosition).getToken().getColor().toString(), clone());
            for(ModelViewListener modelViewListener : mvListeners) modelViewListener.addEvent(startTurn);

            // send play card request event
            PlayCardRequest playCard = new PlayCardRequest(getCurrentPlayerNickname(),clone());
            if(!players.get(curPlayerPosition).isDisconnected) getMVListenerByNickname(players.get(curPlayerPosition).getNickname()).addEvent(playCard);

            //check there are still card on table center
            boolean empty = true;
            for (int i = 0; i < 4; i++) {
                if (tablecenter.getCenterCards()[i] != null) {
                    empty = false;
                    break;
                }
            }
            //if both deck are not empty and !empty, a draw will be requested
            if (!tablecenter.getResDeck().AckEmpty || !tablecenter.getGoldDeck().AckEmpty || !empty) {
                DrawCardRequest drawCard = new DrawCardRequest(players.get(curPlayerPosition).getNickname(), clone(), tablecenter.getResDeck().getNCards(), tablecenter.getGoldDeck().getNCards());
                if(!players.get(curPlayerPosition).isDisconnected) getMVListenerByNickname(players.get(curPlayerPosition).getNickname()).addEvent(drawCard);
            }
            turnCounter++;
            remainingTurns--;
            //if (remainingTurns==0 ) checkWinner();
        }
        else{
            turnCounter++;
            remainingTurns--;
            if (remainingTurns==0 ) checkWinner();
            else nextPlayer(players.get(curPlayerPosition));
        }
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

        // credo sia al contrario
        if (objectiveCard instanceof ObjectiveCard2) {
            int minPoints = 1000;
            //calcolo punti a seconda del tipo di obj card
            for (Resource resource : ((ObjectiveCard2) objectiveCard).getReqMap().keySet()) {
                //controllo le risorse necessarie per i punti
                int required = ((ObjectiveCard2) objectiveCard).getReqMap().get(resource);
                //in pratica controllo per ogni risorsa nelle currentersources quante volte ne ha per i requisiti della carta
                //e prendendo il minimo di ogni risorsa sono sicuro di prendere il massimo numero  di punti che il giocatore
                //avrà totalizzato

                if (required != 0 && players.get(playerPos).getCurrentResources().currentResources!=null) {
                    if ((players.get(playerPos).getCurrentResources().currentResources.get(resource) / required) < minPoints) {
                        minPoints = players.get(playerPos).getCurrentResources().currentResources.get(resource) / required;
                    }
                }


            }
            return minPoints*objectiveCard.getPoints(); //aggiungo il punteggio all'array posizionalmente


        } else {// ObjectiveCard1
            //Serve scannerizzare l'intera matrice del player 81x81 in sottomatrici 3x3 e se trovo la pattern indicata
            //allora setto un attributo found sulla carta ad 1. SE l'attributo found è a 1 sulle carte trovate (ne basta una)
            //allora i punti NON SARANNO VALIDI!!! 0 punti.
            int totalpoints = 0;
            int rows = 81;
            int columns = 81;
            Card[] savedCards = new Card[3];
            for (int k = 0; k < rows - 2; k++) {
                for (int j = 0; j < columns - 2; j++) {
                    //get the 3x3 submatrix needed to perform operations on (checking obj cards requisites)
                    Card[][] subMatrix = getSubmatrix(players.get(playerPos).getHand().getDisplayedCards(), k, j);
                    boolean found = true;

                    int counter = 0;
                    //PER OGNI 3X3 SOTTOMATRICE SVOLGO
                        for (int index = 0; index < 3; index++) { //quindi 3 iterazioni (per le 3 carte)
                            int x = 0; //ROWS
                            int y = 0; //COLUMNS
                            //switch case to translate position into matrix position[][]
                            switch (((ObjectiveCard1) objectiveCard).getRequiredPositions()[index]) {
                                case 1: //posizioni partono da 1
                                    x = 0;
                                    y = 0;
                                    break;
                                case 2:
                                    x = 0;
                                    y = 1;
                                    break;
                                case 3:
                                    x = 0;
                                    y = 2;
                                    break;
                                case 4:
                                    x = 1;
                                    y = 0;
                                    break;
                                case 5:
                                    x = 1;
                                    y = 1;
                                    break;
                                case 6:
                                    x = 1;
                                    y = 2;
                                    break;
                                case 7:
                                    x = 2;
                                    y = 0;
                                    break;
                                case 8:
                                    x = 2;
                                    y = 1;
                                    break;
                                case 9:
                                    x = 2;
                                    y = 2;
                                    break;
                            }

                            if (subMatrix[x][y] == null || subMatrix[x][y] instanceof StartingCard || ((PlayableCard)subMatrix[x][y]).isChecked == 1) {
                                found = false;
                                break;
                            }

                            if (subMatrix[x][y] != null) {
                                if (subMatrix[x][y].getColor() == ((ObjectiveCard1) objectiveCard).getCardColors()[index]) { //se il colore corrisponde a quello della required in quella posizione
                                    savedCards[index] = subMatrix[x][y];
                                    counter++;

                                } else {
                                    found = false;
                                    break;
                                }
                            }


                        }
                        if(found && counter == 3){
                            for(int z = 0; z < 3; z++){
                                ((PlayableCard)savedCards[z]).isChecked = 1;

                            }
                            totalpoints += objectiveCard.points;

                        }
                    }
                }
            for(int rowz = 0; rowz < 81; rowz++){ //serve per resettare i checked a fine partita
                for(int columnz = 0; columnz < 81; columnz++){
                    if (!(players.get(playerPos).getHand().getDisplayedCards()[rowz][columnz] instanceof StartingCard) &&
                            players.get(playerPos).getHand().getDisplayedCards()[rowz][columnz]!= null ) {
                        ((PlayableCard) players.get(playerPos).getHand().getDisplayedCards()[rowz][columnz]).isChecked = 0;
                    }
                }
            }
            return totalpoints;
        }
    }

    /**
     * Method to clone the game.
     * @return the game cloned.
     */
    @Override
    public GameView clone(){
        return new GameView(this);
    }

    /**
     * The One Player Left procedure is called when it is noted that there's only one active player.
     */
    private synchronized void OPLProcedure(boolean wait){
        if(wait){
            try{
                // Wait for some seconds.
                synchronized (OPLLock){
                    OPLLock.wait(timeoutOnePlayer*1000);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        if(getActivePlayers() > 1){
            synchronized (mvListeners){
                for(int i = 0; i < mvListeners.size(); i++){
                    mvListeners.get(i).addEvent(new ServerMessage("The game is resuming...", "every one"));
                }
            }
        }
        else{
            if     (getActivePlayers() == 1) System.out.println("[END] Game ended: only one player left.");
            else if(getActivePlayers() == 0)
                System.out.println("[END] Game ended: no players left.");

            endGame(7);
        }
    }

    /**
     * Method to disconnect a player from the game.
     * @param player
     */
    public void disconnectPlayer(Player player){

        if(!isStarted){
            players.remove(player);
            numPlayers--;
            if(numPlayers <= 1) OPLProcedure(false);
        }
        else player.isDisconnected = true;

        // Send a notification to every active player.
        synchronized (mvListeners){
            for(Player p : players){
                if(!p.isDisconnected){
                    getMVListenerByNickname(p.getNickname()).addEvent(new PlayerDisconnected("every one", player != null ? player.getNickname() : "A client", getActivePlayers(), false));
                };
            }
        }

        if(!isStarted) return;

        // If it's the disconnected player's turn, simply skips it and go to the next player.
        if(getCurrentPlayerNickname().equals(player.getNickname())){
            if(turnPhase == 0){
                // Skips the turn.
            }
            else if(turnPhase == 1){
                PlayableCard newCard = null;

                // Draws the first card on the table.
                for(PlayableCard card : tablecenter.getCenterCards()){
                    if(card!=null){newCard = card;
                    break;}
                }

                try{
                    player.getHand().DrawPositionedCard(newCard);
                }catch (HandFullException | isEmptyException ignored){}
            }
            else if(turnPhase == 2){
                // Skips the turn.
            }

            if(getCurrentPlayerNickname() != null){
                for(ModelViewListener listener : mvListeners){
                    listener.addEvent(new EndTurn(getCurrentPlayerNickname(), players.get(getCurPlayerPosition()).getNickname(), clone()));
                }
            }

            isTurnSkipped = true;
        }
    }

    /**
     * Method that returns the number of currently active players. Notice that it is equivalent to the
     * number of listeners in their list.
     * @return
     */
    private int getActivePlayers(){
        return mvListeners.size();
    }

    /**
     * Method to stop the running threads.
     */
    public void stop(){
        running = false;
    }

    /**
     * Method to rejoin the game after a disconnection.
     * @param newListener the listener associated to the rejoined player.
     */
    public synchronized void rejoin(ModelViewListener newListener) {
        int pos = -1;

        // Notify the rejoined player about the current situation.
        newListener.addEvent(new TurnOrder(newListener.nickname, turnOrder, this.clone()));

        // Notify the other players about the rejoining.
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getNickname().equals(newListener.nickname)) pos = i;
        }

        players.get(pos).isDisconnected = false;
    }

    /**
     * Getter for the listener associated with the nickname.
     * @param nickname
     * @return the listener if found, else null.
     */
    public ModelViewListener getMVListenerByNickname(String nickname){
        for(ModelViewListener listener : mvListeners){
            if(listener.nickname.equals(nickname)){
                return listener;
            }
        }

        return null;
    }

    /**
     * Getter for available tokens.
     * @return
     */
    public ArrayList<TokenColor> getAvailableTokens() {
        return availableTokens;
    }

    /**
     * Setter for the listeners' list.
     * @param mvListeners
     */
    public void setMVListeners(ArrayList<ModelViewListener> mvListeners) {
        this.mvListeners = mvListeners;
    }

    /**
     * Getter for the listeners' list.
     * @return the listeners' list.
     */
    public ArrayList<ModelViewListener> getMvListeners() {
        return mvListeners;
    }
}









