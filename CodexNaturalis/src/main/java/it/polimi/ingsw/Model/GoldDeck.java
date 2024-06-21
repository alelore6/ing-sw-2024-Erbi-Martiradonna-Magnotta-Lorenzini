package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

import static it.polimi.ingsw.Model.Position.*;
import static it.polimi.ingsw.Model.Position.DOWN_DX;

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
            System.err.println("Error with JSON.");
        }

        for(GoldCard card : cards){
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

        for(GoldCard card : cards){
            for (Resource resource : Resource.values()) {
                if(!card.getReq().containsKey(resource))
                    card.addReq(resource, 0);
            }
        }

        cards = (GoldCard[]) shuffle(cards);
    }

    public GoldCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;

            GoldCard temp = cards[NCards];

            // the resize of the array is an overkill
            cards[NCards] = null;

            // I supposed the drawn card is the one available with the highest index.
            return temp;
        }
    }

    public CardColor peek(){
        return cards[getNCards()-1].getColor();
    }

    public GoldCard peekNextCard() throws isEmptyException {
        if (getNCards() == 0) {
            throw new isEmptyException(this);
        }
        return cards[getNCards() - 1];
    }

}
