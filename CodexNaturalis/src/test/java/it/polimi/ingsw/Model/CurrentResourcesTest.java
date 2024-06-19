package it.polimi.ingsw.Model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CurrentResourcesTest {

    private CurrentResources currentResources;
    private Player player;
    private Token token;
    private StartingCard startingCard;
    private GoldCard goldCard;
    private Corner cornerNotVisible;
    private Corner cornerEmpty;
    private Corner cornerAnimal;
    private Corner cornerFungi;
    private Corner cornerInsect;
    private Corner cornerPlant;
    private Corner cornerQuill;

    @Before
    public void setUp() {
        //viene eseguito prima di ogni test

        //creo classi mock
        player = mock(Player.class);
        goldCard = mock(GoldCard.class);
        token= mock(Token.class);
        cornerNotVisible = mock(Corner.class);
        startingCard = mock(StartingCard.class);
        cornerAnimal = mock(Corner.class);
        cornerFungi = mock(Corner.class);
        cornerInsect = mock(Corner.class);
        cornerPlant = mock(Corner.class);
        cornerQuill = mock(Corner.class);
        cornerEmpty= mock(Corner.class);

        //tranne quella che devo testare
        currentResources = new CurrentResources(player);

        //attributi pubblici li setto direttamente
        cornerNotVisible.pos=null;
        cornerAnimal.pos=Position.UP_DX;
        cornerFungi.pos=Position.UP_DX;
        cornerPlant.pos=Position.UP_DX;
        cornerInsect.pos=Position.UP_DX;
        cornerQuill.pos=Position.UP_DX;
        cornerEmpty.pos=Position.UP_DX;

        startingCard.frontCorners=new Corner[]{cornerPlant, cornerAnimal, cornerInsect, cornerFungi};
        startingCard.backCorners=new Corner[]{cornerNotVisible, cornerAnimal, cornerEmpty, cornerFungi};
        startingCard.resource= new Resource[]{Resource.ANIMAL,Resource.FUNGI};

        goldCard.frontCorners=new Corner[]{cornerQuill, cornerAnimal, cornerNotVisible, cornerFungi};
        goldCard.backCorners=new Corner[]{cornerNotVisible, cornerNotVisible, cornerEmpty, cornerNotVisible};

        //per metodi getter imposto il valore di ritorno
        when(player.getToken()).thenReturn(token);
        when(goldCard.getColor()).thenReturn(CardColor.BLUE);
        when(cornerAnimal.getResource()).thenReturn(Resource.ANIMAL);
        when(cornerFungi.getResource()).thenReturn(Resource.FUNGI);
        when(cornerPlant.getResource()).thenReturn(Resource.PLANT);
        when(cornerInsect.getResource()).thenReturn(Resource.INSECT);
        when(cornerQuill.getResource()).thenReturn(Resource.QUILL);
        when(cornerEmpty.getResource()).thenReturn(null);
        //posso farlo anche dentro ogni test per fare i diversi casi
    }

    //per settare campi privati senza setter
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testUpdateStartingCardFaceDown() {
        startingCard.isFacedown=true;
        currentResources.update(startingCard, null);
        assertEquals((Integer) 2,currentResources.currentResources.get(Resource.ANIMAL));
        assertEquals((Integer) 2,currentResources.currentResources.get(Resource.FUNGI));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.INSECT));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.PLANT));
    }

    @Test
    public void testUpdateStartingCardFaceUp() {
        startingCard.isFacedown=false;
        currentResources.update(startingCard, null);
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.ANIMAL));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.FUNGI));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.INSECT));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.PLANT));
    }


    @Test
    public void testUpdateCardFaceUp() {
        goldCard.isFacedown=false;
//        when(goldCard.getRPoints()).thenReturn(Resource.QUILL);
        currentResources.update(goldCard, null);
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.ANIMAL));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.FUNGI));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.INSECT));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.PLANT));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.QUILL));

    }

    @Test
    public void testUpdateCardFaceDown() {
        goldCard.isFacedown=true;

        //blue in setup
        currentResources.update(goldCard, null);

        when(goldCard.getColor()).thenReturn(CardColor.RED);
        currentResources.update(goldCard, null);

        when(goldCard.getColor()).thenReturn(CardColor.GREEN);
        currentResources.update(goldCard, null);

        when(goldCard.getColor()).thenReturn(CardColor.PURPLE);
        currentResources.update(goldCard, null);

        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.ANIMAL));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.FUNGI));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.INSECT));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.PLANT));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.QUILL));

    }

    @Test
    public void testUpdateOverlaps(){
        currentResources.currentResources.replace(Resource.ANIMAL,currentResources.currentResources.get(Resource.ANIMAL)+1);
        currentResources.currentResources.replace(Resource.QUILL,currentResources.currentResources.get(Resource.QUILL)+1);
        currentResources.currentResources.replace(Resource.FUNGI,currentResources.currentResources.get(Resource.FUNGI)+1);

        Corner[] overlaps = new Corner[]{cornerQuill, cornerAnimal, cornerEmpty, null};
        goldCard.frontCorners=new Corner[]{cornerEmpty, cornerEmpty, cornerNotVisible, cornerNotVisible};


        currentResources.update(goldCard, overlaps);

        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.ANIMAL));
        assertEquals((Integer) 1,currentResources.currentResources.get(Resource.FUNGI));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.INSECT));
        assertEquals((Integer) 0,currentResources.currentResources.get(Resource.QUILL));
    }


    @Test
    public void testUpdateResourcePoints(){
        when(goldCard.getRPoints()).thenReturn(Resource.QUILL);
        when(goldCard.getPoints()).thenReturn(1);
        goldCard.isFacedown=false;
        currentResources.getCurrentResources().replace(Resource.QUILL,currentResources.currentResources.get(Resource.QUILL)+3);


        //prende l'argomento di un metodo
        ArgumentCaptor<Integer> pointsCaptor = ArgumentCaptor.forClass(Integer.class);

        currentResources.update(goldCard, null);

        verify(token).move(pointsCaptor.capture());
        assertEquals((Integer) 4, pointsCaptor.getValue());
    }



    @Test
    public void testUpdateCornerPoints(){
        when(goldCard.isRPointsCorner()).thenReturn(true);
        when(goldCard.getPoints()).thenReturn(2);
        goldCard.isFacedown=false;

        Corner[] overlaps = new Corner[]{cornerQuill, cornerAnimal, cornerEmpty, null};

        //prende l'argomento di un metodo
        ArgumentCaptor<Integer> pointsCaptor = ArgumentCaptor.forClass(Integer.class);

        currentResources.update(goldCard, overlaps);

        verify(token).move(pointsCaptor.capture());
        assertEquals((Integer) 6, pointsCaptor.getValue());
    }

}
