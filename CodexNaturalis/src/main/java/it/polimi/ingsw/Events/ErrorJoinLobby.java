package it.polimi.ingsw.Events;

public class ErrorJoinLobby extends GenericEvent {

    public ErrorJoinLobby(String nickname) {
        super("Cannot join a lobby: game has already started or lobby is full.", nickname);
    }
}
