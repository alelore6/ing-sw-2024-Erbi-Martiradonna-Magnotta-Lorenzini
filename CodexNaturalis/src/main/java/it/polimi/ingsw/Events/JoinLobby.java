package it.polimi.ingsw.Events;

public class JoinLobby extends GenericEvent{
    JoinLobby(String nickname){
        super("You have joined a lobby. Waiting for other players to start the game", nickname);
    }
}
