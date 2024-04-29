package it.polimi.ingsw.View;

import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import java.util.ArrayList;

public abstract class UI implements View, ModelViewListener {

    protected final String nickname;

    protected ArrayList<ViewControllerListener> listeners;

    // WATCH OUT! Player not created yet. This class will refer only on the nickname.
    // For more info, see a comment on Lobby class
    public UI(String nickname) {
        this.nickname = nickname;
    }

    // Notice that the permanent disconnection of a player doesn't affect the listener.
    // In fact, the message will not be received but no more happens.
    @Override
    public void addListener(ViewControllerListener listener){
        listeners.add(listener);
    }
}
