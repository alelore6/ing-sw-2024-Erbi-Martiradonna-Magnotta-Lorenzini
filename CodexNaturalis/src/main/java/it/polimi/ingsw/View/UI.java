package it.polimi.ingsw.View;

import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.util.ArrayList;

public abstract class UI implements View{

    protected final String nickname;

    protected final ArrayList<ViewControllerListener> listeners;

    // WATCH OUT! Player not created yet. This class will refer only on the nickname.
    // For more info, see a comment on Lobby class
    public UI(String nickname) {
        this.nickname = nickname;
        this.listeners = new ArrayList<ViewControllerListener>();
    }
}
