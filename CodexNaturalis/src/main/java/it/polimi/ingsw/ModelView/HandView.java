package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.Hand;
import it.polimi.ingsw.Model.PlayableCard;

import java.io.Serializable;

/**
 * Represent the hand of a player in a certain moment
 */
public class HandView implements Serializable {
    /**
     * the player's hand cards
     */
    public final PlayableCard[] handCards;
    /**
     * the player's played cards
     */
    public final Card[][] playedCards;

    /**
     * Constructor that works like a clone method
     * @param hand the player's hand
     */
    public HandView(Hand hand){
        this.handCards=hand.getHandCards().clone();
        this.playedCards= subMatrix(hand.getDisplayedCards().clone());
        //TODO aggiungere possible plays
        //se riesci la cosa migliore sarebbe aggiungerle direttamente a questa matrice
        //puoi usare il costruttore che ho aggiunto per test in resource card mettendo un id negativo (tipo -1)
        // e come play order sparare un numero alto (tipo >100 per sicurezza)
    }

    /**
     * method to restrict the matrix to his minimum in terms of size
     * @param matrix the matrix before
     * @return the restricted matrix
     */
    public static Card[][] subMatrix(Card[][] matrix) {
        //non sono sicuro sia corretto
        // la restringe al minimo mantenendo la matrice quadrata
        //ma a noi ci servirebbero degli spazi a lato

        //se non c'è la starting card ritorno la matrice intera o null?
        if(matrix[40][40]==null) return matrix;


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
