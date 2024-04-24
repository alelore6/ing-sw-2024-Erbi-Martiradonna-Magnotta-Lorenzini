package it.polimi.ingsw.Network;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;

public class GameServer {
    private Controller controller;
    private String[] playingUsers;

    //deve avere i client???
    public static void main( String[] args ){

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(/*port number*/);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            boolean newConnection=false;
            //1. accetto connessioni in entrambi i modi
                //1.1 cerco connessione nel primo modo, se trovata newConnection=true;
                //1.2 se !newConnection cerco nel secondo modo
            //2. se ho una nuova connessione creo una istanza della classe client
            //3. al client verrà richiesto il nickname e il numero di giocatori se è il primo
            // da chi? dal controller, da qui o dal client stesso?
            //4. con la risposta lo si aggiunge alla lobby
            //5. se la lobby è piena creo il game e (senza riconnessioni) break per uscire dal while
            // altrimenti ripeto
            break;
        }
        while(true){
            //se la partita è finita
            break;
        }

    }
}
