package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    protected final String nickname;

     PlayerPanel(PlayerView playerView) {
        super();
        this.nickname = playerView.nickname;
        this.setBackground(Color.YELLOW);

    }

    protected void update(PlayerView playerView) {

    }
}
