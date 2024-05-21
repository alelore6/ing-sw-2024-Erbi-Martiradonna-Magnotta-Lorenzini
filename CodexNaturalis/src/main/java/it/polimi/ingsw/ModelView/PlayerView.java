package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Token;

import java.io.Serializable;

public class PlayerView implements Serializable {
    public final String nickname;
    public final ObjectiveCard objectiveCard;
    public final TokenView token;
    public final HandView hand;

    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.objectiveCard = player.getObjective();
        this.token = new TokenView(player.getToken());
        this.hand= new HandView(player.getHand());

    }
}
