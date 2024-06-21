package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.isEmptyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.configuration.IMockitoConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class ResourceDeckTest {
    private ResourceDeck resourceDeck;
    private ResourceDeck resourceDeck2;
   private Card[]cards;
    @Before
    public void setUp() throws Exception {

        resourceDeck= new ResourceDeck();
        resourceDeck2=new ResourceDeck();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getNCards() {

    }

    @Test
    public void shuffle() {

        List<Card> cardList= Arrays.asList(resourceDeck);
        Collections.shuffle(cardList);
        resourceDeck=cardList.toArray(new Card[cardList.size()]);
        assertNotEquals(resourceDeck,resourceDeck2);
    }

    @Test
    public void draw() throws isEmptyException {
        resourceDeck.getNCards();
        resourceDeck.draw();
        verify(resourceDeck, times(1)).draw();
        assertEquals(resourceDeck.getNCards()-1, resourceDeck2.getNCards());

    }
}