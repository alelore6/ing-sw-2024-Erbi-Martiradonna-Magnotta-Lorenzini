import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;
import org.junit.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HandTest {
    private Hand hand;
    private Player player;
    private Deck deck;
    private TableCenter tableCenter;
    private PlayableCard card;
    private Game game;
    private StartingCard startingCard;
    private CurrentResources currentResources;
    private Corner cornerNotVisible;
    private Corner cornerVisible;
    private GoldCard goldCard;


    @Before
    public void setUp() throws Exception {
        player =  mock(Player.class);
        tableCenter = mock(TableCenter.class);
        deck = mock(Deck.class);
        card= mock(PlayableCard.class);
        game=mock(Game.class);
        tableCenter = mock(TableCenter.class);
        startingCard = mock(StartingCard.class);
        currentResources = mock(CurrentResources.class);
        game.tablecenter=tableCenter;
        player.game=game;
        cornerVisible= mock(Corner.class);
        cornerNotVisible = mock(Corner.class);
        goldCard= mock(GoldCard.class);
        HashMap<Resource,Integer> resource=new HashMap<>();
        for (Resource r: Resource.values()){
            resource.put(r,0);
        }
        when(currentResources.getCurrentResources()).thenReturn(resource);
        setField(currentResources,"currentResources",resource);
        setField(cornerNotVisible,"pos", null);
        setField(cornerVisible,"pos", Position.UP_DX);
        Corner[] corners= new Corner[]{cornerVisible, cornerNotVisible,cornerVisible,cornerNotVisible};
        setField(startingCard,"frontCorners", corners);
        setField(startingCard,"backCorners", corners);
        GoldCard goldCard=mock(GoldCard.class);

        when(player.getCurrentResources()).thenReturn(currentResources);
        hand= new Hand(player);
    }

    // To set private fields without setters
    private void setField(Object target, String fieldName, Object value){
        Field field = getField(target.getClass(), fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Recursive method to find the field in the class hierarchy
    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass, fieldName);
            }
        }
        return null;
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
    public void testDrawPositionedCardThrowsIsEmptyExceptionCaseCardNotFound(){
        when(tableCenter.drawAndPosition(card)).thenReturn(-1);
        assertThrows(isEmptyException.class,()->hand.DrawPositionedCard(card));
        assertNull(hand.getHandCards()[0]);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);
       }

    @Test
    public void testDrawPositionedCardThrowsIsEmptyExceptionCaseDeckEmpty(){
        when(tableCenter.drawAndPosition(card)).thenReturn(1);
        assertThrows(isEmptyException.class,()->hand.DrawPositionedCard(card));
        assertEquals(hand.getHandCards()[0],card);
        assertNull(hand.getHandCards()[1]);
        assertNull(hand.getHandCards()[2]);
    }

    @Test
    public void testPlayCardWithStartingCard(){
        int x=40;
        int y=40;
        try {
            hand.playCard(startingCard,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(startingCard,hand.getDisplayedCards()[x][y]);
        assertEquals(0,startingCard.getPlayOrder());
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCaseCardNotFound(){
        int x=41;
        int y=41;
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x,y));
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCaseNoOverlaps(){
        int x=45;
        int y=45;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x,y));
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCasePositionAlreadyOccupied(){
        int x=40;
        int y=40;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x,y));
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCasePositionNextToNotValid(){
        int x=40;
        int y=40;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x,y-1));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x-1,y));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x,y+1));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,x+1,y));
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCaseCornerNotVisible(){
        Corner[] corners= new Corner[]{cornerNotVisible, cornerNotVisible,cornerNotVisible,cornerNotVisible};
        setField(startingCard,"frontCorners", corners);
        setField(startingCard,"backCorners", corners);
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,39,39));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,39,41));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,41,39));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,41,41));

        startingCard.isFacedown=true;
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,39,39));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,39,41));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,41,39));
        assertThrows(WrongPlayException.class, ()-> hand.playCard(card,41,41));
    }

    @Test
    public void testPlayCardThrowsWrongPlayExceptionCaseNotEnoughResources(){
        int x=39;
        int y=39;
        hand.getHandCards()[0]=goldCard;
        HashMap<Resource,Integer> requirements=new HashMap<>();
        for (Resource r: Resource.values()){
            requirements.put(r,1);
        }
        setField(goldCard,"req",requirements);
        when(goldCard.getReq()).thenReturn(requirements);
        try {
            hand.playCard(startingCard,40,40);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertThrows(WrongPlayException.class, ()-> hand.playCard(goldCard,x,y));
    }

    @Test
    public void testPlayCardCorrectlyOnAFaceUpCard(){
        Corner[] corners= new Corner[]{cornerVisible, cornerVisible,cornerVisible,cornerVisible};
        setField(startingCard,"frontCorners", corners);
        int x=39;
        int y=39;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=41;
        y=39;
        hand.getHandCards()[0]=goldCard;
        HashMap<Resource,Integer> requirements=new HashMap<>();
        for (Resource r: Resource.values()){
            requirements.put(r,0);
        }
        setField(goldCard,"req",requirements);
        when(goldCard.getReq()).thenReturn(requirements);
        try {
            hand.playCard(goldCard,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(goldCard,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=39;
        y=41;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=41;
        y=41;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);
    }

    @Test
    public void testPlayCardCorrectlyOnAFaceDownCard(){
        Corner[] corners= new Corner[]{cornerVisible, cornerVisible,cornerVisible,cornerVisible};
        setField(startingCard,"backCorners", corners);
        startingCard.isFacedown=true;
        int x=39;
        int y=39;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=41;
        y=39;
        hand.getHandCards()[0]=goldCard;
        HashMap<Resource,Integer> requirements=new HashMap<>();
        for (Resource r: Resource.values()){
            requirements.put(r,0);
        }
        setField(goldCard,"req",requirements);
        when(goldCard.getReq()).thenReturn(requirements);
        try {
            hand.playCard(goldCard,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(goldCard,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=39;
        y=41;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);

        x=41;
        y=41;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,40,40);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
        assertNull(hand.getHandCards()[0]);
    }


    @Test
    public void testPlayCardEdgePosition(){
        Corner[] corners= new Corner[]{cornerVisible, cornerVisible,cornerVisible,cornerVisible};
        setField(startingCard,"frontCorners", corners);
        int x=0;
        int y=0;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,1,1);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);

        x=80;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,79,1);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);


        y=80;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,79,79);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);

        x=0;
        hand.getHandCards()[0]=card;
        try {
            hand.playCard(startingCard,1,79);
            hand.playCard(card,x,y);
        } catch (WrongPlayException e) {
            fail("SHOULD NOT THROW: "+e.message);
        }
        assertEquals(card,hand.getDisplayedCards()[x][y]);
    }
}