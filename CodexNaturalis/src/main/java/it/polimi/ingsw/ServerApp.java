package it.polimi.ingsw;

import it.polimi.ingsw.Network.Server.GameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ServerApp {
    private static ServerApp instance = null;
    private static GameServer server=null;

    public static ServerApp getInstance() throws RemoteException {
        if (instance == null) {
            instance = new ServerApp();
        }
        return instance;
    }

    private GameServer getServer(){
        if (server==null){
            server=new GameServer();
        }
        return server;
    }

    public static void main(String[] args) throws RemoteException {

        // come prendo l'indirizzo ip?

        Scanner terminal = new Scanner(System.in);
        Integer port = 0;
        final int portRMI;
        final int portSocket;

        System.out.print("Insert Server RMI port number - 4 digit only: \n");
        while (port.toString().length() != 4){
            try {
                port = Integer.parseInt(terminal.next());
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number!!\n");
            }
            if (port.toString().length() != 4)
                System.out.print("Enter a 4 digit number only: \n");
        }
        portRMI = port;

        port = 0;
        System.out.print("Insert Server Socket port number - 4 digit only: \n");
        while (port.toString().length() != 4){
            try {
                port = Integer.parseInt(terminal.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!");
            }
            if (port.toString().length() != 4)
                System.out.print("Enter a 4 digit number only: ");
        }
        portSocket = port;

        //creo il mio server
        getInstance().getServer();

        // creo server RMI
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI(portRMI);
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI.\n");
            }
        });
        rmiThread.start();

        // creo server socket
        Thread socketThread = new Thread(() -> {
            try {
                startSocket(portSocket);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread.start();
        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection available.");
        }

    }


    private static void startRMI (int port) throws RemoteException{

    }

    private static void startSocket(int port) throws RemoteException {
        // try with resources se sono riuscito a creare il server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while(true){
                //cerco connessione
                Socket socket = serverSocket.accept();

                }
        } catch (IOException e) {
            throw new RuntimeException("Cannot start socket server", e);
        }
    }


}
