package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;

/**
 * panel with scroll bars that contains the playedCardPanel
 */
class PlayerPanel extends JScrollPane {
    /**
     * the played cards panel contained
     */
    private final PlayedCardsPanel panel ;

    /**
     * Constructor that also creates the relative played cards panel
     * @param playerView the player info
     * @param playing boolean that represent if this panel is contained in a personal panel or not
     */
    PlayerPanel(PlayerView playerView, boolean playing){
        super();
        this.panel= new PlayedCardsPanel(playerView.hand.playedCards, playing);
        setViewportView(panel);
    }

    /**
     * update the current player info and call the updated on the played cards panel
     * @param playerView the players info
     */
    void update(PlayerView playerView) {
        panel.update(playerView.hand.playedCards);
    }

    /**
     * Getter for the chosen position where a card will be played
     * @return the card component describing that contains the position
     */
    CardComponent getPlayPosition() {
        return panel.getSelectedPosition();
    }

    /**
     * getter for the center row in the matrix of played cards from the playedCardsPanel
     * @return the center row index
     */
    int getCenterRow(){return panel.getCenter_row();}

    /**
     * getter for the center column in the matrix of played cards from the playedCardPanel
     * @return the center column index
     */
    int getCenterCol(){return panel.getCenter_col();}


}
