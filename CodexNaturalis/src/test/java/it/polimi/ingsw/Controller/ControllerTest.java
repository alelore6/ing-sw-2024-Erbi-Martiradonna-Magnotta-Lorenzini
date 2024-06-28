package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerTest {
    private String nicknames[];
    private Lobby lobby;
    private Controller controller;
    private ServerImpl server;
    private ModelViewListener mvListener1, mvListener2, mvListener3, mvListener4;
    private ArrayList<ModelViewListener> mvListeners;
    private Game game;
    private Model model;
    private Player[] players;
    private Logger logger;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @Before
    public void setUp() throws Exception {

        mvListeners = new ArrayList<>();
        model = mock(Model.class);
        lobby = mock(Lobby.class);
        mvListener1 = mock(ModelViewListener.class);
        mvListener2 = mock(ModelViewListener.class);
        mvListener3 = mock(ModelViewListener.class);
        mvListener4 = mock(ModelViewListener.class);
        server = mock(ServerImpl.class);
        logger = mock(Logger.class);
        game = mock(Game.class);

        controller = new Controller(server);
        NumPlayersResponse numPlayersResponse1 = mock(NumPlayersResponse.class);
        NumPlayersResponse numPlayersResponse2 = mock(NumPlayersResponse.class);
        NumPlayersResponse numPlayersResponse3 = mock(NumPlayersResponse.class);
        NumPlayersResponse numPlayersResponse4 = mock(NumPlayersResponse.class);

        mvListener1.nickname = "Alidoro";
        mvListener2.nickname = "Pulcinella";
        mvListener3.nickname = "Arlecchino";
        mvListener4.nickname = "Pinocchio";

        mvListeners.add(mvListener1);
        mvListeners.add(mvListener2);
        mvListeners.add(mvListener3);
        mvListeners.add(mvListener4);
        server.logger = logger;

         controller.addMVListener(mvListener1);
         controller.addMVListener(mvListener2);
         controller.addMVListener(mvListener3);
         controller.addMVListener(mvListener4);


        nicknames = new String[]{"Alidoro", "Pulcinella", "Arlecchino", "Pinocchio"};
//        controller.updateModel(numPlayersResponse1, "Alidoro");
//        controller.updateModel(numPlayersResponse2, "Pulcinella");
//        controller.updateModel(numPlayersResponse3, "Arlecchino");
//        controller.updateModel(numPlayersResponse4, "Pinocchio");

    }

    @Test
    public void createGame() throws RemoteException {

        assertNotNull(lobby.getNumPlayers());

        controller.createLobby(4);

        controller.addPlayerToLobby("Alidoro", mvListener1, null);
        controller.addPlayerToLobby("Pulcinella", mvListener2, null);
        controller.addPlayerToLobby("Arlecchino", mvListener3, null);
        controller.addPlayerToLobby("Pinocchio", mvListener4, null);



        controller.createGame();

        game = controller.getGame();

        assertEquals(4, game.getPlayers().size());

    }

    @Test
    public void addPlayerToLobby() throws RemoteException {
        controller.createLobby(4);
        controller.addPlayerToLobby("Alidoro", mvListener1, null);
        controller.addPlayerToLobby("Pulcinella", mvListener2, null);
        controller.addPlayerToLobby("Arlecchino", mvListener3, null);
        controller.addPlayerToLobby("Pinocchio", mvListener4, null);

        assertEquals(4, controller.getLobby().getPlayers().size());
    }

    @Test
    public void testAddPlayerToLobbyWhenLobbyIsFull() throws RemoteException {
        controller.createLobby(2);
        controller.addPlayerToLobby("Player1", mvListener1, null);
        controller.addPlayerToLobby("Player2", mvListener2, null);

        // Prova ad aggiungere un terzo giocatore quando la lobby è piena
        controller.addPlayerToLobby("Player3", mvListener3, null);
        assertEquals(2, controller.getLobby().getPlayers().size());
    }

    @Test
    public void testAddPlayerToLobbyWhenIsNull() throws RemoteException {
        controller.addPlayerToLobby("Player1", mvListener1, null);
        assertNull(controller.getLobby());
    }


}


