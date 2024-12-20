package ui;

import java.util.Scanner;

import ui.server.ServerFacade;
import ui.websocket.ServerMessageHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;


public class Repl implements ServerMessageHandler {
    private final StartingClient sClient;
    private final LoggedInClient lClient;
    private final InGameClient gClient;
    private State state = State.LOGGED_OUT;

    public Repl(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        gClient = new InGameClient(server,serverUrl,state, this);
        lClient = new LoggedInClient(server,serverUrl, state, gClient);
        sClient = new StartingClient(server,serverUrl,state,lClient,gClient);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                Client client;
                switch (state){
                    case LOGGED_IN -> client = lClient;
                    case PLAYING, WATCHING -> client = gClient;
                    case null, default -> {
                        client = sClient;
                        lClient.setAuth(null);
                        gClient.setAuth(null);
                    }
                }
                client.updateState(state);
                result = client.eval(line);
                state = client.getState();
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            if(gClient.getColor() == null){
                System.out.println("\n" + gClient.printBoard(notification.getGame().getBoard(),"white", null));
            }
            System.out.println("\n" + gClient.printBoard(notification.getGame().getBoard(),gClient.getColor(), null));
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(SET_TEXT_COLOR_BLUE + notification.getServerMessageType() + ": " + notification.getErrorMessage());
        } else {
            System.out.println(SET_TEXT_COLOR_BLUE + notification.getServerMessageType() + ": " + notification.getMessage());
        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + "] >>> ");
    }

}
