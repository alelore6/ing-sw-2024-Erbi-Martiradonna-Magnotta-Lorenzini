package it.polimi.ingsw.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

 private Game game;

    @Before
    public void setUp() throws Exception {

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
            for(int j=0; j<81; j++){
                assertEquals(81,tableCenters[i].length);
                assert (tableCenters[i][j] != null);
            }
        }
    }

    @Test
    public void getPlayers() {
    }

    @Test
    public void getNumPlayers() {
        Game game = new Game(4);
        int numPlayers = game.getNumPlayers();
        assertEquals(4, numPlayers);
    }

    @Test
    public void getRemainingTurns() {
    }

    @Test
    public void getTurnCounter() {
    }

    @Test
    public void getCurrentPlayer() {
    }

    @Test
    public void startGame() {
    }

    @Test
    public void endGame() {
    }

    @Test
    public void checkWinner() {
    }

    @Test
    public void nextPlayer() {
    }

    @Test
    public void getSubmatrix() {
    }

    @Test
    public void checkObjectivePoints() {
    }
}