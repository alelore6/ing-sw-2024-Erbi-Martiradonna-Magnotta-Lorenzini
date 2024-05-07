package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.WrongPlayException;
import org.junit.Test;

import static org.junit.Assert.*;
import org.mockito.Mockito;

public class PlayerTest {

    @Test
    public void getNickname() {
    }

    @Test
    public void getObjective() {
    PlayerTest instance = new PlayerTest();
    ObjectiveCard objinstance = new ObjectiveCard();
       ObjectiveCard objinstance = instance.getObjective();
       assertEquals(objinstance, objinstance);
    }

    @Test
    public void chooseObjective() {
        PlayerTest instance = new PlayerTest();
       ObjectiveCard objectiveCard = new ObjectiveCard();
      instance.chooseObjective();
      assertEquals(objectiveCard, objectiveCard);
    }

    @Test
    public void placeStartingCard() {
        PlayerTest instance = new PlayerTest();
        StartingCard startingCard = Mockito.mock(StartingCard.class);
        Mockito.when(Hand.playCard,40,40)).thenReturn();
    try{
        instance.placeStartingCard(startingCard);
    }catch(WrongPlayException e){
        fail("WrongPlayException");
    }
    }

    @Test
    public void getToken() {
        PlayerTest instance = new PlayerTest();
        Token token = Mockito.mock(Token.class);
        instance.getToken();
    }

    @Test
    public void getCurrentResources() {
        PlayerTest instance = new PlayerTest();
       CurrentResources currentResources = Mockito.mock(CurrentResources.class);
        Mockito.doReturn(getCurrentResources() currentResources);
        CurrentResources resources= instance.getCurrentResources();
        assertSame(currentResources,resources);
    }

    @Test
    public void getHand() {
        PlayerTest instance = new PlayerTest();
        Hand hand = new Hand();
        instance.setHand(hand);
        Hand testhand = instance.getHand();
        assertSame(hand,testhand);
    }

    @Test
    public void setToken() {
        PlayerTest instance = new PlayerTest();
        Token token = Mockito.mock(Token.class);
        instance.setToken(token);
         Token testtoken = instance.getToken();
         assertSame(token,testtoken);
    }


}