import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HandTest {
    private Hand hand;
    private Player player;
    private Deck deck;
    private TableCenter tableCenter;
    private PlayableCard card;
    private Game game;

    @Before
    public void setUp() throws Exception {
        player =  mock(Player.class);
        tableCenter = mock(TableCenter.class);
        deck = mock(Deck.class);
        card= mock(PlayableCard.class);
        game=mock(Game.class);
        tableCenter = mock(TableCenter.class);
        game.tablecenter=tableCenter;
        player.game=game;

        hand= new Hand(player);

    }


    @Test
    public void testDrawFromDeckFillHand() {
        try {
            when(deck.draw()).thenReturn(card);
            hand.DrawFromDeck(deck);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);

        try {
            hand.DrawFromDeck(deck);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertNull(hand.getHandCards()[2]);

        try {
            hand.DrawFromDeck(deck);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertEquals(hand.getHandCards()[2], card);
    }

    @Test
    public void testDrawFromDeckThrowsHandFullException() {
        try {
            when(deck.draw()).thenReturn(card);
            hand.DrawFromDeck(deck);
            hand.DrawFromDeck(deck);
            hand.DrawFromDeck(deck);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertEquals(hand.getHandCards()[2], card);
        assertThrows(HandFullException.class, ()->hand.DrawFromDeck(deck));
    }

    @Test
    public void testDrawFromDeckThrowsIsEmptyException() throws isEmptyException {
        when(deck.draw()).thenThrow(isEmptyException.class);
        assertThrows(isEmptyException.class,()->hand.DrawFromDeck(deck));
        assertNull(hand.getHandCards()[0]);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);
    }

    @Test
    public void testDrawPositionedCardFillHand() {
        when(tableCenter.drawAndPosition(card)).thenReturn(0);
        try {
            hand.DrawPositionedCard(card);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);

        try {
            hand.DrawPositionedCard(card);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertNull(hand.getHandCards()[2]);

        try {
            hand.DrawPositionedCard(card);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertEquals(hand.getHandCards()[2], card);
    }

    @Test
    public void testDrawPositionedCardThrowsHandFullException(){
        when(tableCenter.drawAndPosition(card)).thenReturn(0);
        try {
            hand.DrawPositionedCard(card);
            hand.DrawPositionedCard(card);
            hand.DrawPositionedCard(card);
        } catch (HandFullException | isEmptyException e ) {
            fail("SHOULD NOT THROW: "+e.getMessage());
        }
        assertEquals(hand.getHandCards()[0], card);
        assertEquals(hand.getHandCards()[1], card);
        assertEquals(hand.getHandCards()[2], card);
        assertThrows(HandFullException.class, ()->hand.DrawPositionedCard(card));
    }

    @Test
    public void testDrawPositionedCardThrowsIsEmptyExceptionCaseBothDeckEmpty(){
        when(tableCenter.drawAndPosition(card)).thenReturn(-1);
        assertThrows(isEmptyException.class,()->hand.DrawPositionedCard(card));
        assertNull(hand.getHandCards()[0]);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);
       }

    @Test
    public void testDrawPositionedCardThrowsIsEmptyExceptionCaseOneDeckEmpty(){
        when(tableCenter.drawAndPosition(card)).thenReturn(1);
        assertThrows(isEmptyException.class,()->hand.DrawPositionedCard(card));
        assertEquals(hand.getHandCards()[0],card);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);
    }
}