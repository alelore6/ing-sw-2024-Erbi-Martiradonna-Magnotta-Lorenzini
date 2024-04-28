package it.polimi.ingsw.View;

import it.polimi.ingsw.Messages.Events;

public interface View extends Runnable {
    void update(View view, Events e);

    @Override
    void run();


}
