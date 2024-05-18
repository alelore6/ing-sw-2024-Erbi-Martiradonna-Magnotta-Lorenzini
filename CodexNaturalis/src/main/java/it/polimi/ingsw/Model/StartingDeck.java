package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

public class StartingDeck extends Deck{

    int NCards = 6;
    private StartingCard[] cards;

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