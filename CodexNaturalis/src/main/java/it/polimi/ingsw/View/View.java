package it.polimi.ingsw.View;

import it.polimi.ingsw.Messages.Events;

public interface View extends Runnable {
    void update(Events e);
    @Override
    void run();
}
