package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.EndGameTriggered;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Events.JoinLobby;
import it.polimi.ingsw.Listeners.ModelViewListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import java.rmi.RemoteException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
 private Lobby lobby;
 public Controller controller;
 private ServerImpl server;
 private ModelViewListener modelViewListener1;
 private ModelViewListener modelViewListener2;

   private ServerImpl serverMock;
   private ModelViewListener listenerMock;

    @Before
    public void setUp() throws Exception {
        controller = Mockito.mock(Controller.class);
         modelViewListener1 = Mockito.mock(ModelViewListener.class);
         modelViewListener2 = Mockito.mock(ModelViewListener.class);
   }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createGame() throws RemoteException {
        when (lobby.getNumPlayers()).thenReturn(4);
        when(lobby .addPlayer("player1")).thenReturn(true);
        when(lobby .addPlayer("player2")).thenReturn(true);
        when(lobby .addPlayer("player3")).thenReturn(true);
        when(lobby .addPlayer("player4")).thenReturn(true);
        controller.createGame();
         verify(controller).createGame();
    }

   @Test
           public void createLobbyTest() {
     assertNotNull(lobby ,"lobby is created successfully");
       }


    @Test
    public void endGame() throws RemoteException {
        controller.createGame();

    }

    @Test
    public void addPlayerToLobby() throws RemoteException {
   String nickname = "FirstPlayer";
   String oldNickname = "Oldlayer";
    controller.addPlayerToLobby(nickname,modelViewListener1,oldNickname);

   verify(lobby).addPlayer("newPlayer");
   verify(lobby).getNumPlayers();
   verify(lobby).getPlayers();
   verify(modelViewListener1).addEvent(new JoinLobby(oldNickname,nickname));
   verify(controller,times(1)).createGame();
    }

    @Test
    public void getGame() {

    }

    @Test
    public void updateModel() {

    }

    @Test
    public void getMVListenerByNickname() throws RemoteException {

 when(controller.getMVListenerByNickname("player1")).thenReturn(modelViewListener1);
 when(controller.getMVListenerByNickname("player2")).thenReturn(modelViewListener2);
 when(controller.getMVListenerByNickname("player3")).thenReturn(null);

 assertEquals(modelViewListener1,controller.getMVListenerByNickname("player1"));
 assertEquals(modelViewListener2,controller.getMVListenerByNickname("player2"));
  assertTrue(controller.getMVListenerByNickname("player3") == null);

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