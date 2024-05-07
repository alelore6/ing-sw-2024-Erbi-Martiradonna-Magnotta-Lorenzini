package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.util.ArrayList;

public abstract class UI implements View{

    public final String nickname;

    protected final ViewControllerListener listener;

    // WATCH OUT! This constructor allows the creation of a TUI w/o having created the actual player
    // The nickname here is not the player's but a test one.
    public UI(String nickname) {
        this.nickname = nickname;
        this.listener = null;
    }

    public UI(ClientImpl client) {
        this.nickname = client.getNickname();
        this.listener = new ViewControllerListener(client);
    }
}
