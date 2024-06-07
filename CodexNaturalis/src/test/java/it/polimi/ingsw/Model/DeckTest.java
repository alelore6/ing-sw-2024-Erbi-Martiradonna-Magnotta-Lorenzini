package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.isEmptyException;
import jdk.incubator.vector.VectorShuffle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeckTest {
 private Deck deck;
 private Card card1, card2, card3;
 private Hand hand;
 private Card[] originaldeck, deckshuffled;
    @Before
    public void setUp() throws Exception {
        card1= Mockito.mock(Card.class);
        card2= Mockito.mock(Card.class);
        card3= Mockito.mock(Card.class);
         deck=Mockito.mock(Deck.class);
         originaldeck=Mockito.mock(Card[].class);
         deckshuffled=Mockito.mock(Card[].class);
         hand=Mockito.mock(Hand.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testdraw() {

        try{
            Card drawnCard = deck.draw();
            assertEquals(card1, drawnCard);
        } catch (isEmptyException e){
            fail("Error during draw.");
        }
    }

    @Test
    public void testgetNCards() throws isEmptyException {
        Card drawnCard1= deck.draw();
        assertEquals(card1, drawnCard1);
        Card drawnCard2= deck.draw();
        assertEquals(card2, drawnCard2);
        Card drawnCard3= deck.draw();
        assertEquals(card3, drawnCard3);

        assertEquals( 3, deck.getNCards());
        assertNotNull(hand);
    }

    @Test
    public void testshuffle() {
        List<Card> cardList= Arrays.asList(originaldeck);
        Collections.shuffle(cardList);
        deckshuffled=cardList.toArray(new Card[cardList.size()]);
        assertNotEquals(originaldeck,deckshuffled);
    }

}