package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Model.*;

import java.io.*;
import java.util.*;

import static it.polimi.ingsw.Model.Position.*;
import static java.lang.String.join;

public class TUI extends UI {

    private final Queue<String> lastInputs = new LinkedList<>();
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final PrintStream out = new PrintStream(System.out, true);
    private final PrintStream outErr = new PrintStream(System.err, true);
    private Object lock_events = new Object();
    private Object lock_chat = new Object();

    public TUI(ClientImpl client) {
        super(client);
    }

    // This method allows to receive the user's choice between min and max included.
    // It runs until a proper answer isn't given.
    // WATCH OUT! This method is meant only for inputs. The proper output must be implemented elsewhere
    //                                                  (e.g. let the user know the mapping between min:max and choices)
    private final int chooseInt(int min, int max){
        boolean isValid = false;
        String choice = null;
        int intChoice = 0;

        while(!isValid){
            synchronized (lastInputs){
                lastInputs.clear();

                while(lastInputs.isEmpty()){
                    try{
                        lastInputs.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

                choice = lastInputs.poll();
            }

            if(listenToChat(choice))    continue;

            // Checks if the input is a chat message and if yes, it sends it.
            try{
                intChoice = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                printOut(inputError());

                continue;
            }

            if(intChoice >= min && intChoice <= max)  isValid = true;

            if(!isValid)    printOut(inputError());
        }

        return intChoice;
    }

    public final void notifyListener(GenericEvent e) {
        listener.addEvent(e);
    }

    public final static void clearConsole(){
        try{
            final String so = System.getProperty("os.name");

            if(so.contains("Windows"))  Runtime.getRuntime().exec("cls");
            else                        Runtime.getRuntime().exec("clear");
        }
        catch (Exception ignored){}
    }

    public String chooseNickname() {
        return chooseString("nickname");
    }

    private final String chooseString(String s){
        boolean isValid = false;
        String tempString = null;

        printOut("\u001B[4m" + "Insert " + s + ":" + "\u001B[0m");

        while(!isValid){
            synchronized (lastInputs){
                lastInputs.clear();

                while(lastInputs.isEmpty()){
                    try{
                        lastInputs.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

                tempString = lastInputs.poll();
            }

            if(tempString != null && !tempString.contains(" ") && tempString.length() >= 4)
                isValid = true;
            else printOut(setColorForString("RED", "Invalid " + s + ". It must be at least 4 characters and can't contain spaces. Try again:", true));
        }

        return tempString.trim();
    }

    public final void printOut(String s){
        out.println(s);
    }

    public final void printErr(String err){
        outErr.println(err);
    }

    public void stop(){
        isActive = false;

        // Do the listeners have to be notified?
    }

    private static String inputError(){
        return "Input not allowed. Please try again";
    }

    private String setColorForBackground(String color, String string){
        String c;
        String reset = "\u001B[49m";
        switch (color){
            case "RED" -> c = "\u001B[41m";
            case "GREEN" -> c = "\u001B[42m";
            case "YELLOW" -> c = "\u001B[43m";
            case "BLUE" -> c = "\u001B[44m";
            case "PURPLE" -> c = "\u001B[45m";
            //case "CYAN" -> c = "\u001B[46m";
            //case "BLACK" -> c = "\u001B[40m";
            //case "WHITE" -> c = "\u001B[47m";
            default -> c = "<INVALID COLOR> ";
        }

        return c + string + reset;
    }

    private String setColorForString(String color, String string, boolean isBright){
        String c;
        String temp = isBright ? "9" : "3";
        String resetColor = "\u001B[0m";

        switch (color){
            case "RED" -> c = "\u001B[" + temp + "1m";
            case "GREEN" -> c = "\u001B[" + temp + "2m";
            case "YELLOW" -> c = "\u001B[" + temp + "3m";
            case "BLUE" -> c = "\u001B[" + temp + "4m";
            case "PURPLE" -> c = "\u001B[" + temp + "5m";
            //case "CYAN" -> c = "\u001B[" + temp + "6m";
            case "BLACK" -> c = "\u001B[" + temp + "0m";
            case "WHITE" -> c = "\u001B[" + temp + "7m";
            default -> c = "<INVALID COLOR> ";
        }

        return c + string + resetColor;
    }

    protected void printCard(Card card){
        if(card instanceof PlayableCard){
            printOut("\n| CARD NUMBER " + card.getID() + "'S DESCRIPTION:"
                    + "\n\tColor: " + setColorForString(card.getColor().toString(), card.getColor().toString(), true) +
                    "\n\tVisible corners:\n");
            for(Corner corner : card.getFrontCorners()){
                printOut("\t\t" + corner.getPosition() + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                        + ": "+ corner.getResource());
            }
            printOut("\tBACK:");
            for(Corner corner : card.getBackCorners()){
                printOut("\t\t" + corner.getPosition() + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                        + ": " + corner.getResource());
            }

            if(card instanceof GoldCard){
                printOut("Requirements:");
                for(Resource res : ((GoldCard) card).getReq().keySet()){
                    if(((GoldCard) card).getReq().get(res) > 0)
                        printOut(((GoldCard) card).getReq().get(res) + " " + res.toString());
                }
            }

            if(((PlayableCard) card).getPoints() == 0)  return;

            printOut("\nReward:\t" + ((PlayableCard) card).getPoints() + " points");
            if(card instanceof GoldCard && (((GoldCard) card).isRPointsCorner() == true || ((GoldCard) card).getRPoints() != null)){
                printOut("\t for every " + (((GoldCard) card).isRPointsCorner() ? "covered corner." : ((GoldCard) card).getRPoints().toString()) + ".");
            }
            printOut("The back of the card has four (all) empty corners with a resource in the center (of the corresponding color).");
        }
        else if(card instanceof StartingCard){
            printOut("\n| YOUR STARTING CARD'S DESCRIPTION:");
            printOut("VISIBLE CORNER:\n\tFRONT:");
            for(Corner corner : card.getFrontCorners()){
                if(!corner.getPosition().equals("empty"))
                    printOut("\t\t" + corner.getPosition() + ": "
                            + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                            + corner.getStringResource());
            }
            printOut("\n\tBACK:");
            for(Corner corner : card.getBackCorners()){
                if(!corner.getPosition().equals("empty"))
                    printOut("\t\t" + corner.getPosition() + ": "
                            + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                            + corner.getStringResource());
            }
            printOut("\n\tRESOURCES IN THE BACK:");
            for(Resource resource : ((StartingCard) card).resource){
                 printOut("\t\t" + resource + "\t\t");
            }
        }
    }

    protected void printCard(ObjectiveCard card){
        printOut("\n| OBJECTIVE CARD NUMBER " + card.getID() + "'S REQUESTS:");
        if(card instanceof ObjectiveCard1){
            printOut("\tPoints: " + card.getPoints() + "\n\tCards' disposition:");
            String grid = "\t\t";
            int index = 0;
            List<Integer> positions = Arrays.stream(((ObjectiveCard1) card).getRequiredPositions()).boxed().toList();

            for(int i = 1; index < 3; i++){
                if(positions.contains(i)){
                    grid += setColorForString(((ObjectiveCard1) card).getCardColors()[index].toString(), "■", false);
                    index++;
                }
                else grid += "  ";

                if(i == 3 || i == 6)    grid += "\n\t\t";

                // Shouldn't happen.
                if(i > 9)   printOut("\nERRORE DI STAMPA.\n");
            }

            printOut(grid);
        }
        else{ // card is an ObjectiveCard2
            printOut("\tPoints: " + card.getPoints() + "\n\tVISIBLE RESOURCES:");
            for(Resource resource : ((ObjectiveCard2) card).getReqMap().keySet()){
                if(((ObjectiveCard2) card).getReqMap().get(resource) > 0)
                    printOut("\t\t " + ((ObjectiveCard2) card).getReqMap().get(resource) + " x " + resource.toString());
            }
        }
    }

    private void printGrid(Card[][] playedCards){
        int minRow = 40;
        int minColumn = 40;
        int maxRow = 40;
        int maxColumn = 40;
        final int GRID_MARGIN = 2;
        final String HORIZONTAL_SPACE = "\t";
        final String FAR_BLOCK = setColorForString("BLACK", "■", false);
        final String NEAR_BLOCK = setColorForString("YELLOW", "■", true);
        String grid = "";

        // Find the min and max of rows and columns used for a better view:
        // by doing so, every time it prints the used grid and not the entire one of the size of 80x80.
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                if(playedCards[i][j] != null){
                    if(i < minRow)    minRow    = i;
                    if(i > maxRow)    maxRow    = i;
                    if(j < minColumn) minColumn = j;
                    if(j > maxColumn) maxColumn = j;
                }
            }
        }

        // Renormalization of pathological cases:
        // if the margins are too big, it simply rescales the min and max of rows and columns.
        if(minRow    - GRID_MARGIN < 0)   minRow    =      GRID_MARGIN;
        if(minColumn - GRID_MARGIN < 0)   minColumn =      GRID_MARGIN;
        if(maxRow    + GRID_MARGIN >= 80) maxRow    = 80 - GRID_MARGIN - 1;
        if(maxColumn + GRID_MARGIN >= 80) maxColumn = 80 - GRID_MARGIN - 1;

        // It prints the column numbers.
        for(int j = minColumn - GRID_MARGIN - 40; j <= maxColumn + GRID_MARGIN - 40; j++)
            grid += HORIZONTAL_SPACE + j;

        // It prints the rest.
        for(int i = minRow - GRID_MARGIN; i <= maxRow + GRID_MARGIN; i++){
            // It prints the row numbers.
            grid += "\n" + (i - 40 < 0 ? "" : " ") + (i - 40);

            for(int j = minColumn - GRID_MARGIN; j <= maxColumn + GRID_MARGIN; j++){
                // If the current element is null, it checks possible adjacent cards and, if it finds at least one,
                // it marks this position with yellow, else with black.
                if(playedCards[i][j] == null){
                    if(checkNear(playedCards, i, j)){
                        grid += HORIZONTAL_SPACE + NEAR_BLOCK;
                    }
                    else{
                        grid += HORIZONTAL_SPACE + FAR_BLOCK;
                    }
                }
                // If the current element is not null, it simply prints the card's ID located here colored by the corresponding color.
                else{
                    grid += HORIZONTAL_SPACE + setColorForString(playedCards[i][j].getColor().toString(), String.valueOf(playedCards[i][j].getID()), true);
                }
            }
        }

        printOut(grid + "\n");
    }

    private boolean checkNear(Card[][] playedCards, int x, int y){
        if(x-1 >=  0 && y-1 >=  0 && playedCards[x-1][y-1] != null && !playedCards[x-1][y-1].getCorners()[3].getPosition().equals("empty")) return true;
        if(x-1 >=  0 && y+1 <= 80 && playedCards[x-1][y+1] != null && !playedCards[x-1][y+1].getCorners()[2].getPosition().equals("empty")) return true;
        if(x+1 >=  0 && y-1 >=  0 && playedCards[x+1][y-1] != null && !playedCards[x+1][y-1].getCorners()[1].getPosition().equals("empty")) return true;
        if(x+1 <= 80 && y+1 <= 80 && playedCards[x+1][y+1] != null && !playedCards[x+1][y+1].getCorners()[0].getPosition().equals("empty")) return true;

        return false;
    }

    // It returns true if the string is a chat message, and it also sends it.
    private boolean listenToChat(String string){
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(string.split(" ")));

        // If not, the chat message has not the correct format in order to be sent.
        if(words.get(0).equals("CHAT") && words.size() > 1){
            boolean isForEveryone = false;
            String recipient = null;

            // Private chat mode.
            if(words.get(1).equals("P") && words.size() > 2){
                recipient = words.get(2);
                words.remove(2);
                words.remove(1);
            }
            else{
                recipient = "everyone";
                isForEveryone = true;
            }

            words.remove(0);

            listener.addEvent(new ChatMessage(join(" ", words), client.getNickname(), recipient, isForEveryone));

            return true;
        }

        return false;
    }

    public final void update(GenericEvent e){
        if(e instanceof ChatMessage){
            synchronized (lock_chat){
                chatMessages.add((ChatMessage) e);
            }
        }
        else{
            synchronized(lock_events){
                inputEvents.add(e);
            }
        }
    }

    @Override
    public void run() {

        clearConsole();

        // Chat's input
        new Thread(){
            @Override
            public void run() {
                while(isActive){
                    String s = null;
                    try {
                        s = in.readLine();
                        synchronized(lastInputs){
                            if(!listenToChat(s)){
                                lastInputs.add(s);
                                lastInputs.notifyAll();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }.start();

        // Chat's output
        new Thread(){
            @Override
            public void run() {
                while(isActive){
                    ChatMessage msg = null;
                    synchronized(lock_chat){
                        if (!chatMessages.isEmpty()){
                            msg = chatMessages.poll();

                            if((msg instanceof ChatAck) || !msg.nickname.equals(client.getNickname()))
                                printOut(msg.msgOutput());
                        }
                    }
                }
            }
        }.start();

        // Event's handling thread
        new Thread(){
            @Override
            public void run() {
                while(isActive){
                    GenericEvent ev = null;

                    synchronized(lock_events){
                        if(inputEvents.isEmpty())   continue;

                        ev = inputEvents.poll();
                    }

                    // Ignore all other player's events
                    if(!ev.mustBeSentToAll && !ev.nickname.equals(client.getNickname())) continue;

                    int n;

                    clearConsole();

                    if((ev instanceof AckResponse) || !(ev instanceof GenericResponse)) printOut(ev.msgOutput());

                    switch(ev){
                        case DrawCardRequest e :
                            notifyListener(new DrawCardResponse(chooseInt(1,2),client.getNickname()));
                            break;

                        case ErrorJoinLobby e :
                            printOut("Do you want to try to connect again?\n" +
                                    "(1) for yes, (2) for no:");
                            n = chooseInt(1,2);
                            if(n == 1)  notifyListener(new ClientRegister(client));
                            else        System.exit(1);
                            break;

                        case ChooseObjectiveRequest e :
                            printCard(e.objCard1);
                            printCard(e.objCard2);
                            printOut(e.msgOutput2());
                            notifyListener(new ChooseObjectiveResponse(e.getChosenCard(chooseInt(1,2)), client.getNickname()));
                            break;

                        case NumPlayersRequest e :
                            notifyListener(new NumPlayersResponse(chooseInt(2,4), client.getNickname()));
                            break;

                        case PlayCardRequest e :
                            printOut("YOUR PLAYED CARDS:");
                            printGrid(e.playerView.hand.playedCards);

                            printOut("YOUR HAND:");
                            for(Card card : e.playerView.hand.handCards){
                                printCard(card);
                            }

                            n = chooseInt(1,3);

                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.playerView.hand.handCards[n-1].isFacedown = true;

                            printOut(e.msgOutput3());
                            notifyListener(new PlayCardResponse(client.getNickname(), e.playerView.hand.handCards[n-1], 40 + chooseInt(-40, 40), 40 + chooseInt(-40, 40)));
                            break;

                        case SetTokenColorRequest e :
                            n = -1;
                            printOut(e.msgOutput2());
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,4);
                            }while(!e.choiceIsValid(n));

                            notifyListener(new SetTokenColorResponse(n, client.getNickname()));
                            break;

                        case JoinLobby e :
                            if(e.getNewNickname() != null)  client.setNickname(e.getNewNickname());

                            notifyListener(new SetPassword(client.getNickname(), chooseString("password")));
                            break;

                        case PlaceStartingCard e :
                            printCard(e.startingCard);
                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.startingCard.isFacedown = true;

                            notifyListener(new PlaceStartingCard( e.startingCard, client.getNickname()));
                            break;

                        case ReconnectionRequest e:
                            notifyListener(new ReconnectionResponse( client.getNickname(),chooseString("password")));
                            break;

                        default :
                            break;
                    }
                }
            }
        }.start();
    }
}
