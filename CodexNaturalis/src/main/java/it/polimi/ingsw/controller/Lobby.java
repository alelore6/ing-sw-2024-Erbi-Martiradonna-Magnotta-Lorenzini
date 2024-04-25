package it.polimi.ingsw.controller;

public class Lobby {
    protected int numPlayers=0;
    protected String[] Nicknames;

    protected boolean addPlayer(String nickname){
        if (Nicknames==null){
            this.Nicknames=new String[numPlayers];
            Nicknames[0]=nickname;
            return true;
        }
        for(int i=0; i<numPlayers; i++){
            if (Nicknames[i]==null){
                Nicknames[i]=nickname;
                return true;
            }
        }
        return false;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
