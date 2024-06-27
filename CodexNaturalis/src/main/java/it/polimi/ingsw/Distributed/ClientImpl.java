package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class representing a client instance.
 */
public class ClientImpl extends UnicastRemoteObject implements Client{

    /**
     * The client application.
     */
    public final ClientApp clientApp;
    /**
     * The user interface.
     */
    private final UI userInterface;
    /**
     * The nickname of the user.
     */
    private String nickname;
    /**
     * The server to which the client is connected to.
     */
    private final Server server;

    /**
     * Constructor
     *
     * @param clientApp
     * @param server
     * @param isTUI
     * @throws RemoteException
     */
    public ClientImpl(ClientApp clientApp, Server server, boolean isTUI) throws RemoteException {
        super();

        this.clientApp = clientApp;
        this.server = server;
        userInterface = isTUI ? new TUI(this) : new GUI(this);

        run();

        nickname = userInterface.chooseNickname();

        initialize();
    }

    /**
     * Method to initialize the client. It sends a registration request to the server and activates
     * the listener to do it.
     * @throws RemoteException
     * @see UI
     */
    private void initialize() throws RemoteException {
        // Socket
        if(server instanceof ServerStub){
            ((ServerStub) server).register(this);
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
        // RMI
        else{
            // The RMI registration is already implicitly happened
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
    }

    /**
     * Setter for the nickname.
     * @param nickname
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method to start running the UI handling incoming event.
     * @see TUI
     * @see GUI
     */
    private void run() {
        userInterface.run();
    }

    /**
     * Method to pong with the server.
     * @throws RemoteException
     * @see ServerImpl
     */
    @Override
    public void ping() throws RemoteException{}

    /**
     * Method to call the event handler in the UI.
     * @param event
     * @see TUI
     * @see GUI
     */
    @Override
    public void update(GenericEvent event){
        userInterface.update(event);
    }

    /**
     * Method to send an event to the server.
     * @param event
     * @throws RemoteException
     * @see ServerImpl
     * @see ServerStub
     */
    public void sendEvent(GenericEvent event) throws RemoteException {
        server.update(this, event);
    }

    /**
     * Getter for the UI.
     * @return the UI.
     * @see UI
     */
    public UI getUserInterface() {
        return userInterface;
    }

    /**
     * Getter for the nickname.
     * @return the nickname.
     */
    @Override
    public String getNickname(){
        return nickname;
    }
}

