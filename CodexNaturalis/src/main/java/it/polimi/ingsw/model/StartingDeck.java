package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.FileReader;
import java.io.IOException;

public class StartingDeck extends Deck{

    int NCards = 6;
    private StartingCard[] cards;

    public StartingDeck() {

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/data/starting_cards.json")) {
            cards = gson.fromJson(reader, StartingCard[].class);
        } catch (IOException e) {
            System.out.println("Error, not found.");
        }

        cards = (StartingCard[]) shuffle(cards);
    }

    public StartingCard draw() throws isEmptyException {
        if (NCards == 0) {
            throw new isEmptyException(this);
        } else {
            StartingCard drawnCard = cards[--NCards];
            return drawnCard;
        } //Not removing from array,
    }
}