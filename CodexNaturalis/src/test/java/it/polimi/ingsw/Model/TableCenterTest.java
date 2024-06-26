package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.isEmptyException;
import it.polimi.ingsw.Listeners.ModelViewListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TableCenterTest {

    private Game testGame;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private ObjectiveDeck objectiveDeck;
    private ObjectiveCard[] objectiveCards;

    private PlayableCard[] centerCards;
    private Scoretrack scoretrack;
    private TableCenter tableCenter;




    @Before
    public void setUp() throws Exception {
         testGame = mock(Game.class);
         resourceDeck = mock(ResourceDeck.class);
         goldDeck = mock(GoldDeck.class);
         objectiveDeck = mock(ObjectiveDeck.class);

        ObjectiveCard1 objectiveCard1 = mock(ObjectiveCard1.class);
        ObjectiveCard2 objectiveCard2 = mock(ObjectiveCard2.class);


         objectiveCards = new ObjectiveCard[]{objectiveCard1, objectiveCard2};

        ResourceCard playableCard1 = mock(ResourceCard.class);
        GoldCard playableCard2 = mock(GoldCard.class);
        ResourceCard playableCard3 = mock(ResourceCard.class);
        GoldCard playableCard4 = mock(GoldCard.class);

        centerCards = new PlayableCard[]{playableCard1, playableCard2, playableCard3, playableCard4};

        scoretrack = mock(Scoretrack.class);

        tableCenter = new TableCenter(resourceDeck, goldDeck, objectiveDeck, testGame);
    }

    @Test
    public void drawAndPosition() throws isEmptyException {
        tableCenter.getCenterCards();
        tableCenter.getScoretrack();
        tableCenter.getGoldDeck();
        tableCenter.getResDeck();
        tableCenter.getObjDeck();


        testGame.setCurPlayerPosition(0);
        testGame.tablecenter = tableCenter;

        ArrayList<ModelViewListener> mvListeners = new ArrayList<>();
        ModelViewListener mvListener1 = mock(ModelViewListener.class);
        ModelViewListener mvListener2 = mock(ModelViewListener.class);
        ModelViewListener mvListener3 = mock(ModelViewListener.class);
        ModelViewListener mvListener4 = mock(ModelViewListener.class);

        mvListener1.nickname = "Alidoro";
        mvListener2.nickname = "Pulcinella";
        mvListener3.nickname = "Arlecchino";
        mvListener4.nickname = "Pinocchio";

        mvListeners.add(mvListener1);
        mvListeners.add(mvListener2);
        mvListeners.add(mvListener3);
        mvListeners.add(mvListener4);

        GoldCard missingCard = mock(GoldCard.class);

        tableCenter.setCenterCards(centerCards);
        tableCenter.drawAndPosition(missingCard);


        tableCenter.drawAndPosition(centerCards[0]);
        tableCenter.drawAndPosition(centerCards[1]);

        when(resourceDeck.getNCards()).thenReturn(0);
        when(goldDeck.getNCards()).thenReturn(0);
        when(resourceDeck.draw()).thenThrow(isEmptyException.class);
        when(goldDeck.draw()).thenThrow(isEmptyException.class);

        tableCenter.drawAndPosition(centerCards[2]);
        tableCenter.drawAndPosition(centerCards[3]);


    }
}