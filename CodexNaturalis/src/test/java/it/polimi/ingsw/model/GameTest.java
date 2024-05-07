package it.polimi.ingsw.model;

import it.polimi.ingsw.Events.DrawCardRequest;
import it.polimi.ingsw.Events.PlayCardRequest;
import it.polimi.ingsw.Events.YourTurn;
import it.polimi.ingsw.Listeners.ModelViewListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class GameTest {

 private Game game;
 private Player[] players;
 private Player previousPlayer;
 private Player nextPlayer;
private int numberOfPlayers;
private List<ModelViewListener> mvListeners;
private List<Player> playerListeners;
    private TableCenter tablecenter;


    @Before
    public void setUp() throws Exception {
        game = new Game();
        mvListeners = new ArrayList<>();
        playerListeners = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            mvListeners.add(mock(ModelViewListener.class));
            playerListeners.add(mock(Player.class));
            when(playerListeners.get(i).getNickname()).thenReturn("player" + i);
        }
        game.setPlayers(playerListeners.toArray(new Player[playerListeners.size()]));
        game.setModelViewListeners(mvListeners);

        players = new Player[4]; /** As example i choose 4 players to do the test, highest amount of player**/
       for (int i = 0; i < players.length; i++) {
           players[i] = mock(Player.class);
       }
       game.setPlayers(players);
       previousPlayer = players[0];
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addListener() {

    }

    @Test
    public void getTablecenter() {
        Game game = new Game();
        TableCenter[][] tableCenters= game.getTablecenter();
        assertNotNull(tableCenters);
        assertEquals(81,tableCenters.length);
        for(int i=0; i<81; i++){
            assertEquals(81,tableCenters[i].length);
        }
    }

    @Test
    public void getPlayers() {
    ArrayList<Player> players = new ArrayList<>();

    players.add(new Player("John"));
    players.add(new Player("Jane"));

    assertFalse(players.isEmpty());

    assertEquals(2,players.size());
    }

    @Test
    public void getNumPlayers() {
        Game game = new Game(4);
        int numPlayers = game.getNumPlayers();
        assertEquals(4, numPlayers);
    }

    @Test
    public void getRemainingTurns() {
     Game game = new Game();
     assertEquals(5, game.getRemainingTurns());
    }

    @Test
    public void getTurnCounter() {
    }

    @Test
    public void getCurrentPlayer() {
    }

    @Test
    public void startGame() {
        Game game = new Game();
        game.startGame();

        TableCenter tableCenter = game.getTablecenter();
        assertNotNull(tableCenter);

        ObjectiveCard objectiveCard = game.getObjectiveCard;
        assertNotNull(objectiveCard);

        StartingCard startingCard = game.getStartingCard;
        assertNotNull(startingCard);

        String TokenColorRequest = game.getTokenColorRequest;
        assertNotNull(TokenColorRequest);
    }

    @Test
    public void endGame() {
    }

    @Test
    public void checkWinner() {

    }

    @Test
    public void nextPlayer() {
    game.nextPlayer(previousPlayer);

    for (int i = 0; i < players.length; i++) {
    verify(mvListeners.get(i), times(1)).addEvent(any(YourTurn.class));
    if (i==0){
        verify(mvListeners.get(i), times(1)).addEvent(PlayCardRequest.class);
    }else{
        verify(mvListeners().get(i), never()).addEvent(any(PlayCardRequest.class));
    }

    if(!tablecenter.getResDeck().AckEmpty && !tablecenter.getGoldDeck().AckEmpty && !tablecenter.getTablecenter().AckEmpty )
        verify(mvListeners.get(0), times(1)).addEvent(any(DrawCardRequest.class));
    else
        verify(mvListeners.get(0),never()).addEvent(any(DrawCardRequest.class));
        assertEquals(1,getTurnCounter();
        }


    /** to test nextplayer methdod i verified  that the Your Turn and PlayCardRequest events are sent correctly,as the base principle of the turn passing**/
    /** next i checked the Draw Card Request and the correct update of the counter.  **/
    }

    @Test
    public void getSubmatrix() {
    }

    @Test
    public void checkObjectivePoints() {
    }
}