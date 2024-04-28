package it.polimi.ingsw.Messages;

public enum Events {
    //enum of all event type that can be exchanged between client and server
    JoinServer,
    JoinLobby,
    SetNumPlayers,
    StartGame,
    ChooseObjective,
    RotateCard,
    YourTurn,
    PlayCard,
    DrawCard,
    NextPlayer,
    Waiting,
    EndGameTriggered,
    Winner
}
