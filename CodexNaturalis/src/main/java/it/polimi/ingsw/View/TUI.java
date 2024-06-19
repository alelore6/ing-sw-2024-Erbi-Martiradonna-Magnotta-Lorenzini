package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.ModelView.HandView;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Model.Position.*;
import static java.lang.String.join;

public class TUI extends UI {

    private final Queue<String> lastInputs = new LinkedList<>();
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final PrintStream out = new PrintStream(System.out, true);
    private final PrintStream outErr = new PrintStream(System.err, true);
    private String firstPlayer = "";
    private Card[][] lastPlayedCards = null;
    private final ObjectiveCard[] publicObjCards = new ObjectiveCard[2];
    private boolean objBool = true;
    private ObjectiveCard privateObjectiveCard = null;
    private Object lock_events = new Object();
    private Thread TUIThread, commandThread;

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
                    }catch(InterruptedException ignored){}
                }

                choice = lastInputs.poll();
            }

            // Checks if the input is a chat message and if yes, it sends it.
                 if(listenToChat(choice))                  continue;
            // Checks if the input is a card print request and if yes, it sends it.
            else if(listenToCard(choice, lastPlayedCards)) continue;

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
            case "CYAN" -> c = "\u001B[46m";
            case "BLACK" -> c = "\u001B[40m";
            case "WHITE" -> c = "\u001B[47m";
            default -> c = "<INVALID COLOR> ";
        }

        return reset + c + string + reset;
    }

    public static String setColorForString(String color, String string, boolean isBright){
        String c;
        String temp = isBright ? "9" : "3";
        String resetColor = "\u001B[0m";

        switch (color){
            case "RED" -> c = "\u001B[" + temp + "1m";
            case "GREEN" -> c = "\u001B[" + temp + "2m";
            case "YELLOW" -> c = "\u001B[" + temp + "3m";
            case "BLUE" -> c = "\u001B[" + temp + "4m";
            case "PURPLE" -> c = "\u001B[" + temp + "5m";
            case "CYAN" -> c = "\u001B[" + temp + "6m";
            case "BLACK" -> c = "\u001B[" + temp + "0m";
            case "WHITE" -> c = "\u001B[" + temp + "7m";
            default -> c = "<INVALID COLOR> ";
        }

        return c + string + resetColor;
    }

    protected void printCard(Card card){
        if(card instanceof PlayableCard){
            printOut("| CARD NUMBER " + card.getID() + "'S DESCRIPTION:"
                    + "\n\tColor: " + setColorForString(card.getColor().toString(), card.getColor().toString(), false) +
                    "\n\tVisible corners:\n\t\tFRONT:");
            for(Corner corner : card.getFrontCorners()){
                if(corner.getPosition() != null && corner.isCovered == false)
                    printOut("\t\t\t" + corner.getPosition() + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                            + ": "+ corner.getStringResource());
            }
            printOut("\t\tBACK:");
            for(Corner corner : card.getBackCorners()){
                printOut("\t\t\t" + corner.getPosition() + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                        + ": " + corner.getStringResource());
            }

            if(card instanceof GoldCard){
                printOut("\tRequirements:");
                for(Resource res : ((GoldCard) card).getReq().keySet()){
                    if(((GoldCard) card).getReq().get(res) > 0)
                        printOut("\t\t" + ((GoldCard) card).getReq().get(res) + " x " + res.toString());
                }
            }

            if(((PlayableCard) card).getPoints() == 0)  return;

            printOut("\tReward:\t" + ((PlayableCard) card).getPoints() + " points" +
                    ((card instanceof GoldCard && (((GoldCard) card).isRPointsCorner() == true || ((GoldCard) card).getRPoints() != null)) ?
                    (" for every " + (((GoldCard) card).isRPointsCorner() ? "covered corner" : ((GoldCard) card).getRPoints().toString())) : ""));

            // printOut("The back of the card has four (all) empty corners with a resource in the center (of the corresponding color).");
        }
        else if(card instanceof StartingCard){
            printOut("\n| YOUR STARTING CARD'S DESCRIPTION:");
            printOut("Visible corners:\n\tFRONT:");
            for(Corner corner : card.getFrontCorners()){
                if(corner.getPosition() != null)
                    printOut("\t\t" + corner.getPosition() + ": "
                            + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                            + corner.getStringResource());
            }
            printOut("\n\tBACK:");
            for(Corner corner : card.getBackCorners()){
                if(corner.getPosition() != null)
                    printOut("\t\t" + corner.getPosition() + ": "
                            + (corner.getPosition().equals(UP_SX.toString()) || corner.getPosition().equals(UP_DX.toString()) ? "  " : "")
                            + corner.getStringResource());
            }
            printOut("\n\tRESOURCES IN THE BACK:");
            for(Resource resource : ((StartingCard) card).resource){
                 printOut("\t\t" + resource.toString() + "\t\t");
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
                if(i > 9)   printErr("\nPRINT ERROR.\n");
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

    private void requestCard(int ID, Card[][] playedCards){
        if(playedCards == null){
            printErr("You can't see the card: you haven't even started the game!");
            return;
        }
        if(publicObjCards[0] == null){
            printErr("Can't see the card yet. Wait the start of your game!");
            return;
        }
        if(publicObjCards[0].getID() == ID){
            printCard(publicObjCards[0]);
            return;
        }
        if(publicObjCards[1].getID() == ID){
            printCard(publicObjCards[1]);
            return;
        }

        Optional<Card> cardRequested = Arrays.stream(playedCards).flatMap(Arrays::stream).filter(card -> card != null && card.getID() == ID).findFirst();

        if(cardRequested.isPresent()) printCard(cardRequested.get());
        else printErr("You can't see the card: it's not present on your played cards.");
    }

    private void printGrid(Card[][] playedCards){
        int minRow = 100;
        int minColumn = 100;
        int maxRow = 0;
        int maxColumn = 0;
        final int size = playedCards.length;
        int center_row = -100;
        int center_column = -100;
        final int GRID_MARGIN = HandView.GRID_MARGIN;
        final String HORIZONTAL_SPACE = "\t";
        final String FAR_BLOCK = setColorForString("BLACK", "■", false);
        final String NEAR_BLOCK = setColorForString("YELLOW", "■", true);
        String grid = "";

        // Find the min and max of rows and columns used for a better view:
        // by doing so, every time it prints the used grid and not the entire one of the size of 80x80.
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(playedCards[i][j] != null && playedCards[i][j].getID() != -1){
                    if(i - GRID_MARGIN < minRow)    minRow    = i - GRID_MARGIN;
                    if(i + GRID_MARGIN > maxRow)    maxRow    = i + GRID_MARGIN;
                    if(j - GRID_MARGIN < minColumn) minColumn = j - GRID_MARGIN;
                    if(j + GRID_MARGIN > maxColumn) maxColumn = j + GRID_MARGIN;
                }
            }
        }

        // Renormalization of pathological cases:
        // if the margins are too big, it simply rescales the min and max of rows and columns.
        if(minRow    < 0)  minRow    =  0;
        if(minColumn < 0)  minColumn =  0;
        if(maxRow    > 80) maxRow    = 80;
        if(maxColumn > 80) maxColumn = 80;

        // This finds the starting card's coordinates.
        for(int i = minRow; i <= maxRow; i++){
            boolean isFound = false;
            for(int j = minColumn; j <= maxColumn; j++){
                if(playedCards[i][j] instanceof StartingCard && playedCards[i][j].getID() != -1){
                    center_row = i;
                    center_column = j;

                    isFound = true;
                    break;
                }
                if(isFound) break;
            }
        }

        // It prints the column numbers.
        for(int j = minColumn; j <= maxColumn; j++)
            grid += HORIZONTAL_SPACE + (j - center_column);

        // It prints the rest.
        for(int i = minRow; i <= maxRow; i++){
            // It prints the row numbers.
            grid += "\n" + ((i - center_row) < 0 ? "" : " ") + (i - center_row);

            for(int j = minColumn; j <= maxColumn; j++){
                boolean isOut = i < 0 || j < 0 || i >= size || j >= size;
                // If the current element is null, it checks possible adjacent cards and, if it finds at least one,
                // it marks this position with yellow, else with black.
                if(isOut || playedCards[i][j] == null)
                    grid += HORIZONTAL_SPACE + FAR_BLOCK;
                else if(playedCards[i][j].getID() == -1)
                    grid += HORIZONTAL_SPACE + NEAR_BLOCK;

                // If the current element has a valid card, it simply prints the card's ID located here colored by the corresponding color.
                else{
                    grid += HORIZONTAL_SPACE + setColorForString(playedCards[i][j].getColor().toString(), String.valueOf(playedCards[i][j].getID()), true);
                }
            }
        }

        printOut(grid + "\n");
    }

    // It returns true if the string is a chat message, and it also sends it.
    private boolean listenToChat(String string){
        if(string == null)  return false;

        ArrayList<String> words = new ArrayList<String>(Arrays.asList(string.split(" ")));

        // If not, the chat message has not the correct format in order to be sent.
        if(words.get(0).equalsIgnoreCase("CHAT")){
            boolean isForEveryone = false;
            String recipient = null;

            // Private chat mode.
            if(words.size() > 3 && words.get(1).equalsIgnoreCase("P")){
                recipient = words.get(2);

                if(client.getNickname().equals(recipient)){
                    printErr("You can't send a message to yourself!");
                    return true;
                }
                if(words.get(3).equals("")){
                    printErr("The message is empty!");
                    return true;
                }

                words.remove(2);
                words.remove(1);
            }
            else if(words.size() == 1 || words.get(1).equals("")){
                printErr("The message is empty!");

                return true;
            }

            words.remove(0);

            listener.addEvent(new ChatMessage(join(" ", words), client.getNickname(), recipient));

            return true;
        }

        return false;
    }

    private boolean listenToCard(String string, Card[][] playedCards){
        if(string == null)  return false;

        ArrayList<String> words = new ArrayList<String>(Arrays.asList(string.split(" ")));
        int ID;

        if(words.get(0).equalsIgnoreCase("CARD") && words.size() > 1){
            try{
                ID = Integer.parseInt(words.get(1));
            } catch (NumberFormatException e) {
                printOut(inputError());

                return true;
            }
            if(ID < 1 || ID > 102){
                printErr("There's no card with such ID.");
                return true;
            }

            // If the player wants to print theirs objective card.
            if(privateObjectiveCard != null && ID == privateObjectiveCard.getID()){
                printCard(privateObjectiveCard);
                return true;
            }

            requestCard(ID, playedCards);

            return true;
        }

        return false;
    }

    private boolean listenToDisconnection(String command){

        return false;
    }

    public final void update(GenericEvent e){
        if(e instanceof ChatMessage){
            if(e instanceof ChatAck == e.nickname.equals(client.getNickname())){
                printOut(e.msgOutput());
            }
            // else ignored
        }
        else if(e instanceof FinalRankings){
            printOut(setColorForString("GREEN", e.msgOutput(), true));

            System.exit(0);
        }
        else if(e instanceof ServerMessage && (e.mustBeSentToAll = true || e.nickname == client.getNickname())){
            printOut(e.msgOutput());
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

        // Commands input
        commandThread = new Thread(){
            @Override
            public void run() {
                while(running){
                    String s = null;
                    try {
                        s = in.readLine();
                        synchronized(lastInputs){
                            if(!(listenToChat(s) || listenToCard(s, lastPlayedCards) || listenToDisconnection(s))){
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
        };

        commandThread.start();

        // Event's handling thread
        TUIThread = new Thread(){
            @Override
            public void run() {
                while(running){
                    GenericEvent ev = null;

                    synchronized(lock_events){
                        if(inputEvents.isEmpty())   continue;

                        ev = inputEvents.poll();
                    }

                    if(ev instanceof JoinLobby && !((JoinLobby) ev).getNewNickname().equals(client.getNickname()))
                        client.setNickname(((JoinLobby) ev).getNewNickname());
                    // Ignore all other player's events
                    else if(!ev.mustBeSentToAll && !ev.nickname.equals(client.getNickname())) continue;

                    int n = -1;

                    clearConsole();

                    if((ev instanceof AckResponse) || !(ev instanceof GenericResponse))
                        printOut(ev.msgOutput());

                    // If login fails.
                    if(ev instanceof AckResponse && ((AckResponse) ev).response instanceof ReconnectionResponse && ((AckResponse) ev).ok == false){
                        printErr("Exiting...");
                        System.exit(1);
                    }

                    switch(ev){
                        case DrawCardRequest e :
                            boolean[] presentCards = new boolean[4];
                            for(int i = 1; i <= e.tableCenterView.centerCards.length; i++){
                                if(e.tableCenterView.centerCards[i-1] != null){
                                    printOut("\n\n" + setColorForString("BLACK", setColorForBackground("YELLOW", "(" + i + ")"), false));
                                    printCard(e.tableCenterView.centerCards[i-1]);
                                    presentCards[i-1] = true;
                                }
                                else presentCards[i-1] = false;
                            }
                            printOut("\n\n" + setColorForString("BLACK", setColorForBackground("YELLOW", "(5)"), false) + " " +setColorForString(e.tableCenterView.topResourceCardColor.toString(), "Resource deck", false) + " (" + e.resCardinDeck + " card" + (e.resCardinDeck == 1 ? "" : "s") + " left).\n");
                                    printOut(setColorForString("BLACK", setColorForBackground("YELLOW", "(6)"), false) + " " + setColorForString(e.tableCenterView.topGoldCardColor.toString(), "Gold deck", false) + " (" + e.goldCardinDeck + " card" + (e.goldCardinDeck == 1 ? "" : "s") + " left).\n");

                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1, 6);
                            } while (n <= 4 && !presentCards[n-1]);

                            notifyListener(new DrawCardResponse(n,client.getNickname()));
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
                            n = chooseInt(1,2);
                            notifyListener(new ChooseObjectiveResponse(e.getChosenCard(n), client.getNickname()));
                            privateObjectiveCard = e.getChosenCard(n);
                            break;

                        case NumPlayersRequest e :
                            notifyListener(new NumPlayersResponse(chooseInt(2,4), client.getNickname()));
                            break;

                        case PlayCardRequest e :
                            String STATS_COLOR = "CYAN";
                            if(objBool){
                                publicObjCards[0] = e.tableView.objCards[0];
                                publicObjCards[1] = e.tableView.objCards[1];

                                objBool = false;
                            }
                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "CURRENT RANKINGS"), false));
                            n = 1;
                            Map<String, Integer> sortedMap = e.tableView.scoreTrack.points.entrySet()
                                    .stream()
                                    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                                            .thenComparing(Map.Entry.comparingByKey()))
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new
                                    ));
                            for(int i = 0; i < sortedMap.keySet().size(); i++){
                                String[] nicks = sortedMap.keySet().toArray(new String[sortedMap.size()]);
                                printOut(setColorForString("WHITE", n + ") " + nicks[i] + ": " + sortedMap.get(nicks[i]) + " point" + (sortedMap.get(nicks[i]) == 1 ? "" : "s"), sortedMap.get(nicks[i]) == sortedMap.get(nicks[0]) ? true : false));
                                n++;
                            }
                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "YOUR SECRET OBJECTIVE CARD's ID"), false) + "\n" + privateObjectiveCard.getID());
                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "PUBLIC OBJECTIVE CARDS"), false) + "\n" + publicObjCards[0].getID() + ", " + publicObjCards[1].getID());
                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "YOUR CURRENT RESOURCES"), false));
                            boolean isAny = false;
                            for(Resource resource : e.playerView.currentResources.keySet()){
                                if(e.playerView.currentResources.get(resource) > 0){
                                    printOut("\t" + resource.toString() + " x " + e.playerView.currentResources.get(resource));
                                    isAny = true;
                                }
                            }

                            if(!isAny)  printOut("\tnone");

                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "YOUR PLAYED CARDS"), false));
                            printGrid(e.playerView.hand.playedCards);

                            printOut(setColorForString("BLACK", setColorForBackground(STATS_COLOR, "YOUR HAND"), false));
                            for(Card card : e.playerView.hand.handCards){
                                printCard(card);
                                printOut("\n");
                            }

                            lastPlayedCards = e.playerView.hand.playedCards;

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
                                n = chooseInt(1, 4);
                            } while (!e.choiceIsValid(n));

                            notifyListener(new SetTokenColorResponse(n, client.getNickname()));
                            break;

                        case JoinLobby e :

                            notifyListener(new SetPassword(client.getNickname(), chooseString("password")));
                            break;

                        case PlaceStartingCard e :
                            printCard(e.startingCard);
                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.startingCard.isFacedown = true;

                            notifyListener(new PlaceStartingCard( e.startingCard, client.getNickname()));
                            printOut("\nWaiting for other players...");
                            break;

                        case TurnOrder e :
                            firstPlayer = e.order.split(" ")[3];
                            break;

                        case ReconnectionRequest e:
                            notifyListener(new ReconnectionResponse( client.getNickname(),chooseString("password")));
                            break;

                        default :
                            break;
                    }
                }
            }
        };

        TUIThread.start();
    }

    public void stop(){
        running = false;
        try{
            in.close();
        }catch (IOException ignored){}
        commandThread.interrupt();
        TUIThread.interrupt();
    }
}
