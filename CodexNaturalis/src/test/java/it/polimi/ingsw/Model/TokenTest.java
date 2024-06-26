package it.polimi.ingsw.Model;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenTest {
    private TokenColor color;

    private int scoreTrackPos;

    private Scoretrack scoretrack;

    private Player player;

    private Token token;

    private Game testGame;

    @Before
    public void setUp() throws Exception {
        color = TokenColor.BLUE;
        scoreTrackPos = 0;
        scoretrack = mock(Scoretrack.class);
        player = mock(Player.class);

        token = new Token(color, scoretrack, player);

        testGame = mock(Game.class);

        player.game = testGame;
    }

    @Test
    public void move() {

        token.getColor();
        token.getScoreTrackPos();
        token.getScoretrack();
        token.getPlayer();


        int[] pos = new int[30];
        scoretrack.points = new HashMap<>();

        // Aggiungiamo il token alla mappa points
        scoretrack.points.put(token, scoreTrackPos);

        when(scoretrack.getTokenPos()).thenReturn(pos);


        player.game.isTriggered = true;
        token.move(1);
        token.move(30);

        player.game.isTriggered = false;
        scoreTrackPos = 30;
        token.move(2);


    }
}