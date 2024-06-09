package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JScrollPane {

    private PlayedCardsPanel panel ;

    private PlayerView playerView;

    PlayerPanel(PlayerView playerView){
        super();
        this.playerView = playerView;
//        this.panel= new PlayedCardsPanel(null); //   subMatrix(playerView.hand.playedCards)
//        setViewportView(panel);
    }

    protected void update(PlayerView playerView) {

    }

    public static Card[][] subMatrix(Card[][] matrix) {
        int n = matrix.length;
        int startRow = n, endRow = -1, startCol = n, endCol = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != null) {
                    if (i < startRow) startRow = i;
                    if (i > endRow) endRow = i;
                    if (j < startCol) startCol = j;
                    if (j > endCol) endCol = j;
                }
            }
        }

        // Calcola la dimensione della sotto-matrice quadrata
        int size = Math.max(endRow - startRow + 1, endCol - startCol + 1);

        // Estrai la sotto-matrice quadrata
        Card[][] subMatrix = new Card[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (startRow + i < n && startCol + j < n) {
                    subMatrix[i][j] = matrix[startRow + i][startCol + j];
                } else {
                    subMatrix[i][j] = null;
                }
            }
        }

        return subMatrix;
    }
}
