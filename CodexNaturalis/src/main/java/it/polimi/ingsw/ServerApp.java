package it.polimi.ingsw;

import it.polimi.ingsw.Network.Client;
import it.polimi.ingsw.Network.GameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private static GameServer server;
    public static void main(String[] args){
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        // creo server
         ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        server=new GameServer();
        System.out.println("Server ready");


        while(true){
            boolean newConnection=false;
            //1. accetto connessioni in entrambi i modi
            //1.1 cerco connessione nel primo modo
            Socket socket =null;
            try {
                socket  = serverSocket.accept();
                if (socket!=null){
                    newConnection=true;
                }
            } catch (IOException e) {
                break; //server not ready
            }

            //1.2 se !newConnection cerco nel secondo modo

            //2. se ho una nuova connessione ClientApp avrà creato
            // nel thread una istanza di Client, ma come la recupero?
            if(newConnection){
                //server.addClient(client)
            }

        }


    }

    public GameServer getServer() {
        return server;
    }

    }
