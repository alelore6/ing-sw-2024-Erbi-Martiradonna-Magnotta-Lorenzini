package it.polimi.ingsw.Events;

/**
 * Event that represent the successful join of the pre-game lobby of a player
 */
public class JoinLobby extends GenericRequest{
    /**
     * the player's new nickname if the one he has chosen was already taken
     */
    private final String newNickname;

    /**
     * Constructor
     * @param oldNickname the player chosen nickname that has joined the lobby
     * @param newNickname the nickname of the player after connecting. It's different only if chosen nickname was already present
     */
    public JoinLobby(String oldNickname, String newNickname){
        super("You have joined a lobby, waiting for other players to start the game...\n" +
                "Set a password so that you can reconnect to this game in case of disconnection. \n (min. 4 characters and no space allowed):", oldNickname);
        this.newNickname = newNickname;
    }
    /**
     * Getter for the event message in a cli friendly format
     * @return the message
     */
    @Override
    public String msgOutput(){
        return newNickname.equals(nickname) ? super.msgOutput() : "\u001B[4m" + "Since the nickname '" + nickname + "' is already taken," +
                "your new one is " + "\u001B[1m" + newNickname + "\u001B[0m" + ".\n" + super.msgOutput();
    }

    /**
     * Getter for the new nickname. If it hasn't changed return the old nickname
     * @return the new nickname
     */
    public String getNewNickname(){
        return newNickname;
    }
}
