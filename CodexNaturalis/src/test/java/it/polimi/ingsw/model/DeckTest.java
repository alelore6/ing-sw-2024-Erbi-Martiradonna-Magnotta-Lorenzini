package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.isEmptyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
// dovrei creare il file Maven pom.xml per inserire le seguenti dipendenze
// <dependency>
//    <groupId>org.mockito</groupId>
//    <artifactId>mockito-core</artifactId>
//    <version>3.12.4</version>
//    <scope>test</scope>
//</dependency>
// per poter importare Mockito e sfruttare i mock.
public class DeckTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void draw() {
        Card testcard = mock(testcard.Card);
        Deck deck = new Deck()
        try{
            Card drawnCard = deck.draw();
            assertEquals(testcard, drawnCard);
        } catch (isEmptyException e){
            fail("Error during draw.");
        }
    }

    @Test
    public void getNCards() {
    }

    @Test
    public void shuffle() {
    }
}