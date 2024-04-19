package it.polimi.ingsw.model;

/**
 * Exception that is thrown when a draw is requested on an empty deck
 * Exception will be handled by the controller, may cause EndGame()
 */
public class isEmptyException extends Throwable{
    /**
     * the deck that throws the exception
     */
    public final Deck deck;
    isEmptyException(Deck deck){
        this.deck=deck;
    }
}
