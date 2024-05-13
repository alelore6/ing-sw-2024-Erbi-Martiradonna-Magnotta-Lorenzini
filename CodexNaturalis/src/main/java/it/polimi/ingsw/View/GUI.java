package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import static java.lang.String.valueOf;

public class GUI extends UI{

    private static JFrame f;
    public GUI(ClientImpl client) {
        super(client);
    }

    @Override
    protected void printCard(Card card) {

    }


    protected void printCard(int id, boolean isFacedown) { //TEST, DON'T KNOW IF IT MUST BE HERE
        //boolean side = card.isFacedown;
       // int id = card.getID();
        String idString;
        if(id < 100){
            idString = valueOf(id);
            idString = "0"+idString;
            if(id<10){
                idString = "0"+idString;
            }
        }
        else{
            idString = valueOf(id);
        }

        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream("assets/images/cards/" +(isFacedown ? "back" : "front") + "/" + idString +".png");
        BufferedImage img = null;

        try{
            img = ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }


    }

    @Override
    public void update(GenericEvent e){

    }

    @Override
    public void notifyListener(ViewControllerListener listener, GenericEvent e) {

    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new Thread(){
                    @Override
                    public void run() {
                        f = new CustomFrame("CodexNaturalis");
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        f.setSize(1280,720);
                        f.setVisible(true);

//                        while(true){
//                            //gestione eventi continuo
//                        }
                    }
                }.start();

            }
        });
    }
}
