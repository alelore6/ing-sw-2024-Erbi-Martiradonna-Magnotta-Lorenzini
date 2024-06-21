package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;

/**
 * panel with scroll bars that contains the playedCardPanel
 */
public class PlayerPanel extends JScrollPane {
    /**
     * the played cards panel contained
     */
    private PlayedCardsPanel panel ;
    /**
     * the current player info
     */
    private PlayerView playerView;

    /**
     * Constructor that also creates the relative played cards panel
     * @param playerView the player info
     * @param playing boolean that represent if this panel is contained in a personal panel or not
     */
    PlayerPanel(PlayerView playerView, boolean playing){
        super();
        this.playerView = playerView;
        this.panel= new PlayedCardsPanel(playerView.hand.playedCards, playing);
        setViewportView(panel);
    }

    /**
     * update the current player info and call the updated on the played cards panel
     * @param playerView the players info
     */
    protected void update(PlayerView playerView) {
        this.playerView = playerView;
        panel.update(playerView.hand.playedCards);
    }

    /**
     * Getter for the chosen position where a card will be played
     * @return the card component describing that contains the position
     */
    protected CardComponent getPlayPosition() {
        return panel.getSelectedPosition();
    }

    protected int getCenterRow(){return panel.getCenter_row();}

    protected int getCenterCol(){return panel.getCenter_col();}


}
