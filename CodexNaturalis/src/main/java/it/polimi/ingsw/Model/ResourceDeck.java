package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

public class ResourceDeck extends Deck{

    private ResourceCard[] cards;

    public ResourceDeck(){

        NCards = 40;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/data/resource_cards.json");
            if (inputStream == null) {
                throw new FileNotFoundException("File not found!");
            }
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();

            cards = gson.fromJson(reader, ResourceCard[].class);

        } catch (IOException e) {
            System.err.println("Error with JSON.");
        }

        cards = (ResourceCard[]) shuffle(cards);

    }
    public ResourceCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;

            ResourceCard temp = cards[NCards];

            // the resize of the array is an overkill
            cards[NCards] = null;

            // I supposed the drawn card is the one available with the highest index.
            return temp;
        }
    }
}
