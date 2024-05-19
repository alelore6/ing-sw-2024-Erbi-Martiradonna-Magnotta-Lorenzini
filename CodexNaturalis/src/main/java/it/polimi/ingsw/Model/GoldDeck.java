package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

public class GoldDeck extends Deck{

    private GoldCard[] cards;

    public GoldDeck(){

        NCards = 40;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/data/gold_cards.json");
            if (inputStream == null) {
                throw new FileNotFoundException("File not found!");
            }
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();

            cards = gson.fromJson(reader, GoldCard[].class);

        } catch (IOException e) {
            System.out.println("Error with JSON.");
        }

        cards = (GoldCard[]) shuffle(cards);
    }

    public GoldCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;

            // I supposed the drawn card is the one available with the highest index.
            return cards[NCards];
        }
        // WARNING! We don't delete the card from the array doing this.
    }
}
