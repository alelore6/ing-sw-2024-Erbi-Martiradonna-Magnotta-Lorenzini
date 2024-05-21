package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class GameView implements Serializable {
    public final ArrayList<PlayerView> players;
    public final int numPlayers;
    public final TableCenterView tableCenterView;


    public GameView(Game model) {
        this.numPlayers = model.getNumPlayers();
        this.players = new ArrayList<PlayerView>();
        for(int i=0;i<numPlayers;i++){
            players.add(new PlayerView(model.players[i]));
        }
        this.tableCenterView = new TableCenterView(model.tablecenter);
    }
}
