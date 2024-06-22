//import it.polimi.ingsw.Exceptions.HandFullException;
//import it.polimi.ingsw.Exceptions.isEmptyException;
//import it.polimi.ingsw.Model.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//public class HandTest {
//    public Hand hand;
//    public Player player;
//    public Card cars;
//    public Deck deck;
//   public TableCenter tableCenter;
//    @Before
//    public void setUp() throws Exception {
//        player =  Mockito.mock(Player.class);
//        deck = Mockito.mock(Deck.class);
//        hand = Mockito.mock(Hand.class);
//        tableCenter = Mockito.mock(TableCenter.class);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void getDisplayedCards() {
//        assertNotNull(hand.getDisplayedCards());
//    }
//
//    @Test
//    public void drawFromDeck() {
//
//        try {
//            hand.(deck);
//            fail("Expected HandFullException");
//        } catch (HandFullException e) {
//
//        } catch (isEmptyException e) {
//            fail("Unexpected isEmptyException");
//        }
//        try {
//            Deck emptyDeck = new Deck();
//            hand.DrawFromDeck(emptyDeck);
//            fail("Expected isEmptyException");
//        } catch (isEmptyException e) {
//        } catch (HandFullException e) {
//            fail("Unexpected HandFullException");
//        }
//
//
//        }
//
//    @Test
//    public void drawPositionedCard() {
//
//
//
//        player.game = new Game( 2, null, null);
//
//        PlayableCard mockCard = new PlayableCard();
//        tableCenter.drawAndPosition(mockCard);
//        //created a mock to simulate the game//
//        assertDoesNotThrow(() -> hand.DrawPositionedCard(mockCard));
//        // verify there aren't exception//
//        assertNotNull(hand.getHandCard(0));
//        assertEquals(mockCard, hand.getHandCard(0));
//        //verify drawn card has been added to the hand//
//    }
//
//    @Test
//    public void getHandCard() {
//    }
//
//    @Test
//    public void testplayCard() {
//    player = new Player("Test Player.");
//    hand = new Hand(player);
//
//    Card cardtoplay= new ResourceCard(); // i choose Resource Card as an example//
//         Card[][] displayedCards = new Card[81][81];
//
//        assertDoesNotThrow(() -> hand.playCard(cardtoplay, 10, 45)); // 10,45 as a possible position where a card can be positioned//
//        assertEquals(cardtoplay, displayedCards[10][45]);t
//
//    }
//
//    @Test
//    public void testGetDisplayedCards() {
//    }
//
//
//    @Test
//    public void testGetHandCard() {
//        assertNotNull(hand.getHandCard(0));
//    }
//
//
//    4
//}