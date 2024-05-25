package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Model.*;

import java.io.PrintStream;
import java.util.*;

public class TUI extends UI {

    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = new PrintStream(System.out, true);
    private final PrintStream outErr = new PrintStream(System.err, true);
    private Object lock_events = new Object();

    public TUI(ClientImpl client) {
        super(client);
    }

    // This method allows to receive the user's choice between min and max included.
    // It runs until a proper answer isn't given.
    // WATCH OUT! This method is meant only for inputs. The proper output must be implemented elsewhere
    //                                                  (e.g. let the user know the mapping between min:max and choices)
    private final int chooseInt(int min, int max){
        int     choice  = 0;
        boolean isValid = false;

        while(!isValid){
            try{
                choice = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) {
                // to skip the wrong input and try with the next one.
                in.nextLine();
                printOut(inputError());

                continue;
            }

            try{
                if(choice >= min && choice <= max)  isValid = true;
            }catch(InputMismatchException e){

            }finally {
                if(!isValid)    printOut(inputError());
            }
        }

        return choice;
    }

    public final void notifyListener(GenericEvent e) {
        listener.addEvent(e);
    }

    public final static void clearConsole(){
        try{
            final String so = System.getProperty("os.name");

            if (so.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (Exception ignored){

        }
    }

    public String chooseNickname() {
        return chooseString("nickname");
    }

    private final String chooseString(String s){
        boolean isValid = false;
        String tempString = null;

        printOut("Insert " + s + ": ");

        while(!isValid){
            tempString = in.next();

            if(tempString != null && !tempString.contains(" ") && tempString.length() >= 4)
                isValid = true;
            else printOut("Invalid " + s + ". It must be at least 4 characters and can't contain spaces. Try again:");
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


    private String setColorForString(String color, String string){
        String c;
        String resetColor = "\u001B[0m";
        switch (color){
            case "RED" -> c = "\u001B[31m";
            case "GREEN" -> c = "\u001B[32m";
            case "YELLOW" -> c = "\u001B[33m";
            case "BLUE" -> c = "\u001B[34m";
            case "PURPLE" -> c = "\u001B[35m";
            //case "CYAN" -> c = "\u001B[36m";
            //case "BLACK" -> c = "\u001B[30m";
            //case "WHITE" -> c = "\u001B[37m";
            default -> c = "<INVALID COLOR> ";
        }

        return c + string + resetColor;
    }

    protected void printCard(Card card){
        if(card instanceof PlayableCard){
            printOut("\n| CARD NUMBER " + card.getID() + "'S DESCRIPTION:");
            printOut("Color: " + setColorForString(((PlayableCard) card).getColor().toString(), ((PlayableCard) card).getColor().toString()) +
                        "\nVisible corners: ");
            Arrays.stream(card.getCorners()).forEach(corner -> printOut(
                    corner.getPosition() + ": " + corner.getResource()));

            if(card instanceof GoldCard){
                // the number of total distinct resources
                int NUM_RES = 7;

                printOut("Requirements:");
                for(Resource res : ((GoldCard) card).getReq().keySet()){
                    if(((GoldCard) card).getReq().get(res) > 0)
                        printOut(((GoldCard) card).getReq().get(res) + res.toString());
                }
            }

            if(((PlayableCard) card).getPoints() == 0)  return;

            printOut("\nReward:\t" + ((PlayableCard) card).getPoints() + "points");
            if(card instanceof GoldCard){
                printOut("\t for every " + (((GoldCard) card).isRPointsCorner() ? "covered corner." : ((GoldCard) card).getRPoints().toString()) + ".");
            }
            printOut("The back of the card has four (all) empty corners with a resource in the center (of the corresponding color).");
        }
        else if(card instanceof StartingCard){
            printOut("\n| YOUR STARTING CARD'S DESCRIPTION:");
            printOut("VISIBLE CORNER:\n\tFRONT:");
            for(int i = 0; i < card.getCorners().length; i++){
                if(i == 4) printOut("\tBACK:");
                if(card.getCorners()[i] != null)
                    printOut("\t\t" + card.getCorners()[i].getPosition() + ": " + card.getCorners()[i].getStringResource());
            }
            printOut("\tRESOURCES IN THE BACK:");
            for(Resource resource : ((StartingCard) card).resource){
                 printOut("\t\t" + resource + "\t\t");
            }
        }
    }

    protected void printCard(ObjectiveCard card){
        printOut("\n| CARD NUMBER " + card.getID() + "'S DESCRIPTION:");
        if(card instanceof ObjectiveCard1){
            String grid = "";
            int index = 0;
            List<Integer> positions = Arrays.stream(((ObjectiveCard1) card).getRequiredPositions()).boxed().toList();

            for(int i = 1; index < 3; i++){
                if(positions.contains(i)){
                    grid += setColorForString(((ObjectiveCard1) card).getCardColors()[index].toString(), "■");
                    index++;
                }
                else grid += "  ";

                if(i == 3 || i == 6)    grid += "\n";
                if(i > 9)   printOut("\nERRORE DI STAMPA.\n");
            }

            printOut(grid);
        }
        else{ // card is an ObjectiveCard2

        }
    }

    public final void update(GenericEvent e){
        synchronized(lock_events){
            inputEvents.add(e);
        }
    }

    @Override
    public void run() {

        clearConsole();

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

                    printOut(ev.msgOutput());

                    int n;
                    GenericEvent newEvent;

                    switch(ev){
                        case DrawCardRequest e :
                            newEvent = new DrawCardResponse(chooseInt(1,2),client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case ChooseObjectiveRequest e :
                            printCard(e.objCard1);
                            printCard(e.objCard2);
                            printOut(e.msgOutput2());
                            newEvent = new ChooseObjectiveResponse(e.getChosenCard(chooseInt(1,2)), client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case NumPlayersRequest e :
                            newEvent = new NumPlayersResponse(chooseInt(2,4), client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case PlayCardRequest e :
                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,80);
                            }while(!e.choiceIsValid(n));

                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.playerView.hand.handCards[n-1].isFacedown = true;

                            printOut(e.msgOutput3());
                            newEvent = new PlayCardResponse(client.getNickname(), e.playerView.hand.handCards[n-1], chooseInt(0, 80), chooseInt(0, 80));
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case SetTokenColorRequest e :
                            n = -1;
                            do{
                                if(n != -1) printOut(inputError());
                                n = chooseInt(1,4);
                            }while(!e.choiceIsValid(n));

                            newEvent = new SetTokenColorResponse(n, client.getNickname());
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case JoinLobby e :
                            if(e.getNickname() != null)  client.setNickname(e.getNickname());

                            newEvent = new SetPassword(client.getNickname(), chooseString("password"));
                            notifyListener(newEvent);
                            printOut(newEvent.msgOutput());
                            break;

                        case PlaceStartingCard e :
                            printCard(e.startingCard);
                            printOut(e.msgOutput2());
                            if(chooseInt(1,2) == 2) e.startingCard.isFacedown = true;

                            notifyListener(new PlaceStartingCard( e.startingCard, client.getNickname()));
                            break;

                        case AckResponse ack :
                            if(!ack.ok){
                                synchronized(lock_events){
                                    inputEvents.addFirst(ack.event);
                                }
                            }
                            break;
                        case ReconnectionRequest e:
                            newEvent = new ReconnectionResponse( client.getNickname(),chooseString("password"));
                            notifyListener(newEvent);
                            break;
                        default :
                            break;
                    }
                }
            }
        }.start();
    }
}
