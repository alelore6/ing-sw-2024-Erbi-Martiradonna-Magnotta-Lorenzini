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
        model = Mockito.mock(Model.class);
        lobby = Mockito.mock(Lobby.class);
        mvListener1 = Mockito.mock(ModelViewListener.class);
        mvListener2 = Mockito.mock(ModelViewListener.class);
        mvListener3 = Mockito.mock(ModelViewListener.class);
        mvListener4 = Mockito.mock(ModelViewListener.class);
        server = Mockito.mock(ServerImpl.class);
        logger = Mockito.mock(Logger.class);
        game = Mockito.mock(Game.class);

        controller = new Controller(server);
        NumPlayersResponse numPlayersResponse = Mockito.mock(NumPlayersResponse.class);

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
        controller.updateModel(numPlayersResponse, "Alidoro");
    }

    @Test
    public void createGame() throws RemoteException {

        assertNotNull(lobby.getNumPlayers());
        controller.addPlayerToLobby("Alidoro", mvListener1, null);
        controller.addPlayerToLobby("Pulcinella", mvListener2, null);
        controller.addPlayerToLobby("Arlecchino", mvListener3, null);
        controller.addPlayerToLobby("Pinocchio", mvListener4, null);


        controller.createGame();
        game = controller.getGame();

        assertEquals(4, game.getPlayers().size());

        try {
            controller.createGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addPlayerToLobby() throws RemoteException {
        controller.addPlayerToLobby("Alidoro", this.mvListener1, null);
        controller.addPlayerToLobby("Pulcinella", mvListener2, null);
        controller.addPlayerToLobby("Arlecchino", mvListener3, null);
        controller.addPlayerToLobby("Pinocchio", mvListener4, null);

    }
}


