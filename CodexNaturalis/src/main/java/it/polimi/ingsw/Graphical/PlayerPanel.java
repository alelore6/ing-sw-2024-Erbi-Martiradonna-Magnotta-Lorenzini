package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JScrollPane {

    PlayerPanel(PlayerView playerView, JPanel panel){
        super(panel);
        panel.setBackground(Color.ORANGE);


    }


    protected void update(PlayerView playerView) {

    }

    public static int[][] subMatrix(int[][] matrix) {
        int n = matrix.length;
        int startRow = n, endRow = -1, startCol = n, endCol = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 0) {
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
        int[][] subMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (startRow + i < n && startCol + j < n) {
                    subMatrix[i][j] = matrix[startRow + i][startCol + j];
                } else {
                    subMatrix[i][j] = 0;
                }
            }
        }

        return subMatrix;
    }
}
