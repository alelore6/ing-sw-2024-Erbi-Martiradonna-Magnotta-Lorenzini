package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.*;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerView implements Serializable {
    public final String nickname;
    public final ObjectiveCard objectiveCard;
    public final TokenView token;
    public final HandView hand;
    public final HashMap<Resource,Integer> currentResources;

    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.objectiveCard = player.getObjective();
        this.token = new TokenView(player.getToken());
        this.hand= new HandView(player.getHand());
        this.currentResources=player.getCurrentResources().getCurrentResources();
    }
}
