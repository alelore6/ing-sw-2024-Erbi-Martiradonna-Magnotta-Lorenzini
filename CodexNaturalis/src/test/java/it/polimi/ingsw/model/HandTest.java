import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class HandTest {
    public Hand hand;
    private Player player;
    public Card cars;


    @Before
    public void setUp() throws Exception {
        player = new Player("Test Player.");
        hand = new Hand(player);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getDisplayedCards() {
        assertNotNull(hand.getDisplayedCards());
    }

    @Test
    public void drawFromDeck() {
      Deck  deck = new Deck() {

          @Override
          public Card draw() throws isEmptyException {
              return null;
          }
      };
        assertDoesNotThrow(() -> hand.DrawFromDeck(deck));
    }

    @Test
    public void drawPositionedCard() {
    }

    @Test
    public void getHandCard() {

    }

    @Test
    public void playCard() {

    }

    @Test
    public void testGetDisplayedCards() {
    }


    @Test
    public void testGetHandCard() {
        assertNotNull(hand.getHandCard(0));
    }


    @Test
    public void testPlayCard() {
           Card card = new Card() {
               @Override
               public void flip() {

               }

               @Override
               public Card getCard() {
                   return null;
               }
           };

        int x= 5;
        int y = 5;
        assertDoesNotThrow(() -> hand.playCard(card, x, y));
    }
}