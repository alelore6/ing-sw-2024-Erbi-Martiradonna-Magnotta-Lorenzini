package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

import static it.polimi.ingsw.Model.CardColor.WHITE;

/**
 *  StartingDeck class represents starting card deck.
 *  It implements Deck because it uses some of the method of that class.
 */
public class StartingDeck extends Deck{
    /**
     *  NCards is the number of cards in the starting deck, the number is equal to six.
     */
    private int NCards = 6;
    /**
     * cards is an array of all starting cards.
     */
    private StartingCard[] cards;

    /**
     *Constructor builds a new Starting Deck by loading starting cards from JSON file.
     * All cards are initialized with the apposite color WHITE and shuffled.
     */
    public StartingDeck() {

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/data/starting_cards.json");
            if (inputStream == null) {
                throw new FileNotFoundException("File not found!");
            }
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();

            cards = gson.fromJson(reader, StartingCard[].class);

        } catch (IOException e) {
            System.out.println("Error with JSON.");
        }

        for(StartingCard card : cards)
            card.color = WHITE;

        cards = (StartingCard[]) shuffle(cards);
    }

    /**
     * draw method represents the draw action, it practically is an assignment to each active player of a starting card to be able to play.
     * @return the starting card drawn
     * @throws isEmptyException if the deck is empty
     */
    public StartingCard draw() throws isEmptyException {
        if (NCards == 0) {
            throw new isEmptyException(this);
        } else {
            StartingCard drawnCard = cards[--NCards];
            return drawnCard;
        } //Not removing from array,
    }
}