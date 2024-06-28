package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Exceptions.HandFullException;
import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.ModelView.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class GameTest {

    private Game testGame;

    private final int numPlayers = 4;

    //private int turnCounter;

    //private int timeoutOnePlayer = 5;

    //boolean isFinished;

    //private int remainingTurns;

   // private int curPlayerPosition;

    private StartingDeck startingDeck;

    private Player[] players;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private TableCenter tablecenter;

    private  ArrayList<ModelViewListener> mvListeners;

    private  ArrayList<TokenColor> availableTokens;

   // public int waitNumClient;

    //public int turnPhase;// 0: start turn, 1: play done, 2: draw done

    //public boolean isTriggered ; //to see if endgame is triggered

    private String nicknames[];

    private ModelViewListener modelViewListener;

    @BeforeEach
    public void setUp() {

        tablecenter = mock(TableCenter.class);
        when(tablecenter.getScoretrack()).thenReturn(mock(Scoretrack.class));

        mvListeners = new ArrayList<>();
        ModelViewListener mvListener1 = mock(ModelViewListener.class);
        ModelViewListener mvListener2 = mock(ModelViewListener.class);
        ModelViewListener mvListener3 = mock(ModelViewListener.class);
        ModelViewListener mvListener4 = mock(ModelViewListener.class);

        mvListener1.nickname = "Alidoro";
        mvListener2.nickname = "Pulcinella";
        mvListener3.nickname = "Arlecchino";
        mvListener4.nickname = "Pinocchio";

        mvListeners.add(mvListener1);
        mvListeners.add(mvListener2);
        mvListeners.add(mvListener3);
        mvListeners.add(mvListener4);




//        availableTokens = new ArrayList<TokenColor>();
//        modelViewListener = mock(ModelViewListener.class);
//
//        when(player1.getNickname()).thenReturn("Alidoro");
//        when(player2.getNickname()).thenReturn("Pulcinella");
//        when(player3.getNickname()).thenReturn("Arlecchino");
//        when(player4.getNickname()).thenReturn("Pinocchio");

        nicknames = new String[]{"Alidoro", "Pulcinella", "Arlecchino", "Pinocchio"};



        testGame = new Game(numPlayers, nicknames, mvListeners);



    }

    @Test
    public void testStartGame() throws isEmptyException {
        //precondizioni
        testGame.setCurPlayerPosition(0);

        assertFalse(testGame.isFinished);
        //doNothing().when(modelViewListener).addEvent(any(GenericEvent.class));

        //getter per i deck
//        when(game.getTablecenter().getResDeck().draw()).thenReturn(new ResourceCard(20, 1));
//        when(game.getTablecenter().getGoldDeck().draw()).thenReturn(new GoldCard());
//        when(game.getTablecenter().getObjDeck().draw()).thenReturn(new ObjectiveCard1());

        testGame.players.get(0).setToken(TokenColor.YELLOW);
        testGame.players.get(1).setToken(TokenColor.RED);
        testGame.players.get(2).setToken(TokenColor.GREEN);
        testGame.players.get(3).setToken(TokenColor.BLUE);
        testGame.startGame();

        //verifico che gli eventi siano inviati ai listener
//        verify(mvListeners.get(0)).addEvent(any(SetTokenColorRequest.class));
//        verify(mvListeners.get(0)).addEvent(any(ChooseObjectiveRequest.class));
//        verify(mvListeners.get(0)).addEvent(any(PlaceStartingCard.class));
        testGame.waitNumClient = testGame.getMvListeners().size();

        for(Player p : testGame.getPlayers()) {
            p.getHand().getHandCards()[0] = null;
            p.getHand().getHandCards()[1] = null;
            p.getHand().getHandCards()[2] = null;
        }

        try {
            testGame.startGame();
        } catch (RuntimeException e) {

        }

    }

    @Test
    public void testEndGame() {

        testGame.setCurPlayerPosition(0);
        testGame.endGame(0);
       // when(game.getRemainingTurns()).thenReturn(2);

        assertTrue(testGame.isTriggered);
        assertEquals(0, testGame.getCurPlayerPosition());

    }

    @Test
    public void testEndGame2() {

        testGame.setCurPlayerPosition(0);
        testGame.endGame(5);
        // when(game.getRemainingTurns()).thenReturn(2);

        assertFalse(testGame.isTriggered);
        assertEquals(0, testGame.getCurPlayerPosition());

        testGame.tablecenter.getResDeck().AckEmpty = true;
        testGame.tablecenter.getGoldDeck().AckEmpty = false;
        testGame.endGame(5);

        testGame.tablecenter.getResDeck().AckEmpty = false;
        testGame.tablecenter.getGoldDeck().AckEmpty = true;
        testGame.endGame(6);

        testGame.endGame(7);

    }

    @Test
    public void testEndGame3() {

        testGame.setCurPlayerPosition(0);
        testGame.endGame(4);
        // when(game.getRemainingTurns()).thenReturn(2);

        assertTrue(testGame.isTriggered);
        assertEquals(0, testGame.getCurPlayerPosition());

    }

    @Test
    public void testCheckWinner() {
//        testGame.startGame();
       // tablecenter.getScoretrack().getRankings().get(players[i].getNickname());

//        HashMap<String, Integer> rankings = new HashMap<>();
//        for(int i = 0; i < 4; i++){
//            rankings.put(nicknames[i], 18+i*i);
//        }
//        when(tablecenter.getScoretrack().getRankings()).thenReturn(rankings);
////        for(int i = 0; i < 4; i++) {
////            when(tablecenter.getScoretrack().getRankings().get(players[i].getNickname())).thenReturn(rankings.get(testGame.players[i].getNickname()));
////        }
        HashMap<String, Integer> rankings = new HashMap<>();
        rankings.put("Alidoro", 22);
        rankings.put("Pulcinella", 18);
        rankings.put("Arlecchino", 20);
        rankings.put("Pinocchio", 19);

        when(tablecenter.getScoretrack().getRankings()).thenReturn(rankings);

        ObjectiveCard1 objectiveCard1 = mock(ObjectiveCard1.class);
        when(objectiveCard1.getRequiredPositions()).thenReturn(new int[]{3, 5, 7});
        when(objectiveCard1.getPoints()).thenReturn(5);

        ObjectiveCard1 objectiveCard3 = mock(ObjectiveCard1.class);
        when(objectiveCard3.getRequiredPositions()).thenReturn(new int[]{1, 4, 8});
        when(objectiveCard3.getPoints()).thenReturn(5);

        CardColor color1 = CardColor.BLUE;
        CardColor color2 = CardColor.RED;
        CardColor color3 = CardColor.GREEN;
        CardColor color4 = CardColor.PURPLE;



        CardColor[] colors = {color1, color2, color3};
        CardColor[] colors2 = {color4, color2, color1};
        when(objectiveCard1.getCardColors()).thenReturn(colors);
        when(objectiveCard3.getCardColors()).thenReturn(colors);

        ObjectiveCard2 objectiveCard2 = mock(ObjectiveCard2.class);
        HashMap<Resource, Integer> resources = new HashMap<>();
        for(Resource r: Resource.values()){
            resources.put(r, 1);
        }
        when(objectiveCard2.getReqMap()).thenReturn(resources);
        when(objectiveCard2.getPoints()).thenReturn(4);


        PlayableCard card1 = mock(PlayableCard.class);
        PlayableCard card2 = mock(PlayableCard.class);
        PlayableCard card3 = mock(PlayableCard.class);
        PlayableCard[] handCards = new PlayableCard[]{card1, card2, card3};

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);

        Hand mockHand1 = mock(Hand.class);
        Hand mockHand2 = mock(Hand.class);
        Hand mockHand3 = mock(Hand.class);
        Hand mockHand4 = mock(Hand.class);


        Player[] players = {player1, player2, player3, player4};

        // Set up mock Player behaviors
        when(player1.getNickname()).thenReturn("Alidoro");
        when(player2.getNickname()).thenReturn("Pulcinella");
        when(player3.getNickname()).thenReturn("Arlecchino");
        when(player4.getNickname()).thenReturn("Pinocchio");

        when(player1.getObjective()).thenReturn(objectiveCard1);
        when(player2.getObjective()).thenReturn(objectiveCard1);
        when(player3.getObjective()).thenReturn(objectiveCard3);
        when(player4.getObjective()).thenReturn(objectiveCard2);

        when(player1.getHand()).thenReturn(mockHand1);
        when(player2.getHand()).thenReturn(mockHand2);
        when(player3.getHand()).thenReturn(mockHand3);
        when(player4.getHand()).thenReturn(mockHand4);

        when(mockHand1.getHandCards()).thenReturn(handCards);
        when(mockHand2.getHandCards()).thenReturn(handCards);
        when(mockHand3.getHandCards()).thenReturn(handCards);
        when(mockHand4.getHandCards()).thenReturn(handCards);

        int numRows = 81; // Example: Replace with actual number of rows
        int numCols = 81; // Example: Replace with actual number of columns

        PlayableCard[][] displayedCards = new PlayableCard[numRows][numCols];

        // Example positioning respecting rules:
        displayedCards[0][0] = mock(PlayableCard.class);
        displayedCards[1][1] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[2][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[3][3] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[4][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[5][1] = mock(PlayableCard.class); // Valid diagonal adjacency

        when(mockHand1.getDisplayedCards()).thenReturn(displayedCards);
        when(mockHand2.getDisplayedCards()).thenReturn(displayedCards);
        when(mockHand3.getDisplayedCards()).thenReturn(displayedCards);
        when(mockHand4.getDisplayedCards()).thenReturn(displayedCards);

        CurrentResources mockResources = mock(CurrentResources.class);
        when(player1.getCurrentResources()).thenReturn(mockResources);
        when(player2.getCurrentResources()).thenReturn(mockResources);
        when(player3.getCurrentResources()).thenReturn(mockResources);
        when(player4.getCurrentResources()).thenReturn(mockResources);

        testGame.getTablecenter().getObjCards()[0] = objectiveCard1;
        testGame.getTablecenter().getObjCards()[1] = objectiveCard2;






        testGame.players = new ArrayList<>();
        testGame.players.add(player1);
        testGame.players.add(player2);
        testGame.players.add(player3);
        testGame.players.add(player4);

        testGame.setCurPlayerPosition(1);
        assertEquals(1, testGame.getCurPlayerPosition());
        //when(game.getRemainingTurns()).thenReturn(2);
        testGame.endGame(0);


        testGame.checkWinner();


        //verify(modelViewListener, times(2)).addEvent(any(FinalRankings.class));
    }

    @Test
    public void testNextPlayer() {


        testGame.setCurPlayerPosition(0);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);

        testGame.players.get(0).setToken(TokenColor.YELLOW);
        testGame.players.get(1).setToken(TokenColor.RED);
        testGame.players.get(2).setToken(TokenColor.GREEN);
        testGame.players.get(3).setToken(TokenColor.BLUE);
        testGame.startGame();




        Player[] players = {player1, player2, player3, player4};

        // Set up mock Player behaviors
        when(player1.getNickname()).thenReturn("Alidoro");
        when(player2.getNickname()).thenReturn("Pulcinella");
        when(player3.getNickname()).thenReturn("Arlecchino");
        when(player4.getNickname()).thenReturn("Pinocchio");

        testGame.players = new ArrayList<>();
        testGame.players.add(player1);
        testGame.players.add(player2);
        testGame.players.add(player3);
        testGame.players.add(player4);

        Token token1 = mock(Token.class);
        Token token2 = mock(Token.class);
        Token token3 = mock(Token.class);
        Token token4 = mock(Token.class);

        when(player1.getToken()).thenReturn(token1);
        when(player2.getToken()).thenReturn(token2);
        when(player3.getToken()).thenReturn(token3);
        when(player4.getToken()).thenReturn(token4);

        TokenColor tokenColor1 = TokenColor.BLUE;
        TokenColor tokenColor2 = TokenColor.RED;
        TokenColor tokenColor3 = TokenColor.GREEN;
        TokenColor tokenColor4 = TokenColor.YELLOW;

        when(player1.getToken().getColor()).thenReturn(tokenColor1);
        when(player2.getToken().getColor()).thenReturn(tokenColor2);
        when(player3.getToken().getColor()).thenReturn(tokenColor3);
        when(player4.getToken().getColor()).thenReturn(tokenColor4);

        when(token1.getPlayer()).thenReturn(player1);
        when(token2.getPlayer()).thenReturn(player2);
        when(token3.getPlayer()).thenReturn(player3);
        when(token4.getPlayer()).thenReturn(player4);

        Hand hand1 = mock(Hand.class);
        Hand hand2 = mock(Hand.class);
        Hand hand3 = mock(Hand.class);
        Hand hand4 = mock(Hand.class);

        when(player1.getHand()).thenReturn(hand1);
        when(player2.getHand()).thenReturn(hand2);
        when(player3.getHand()).thenReturn(hand3);
        when(player4.getHand()).thenReturn(hand4);

        // Mocking hand cards
        PlayableCard card1 = mock(PlayableCard.class);
        PlayableCard card2 = mock(PlayableCard.class);
        PlayableCard card3 = mock(PlayableCard.class);
        PlayableCard[] handCards = new PlayableCard[]{card1, card2, card3};

        when(hand1.getHandCards()).thenReturn(handCards);
        when(hand2.getHandCards()).thenReturn(handCards);
        when(hand3.getHandCards()).thenReturn(handCards);
        when(hand4.getHandCards()).thenReturn(handCards);

        PlayableCard[][] displayedCards = new PlayableCard[81][81];

        // Example positioning respecting rules:
        displayedCards[0][0] = mock(PlayableCard.class);
        displayedCards[1][1] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[2][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[3][3] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[4][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[5][1] = mock(PlayableCard.class); // Valid diagonal adjacency

        when(hand1.getDisplayedCards()).thenReturn(displayedCards);
        when(hand2.getDisplayedCards()).thenReturn(displayedCards);
        when(hand3.getDisplayedCards()).thenReturn(displayedCards);
        when(hand4.getDisplayedCards()).thenReturn(displayedCards);

        CurrentResources mockResources = mock(CurrentResources.class);
        when(player1.getCurrentResources()).thenReturn(mockResources);
        when(player2.getCurrentResources()).thenReturn(mockResources);
        when(player3.getCurrentResources()).thenReturn(mockResources);
        when(player4.getCurrentResources()).thenReturn(mockResources);

        testGame.nextPlayer(testGame.getPlayers().get(0));

        assertEquals(1, testGame.getCurPlayerPosition());

    }



    @Test
    public void testCheckObjectivePoints1() {
        ObjectiveCard1 objCard = mock(ObjectiveCard1.class);
        when(objCard.getRequiredPositions()).thenReturn(new int[]{3,5,7});
        when(objCard.getPoints()).thenReturn(10);

        int points = testGame.checkObjectivePoints(objCard, 0);

        assertEquals(0, points);
    }

    @Test
    public void testCheckObjectivePoints2() {
        ObjectiveCard2 objCard = mock(ObjectiveCard2.class);
        when(objCard.getPoints()).thenReturn(10);
        HashMap<Resource, Integer> mapping = new HashMap<>();
        for(Resource r : Resource.values()){
            mapping.put(r, 1);
        }
        when(objCard.getReqMap()).thenReturn(mapping);

        int points = testGame.checkObjectivePoints(objCard, 0);

        assertEquals(0, points);
    }

    @Test
    public void testClone() {

        GameView clonedGameView = testGame.clone();
        assertNotNull(clonedGameView);

    }

    @Test
    public void testDisconnectPlayer(){
        // Creazione del mock per Game e dei giocatori
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);

        // Impostazione dei comportamenti dei giocatori
        when(player1.getNickname()).thenReturn("Player1");
        when(player2.getNickname()).thenReturn("Player2");
        when(player3.getNickname()).thenReturn("Player3");
        when(player4.getNickname()).thenReturn("Player4");



        testGame.players = new ArrayList<>();
        testGame.players.add(player1);
        testGame.players.add(player2);
        testGame.players.add(player3);
        testGame.players.add(player4);



        ModelViewListener listener1 = mock(ModelViewListener.class);
        ModelViewListener listener2 = mock(ModelViewListener.class);
        ModelViewListener listener3 = mock(ModelViewListener.class);
        ModelViewListener listener4 = mock(ModelViewListener.class);

        ArrayList<ModelViewListener> mvListeners = new ArrayList<>();
        mvListeners.add(listener1);
        mvListeners.add(listener2);
        mvListeners.add(listener3);
        mvListeners.add(listener4);

        testGame.setMVListeners(mvListeners);

        // Configurazione del comportamento del metodo getMVListenerByNickname
        listener1.nickname = "Player1";
        listener2.nickname = "Player2";
        listener3.nickname = "Player3";
        listener4.nickname = "Player4";

//        when(testGame.getMVListenerByNickname("Player1")).thenReturn(listener1);
//        when(testGame.getMVListenerByNickname("Player2")).thenReturn(listener2);
//        when(testGame.getMVListenerByNickname("Player3")).thenReturn(listener3);

        Hand hand1 = mock(Hand.class);
        Hand hand2 = mock(Hand.class);
        Hand hand3 = mock(Hand.class);
        Hand hand4 = mock(Hand.class);


        when(player1.getHand()).thenReturn(hand1);
        when(player2.getHand()).thenReturn(hand2);
        when(player3.getHand()).thenReturn(hand3);
        when(player4.getHand()).thenReturn(hand4);

        PlayableCard card1 = mock(PlayableCard.class);
        PlayableCard card2 = mock(PlayableCard.class);
        PlayableCard card3 = mock(PlayableCard.class);


        PlayableCard[] handCards = new PlayableCard[]{card1, card2, card3};

        when(hand1.getHandCards()).thenReturn(handCards);
        when(hand2.getHandCards()).thenReturn(handCards);
        when(hand3.getHandCards()).thenReturn(handCards);
        when(hand4.getHandCards()).thenReturn(handCards);

        PlayableCard[][] displayedCards = new PlayableCard[81][81];

        // Example positioning respecting rules:
        displayedCards[0][0] = mock(PlayableCard.class);
        displayedCards[1][1] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[2][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[3][3] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[4][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[5][1] = mock(PlayableCard.class); // Valid diagonal adjacency

        when(hand1.getDisplayedCards()).thenReturn(displayedCards);
        when(hand2.getDisplayedCards()).thenReturn(displayedCards);
        when(hand3.getDisplayedCards()).thenReturn(displayedCards);
        when(hand4.getDisplayedCards()).thenReturn(displayedCards);

        CurrentResources mockResources = mock(CurrentResources.class);
        when(player1.getCurrentResources()).thenReturn(mockResources);
        when(player2.getCurrentResources()).thenReturn(mockResources);
        when(player3.getCurrentResources()).thenReturn(mockResources);
        when(player4.getCurrentResources()).thenReturn(mockResources);

        testGame.setCurPlayerPosition(0);
        // Eseguo la disconnessione del giocatore 1
        testGame.disconnectPlayer(player1);

        testGame.turnPhase = 1;
        testGame.disconnectPlayer(player1);

        // Verifica che il giocatore sia stato segnato come disconnesso


    }

    @Test
    public void testRejoin(){

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);

        // Impostazione dei comportamenti dei giocatori
        when(player1.getNickname()).thenReturn("Player1");
        when(player2.getNickname()).thenReturn("Player2");
        when(player3.getNickname()).thenReturn("Player3");
        when(player4.getNickname()).thenReturn("Player4");



        testGame.players = new ArrayList<>();
        testGame.players.add(player1);
        testGame.players.add(player2);
        testGame.players.add(player3);
        testGame.players.add(player4);

        ModelViewListener listener1 = mock(ModelViewListener.class);
        ModelViewListener listener2 = mock(ModelViewListener.class);
        ModelViewListener listener3 = mock(ModelViewListener.class);
        ModelViewListener listener4 = mock(ModelViewListener.class);
        ArrayList<ModelViewListener> mvListeners = new ArrayList<>();
        mvListeners.add(listener1);
        mvListeners.add(listener2);
        mvListeners.add(listener3);
        mvListeners.add(listener4);

        testGame.setMVListeners(mvListeners);

        // Configurazione del comportamento del metodo getMVListenerByNickname
        listener1.nickname = "Player1";
        listener2.nickname = "Player2";
        listener3.nickname = "Player3";
        listener4.nickname = "Player4";

        Hand hand1 = mock(Hand.class);
        Hand hand2 = mock(Hand.class);
        Hand hand3 = mock(Hand.class);
        Hand hand4 = mock(Hand.class);


        when(player1.getHand()).thenReturn(hand1);
        when(player2.getHand()).thenReturn(hand2);
        when(player3.getHand()).thenReturn(hand3);
        when(player4.getHand()).thenReturn(hand4);

        PlayableCard card1 = mock(PlayableCard.class);
        PlayableCard card2 = mock(PlayableCard.class);
        PlayableCard card3 = mock(PlayableCard.class);


        PlayableCard[] handCards = new PlayableCard[]{card1, card2, card3};

        when(hand1.getHandCards()).thenReturn(handCards);
        when(hand2.getHandCards()).thenReturn(handCards);
        when(hand3.getHandCards()).thenReturn(handCards);
        when(hand4.getHandCards()).thenReturn(handCards);

        PlayableCard[][] displayedCards = new PlayableCard[81][81];

        // Example positioning respecting rules:
        displayedCards[0][0] = mock(PlayableCard.class);
        displayedCards[1][1] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[2][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[3][3] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[4][2] = mock(PlayableCard.class); // Valid diagonal adjacency
        displayedCards[5][1] = mock(PlayableCard.class); // Valid diagonal adjacency

        when(hand1.getDisplayedCards()).thenReturn(displayedCards);
        when(hand2.getDisplayedCards()).thenReturn(displayedCards);
        when(hand3.getDisplayedCards()).thenReturn(displayedCards);
        when(hand4.getDisplayedCards()).thenReturn(displayedCards);

        CurrentResources mockResources = mock(CurrentResources.class);
        when(player1.getCurrentResources()).thenReturn(mockResources);
        when(player2.getCurrentResources()).thenReturn(mockResources);
        when(player3.getCurrentResources()).thenReturn(mockResources);
        when(player4.getCurrentResources()).thenReturn(mockResources);

        testGame.rejoin(listener1);
    }



}