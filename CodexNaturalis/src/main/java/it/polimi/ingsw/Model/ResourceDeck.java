package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

import static it.polimi.ingsw.Model.Position.*;

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

        for(ResourceCard card : cards){
            card.backCorners = new Corner[4];
            for(int i = 0; i < 4; i++){
                Position pos = null;
                switch (i){
                    case 0 -> pos = UP_SX;
                    case 1 -> pos = UP_DX;
                    case 2 -> pos = DOWN_SX;
                    case 3 -> pos = DOWN_DX;
                }
                card.backCorners[i] = new Corner(pos, null);
            }
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

    public CardColor peek() {
        return cards[getNCards()-1].getColor();
    }
}
