package it.polimi.ingsw.controller;

public class Lobby {
    protected int numPlayers;
    protected String[] Nicknames=new String[4];

    protected boolean addFirstPlayer(int numPlayers, String nickname){
        if (Nicknames[0] == null) {
            this.numPlayers = numPlayers;
            Nicknames[0] = nickname;
            return true;
        }
        return false;
    }
    protected boolean addPlayer(String nickname){
        for(int i=0; i<numPlayers; i++){
            if (Nicknames[i]==null){
                Nicknames[i]=nickname;
                return true;
            }
        }
        return false;
    }
}
