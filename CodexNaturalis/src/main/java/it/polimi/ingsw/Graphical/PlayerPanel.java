package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JScrollPane {

    PlayerPanel(PlayerView playerView, JPanel panel){
        super(panel);
        panel.setBackground(Color.ORANGE);

    }

    public static void main(String[] args) {
        // Creazione del frame principale
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                JPanel panel = new JPanel();
                PlayerPanel scrollPane = new PlayerPanel(null,panel);
                // Aggiunta di molti componenti al pannello per creare la necessità di scorrimento
//                for (int i = 1; i <= 50; i++) {
//                    panel.add(new JLabel("Label " + i));
//                }

                frame.add(scrollPane);

                frame.setVisible(true);


//        Card[][] matrix = new Card[20][20];
//
//        // Riempimento della matrice con valori casuali per esempio
//        java.util.Random rand = new java.util.Random();
//        for (int i = 2; i < 15; i++) {
//            for (int j = 7; j < 18; j++) {
//                matrix[i][j] = new StartingCard();;
//                System.out.print(matrix[i][j].getID() + " ");
//            }
//            System.out.println();
//        }
//        // Trova gli indici della sotto matrice
//        int[] result = findSubMatrix(matrix);
//        if (result == null) {
//            System.out.println("Non ci sono elementi non nulli nella matrice.");
//        } else {
//            System.out.printf("Inizio: %d, Fine: %d\n", result[0], result[1]);
//        }
            }



    protected void update(PlayerView playerView) {

    }

    public static int[] findSubMatrix(Card[][] matrix) {
        int n = 20;
        int minRow = n, maxRow = -1;
        int minCol = n, maxCol = -1;

        // Trova i limiti degli indici non nulli
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != null) {
                    if (i < minRow) minRow = i;
                    if (i > maxRow) maxRow = i;
                    if (j < minCol) minCol = j;
                    if (j > maxCol) maxCol = j;
                }
            }
        }

        // Se non ci sono elementi non nulli, ritorna null
        if (minRow == n) {
            return null;
        }

        // Calcola la dimensione della sottomatrice quadrata che copre tutti gli elementi non nulli
        int maxSize = Math.max(maxRow - minRow + 1, maxCol - minCol + 1);

        // Calcola i limiti della sottomatrice quadrata
        int startRow = minRow;
        int startCol = minCol;
        int endRow = startRow + maxSize - 1;
        int endCol = startCol + maxSize - 1;

        // Limita i confini alla dimensione della matrice originale
        endRow = Math.min(endRow, n - 1);
        endCol = Math.min(endCol, n - 1);

        int start = Math.min(startRow, startCol);
        int end = Math.max(endRow, endCol);
        return new int[] { start, end};
    }
}
