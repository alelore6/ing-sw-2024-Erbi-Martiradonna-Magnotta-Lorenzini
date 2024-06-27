package it.polimi.ingsw.ModelView;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.Hand;
import it.polimi.ingsw.Model.PlayableCard;
import it.polimi.ingsw.Model.StartingCard;

import java.io.Serializable;

/**
 * Represent the hand of a player in a certain moment
 */
public class HandView implements Serializable {

    /**
     * the margin for the grid of played cards.
     */
    public static final int GRID_MARGIN = 2;

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
    }

    /**
     * method to restrict the matrix to its minimum (up to a margin) in terms of size.
     * Also add a card with negative id in the position where its possible to play a card.
     * @param matrix the matrix before
     * @return the restricted matrix
     */
    private static Card[][] subMatrix(Card[][] matrix) {

        if(matrix != null && matrix[40] != null && matrix[40][40] == null)
            return null;

        int n = matrix.length;
        int minRow = n, maxRow = -1, minColumn = n, maxColumn = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != null) {
                    if (i < minRow)    minRow    = i;
                    if (i > maxRow)    maxRow    = i;
                    if (j < minColumn) minColumn = j;
                    if (j > maxColumn) maxColumn = j;
                }
            }
        }

        // Renormalization of pathological cases:
        // if the margins are too big, it simply rescales the min and max of rows and columns.
        if(minRow    - GRID_MARGIN < 0)  minRow    =      GRID_MARGIN;
        if(minColumn - GRID_MARGIN < 0)  minColumn =      GRID_MARGIN;
        if(maxRow    + GRID_MARGIN > 80) maxRow    = 80 - GRID_MARGIN;
        if(maxColumn + GRID_MARGIN > 80) maxColumn = 80 - GRID_MARGIN;

        // Calcola la dimensione della sotto-matrice quadrata
        int maxUsedSpace = Math.max(Math.abs(maxRow - minRow), Math.abs(maxColumn - minColumn)) + 1;
        int size;

        if(maxUsedSpace > 81)   size = 81;
        else                    size = 2 * GRID_MARGIN + maxUsedSpace;

        // Estrai la sotto-matrice quadrata
        Card[][] subMatrix = new Card[size][size];

        for (int i = minRow - GRID_MARGIN; i <= maxRow + GRID_MARGIN; i++) {
            for (int j = minColumn - GRID_MARGIN; j <= maxColumn + GRID_MARGIN; j++) {
                // If the current element is null, it checks possible adjacent cards and, if it finds at least one,
                // it marks this position with yellow, else with black.
                if(matrix[i][j] == null){
                    if(checkNear(matrix, i, j)){
                        subMatrix[i - (minRow - GRID_MARGIN)][j - (minColumn - GRID_MARGIN)]
                                = new StartingCard(-1);
                    }
                }
                else{
                    subMatrix[i - (minRow - GRID_MARGIN)][j - (minColumn - GRID_MARGIN)] = matrix[i][j];
                }
            }
        }

        return subMatrix;
    }

    /**
     * checks if the position can accept a play card following the game rules
     * @param playedCards the matirx of played cards
     * @param x the row-coordinates of the position
     * @param y the column-coordinates of the position
     * @return a boolean representing the result of the method
     */
    private static boolean checkNear(Card[][] playedCards, int x, int y){
        boolean hasNear = false;
        if(x-1 >= 0 && y-1 >= 0 && playedCards[x-1][y-1] != null){
            if(playedCards[x-1][y-1].getCorners()[3].getPosition() != null)
                hasNear = true;
            else
                return false;
        }
        if(x-1 >= 0 && y+1 <= 80 && playedCards[x-1][y+1] != null){
            if(playedCards[x-1][y+1].getCorners()[2].getPosition() != null)
                hasNear = true;
            else
                return false;
        }
        if(x+1 <= 80 && y-1 >= 0 && playedCards[x+1][y-1] != null){
            if(playedCards[x+1][y-1].getCorners()[1].getPosition() != null)
                hasNear = true;
            else
                return false;
        }
        if(x+1 <= 80 && y+1 <= 80 && playedCards[x+1][y+1] != null){
            if(playedCards[x+1][y+1].getCorners()[0].getPosition() != null)
                hasNear = true;
            else
                return false;
        }

        return hasNear;
    }
}
