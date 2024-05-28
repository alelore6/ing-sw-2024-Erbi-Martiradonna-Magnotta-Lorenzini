package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Listeners.ModelViewListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



import static org.junit.Assert.*;

public class ControllerTest {
 String player1 = "p1";
 String player2 = "p2";

 public Controller controller;
 private ServerImpl server;
 private ModelViewListener modelViewListener;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createGame() throws RemoteException {
 Game game = controller.getGame();



    }

    @Test
    public void endGame() {
    }

    @Test
    public void addPlayerToLobby() {

    }

    @Test
    public void getGame() {
    }

    @Test
    public void updateModel() {
    }

    @Test
    public void getMVListenerByNickname() {
    }

    @Test
    public void getMVListeners() {
    }

    @Test
    public void addMVListener() {
    }
}
/*








* */