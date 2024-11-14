package ui;

import java.util.Scanner;

import ui.server.ServerFacade;
import static ui.EscapeSequences.*;


public class Repl {
    private final StartingClient sClient;
    private final LoggedInClient lClient;
    private final InGameClient gClient;
    private State state = State.LOGGED_OUT;

    public Repl(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        lClient = new LoggedInClient(server,serverUrl, state);
        gClient = new InGameClient(server,serverUrl,state);
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

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + "] >>> ");
    }

}
