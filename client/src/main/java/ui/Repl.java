package ui;

import java.util.Scanner;

import server.ServerFacade;
import static ui.EscapeSequences.*;


public class Repl {
    private final StartingClient sClient;
    private final LoggedInClient lClient;
    private final InGameClient gClient;
    private State state = State.LOGGED_OUT;

    public Repl(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        sClient = new StartingClient(server,serverUrl,state);
        lClient = new LoggedInClient(server,serverUrl, state);
        gClient = new InGameClient(server,serverUrl,state);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type help to get started.");
        System.out.print(sClient.help());
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
                    case null, default -> client = sClient;
                }
                client.updateState(state);
                result = client.eval(line);
                state = client.getState();
                System.out.print(SET_TEXT_COLOR_BLACK + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + "] >>> " + SET_TEXT_COLOR_BLACK);
    }

}
