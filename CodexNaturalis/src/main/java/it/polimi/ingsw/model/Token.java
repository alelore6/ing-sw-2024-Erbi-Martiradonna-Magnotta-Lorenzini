package it.polimi.ingsw.model;

public class Token {

    private int color;
    private int scoreTrackPos;

    private Scoretrack scoretrack;

    private Player player;
    public Token(int color, int scoreTrackPos, Scoretrack scoretrack, Player player) {
        this.color = color;
        this.scoreTrackPos = 0;
        this.scoretrack = scoretrack;
        this.player = player;

    }

    public int getColor() {
        return color;
    }

    public int getScoreTrackPos() {
        return scoreTrackPos;
    }

    public Scoretrack getScoretrack() {
        return scoretrack;
    }

    public Player getPlayer() {
        return player;
    }

    public void move(int points){
        if(scoreTrackPos + points <= 29) {
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[scoreTrackPos + points] += 1;
        }
        else{
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[29] += 1;
        }
    }
}

