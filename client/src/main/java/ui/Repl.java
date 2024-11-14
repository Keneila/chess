package ui;

import java.util.Scanner;

import server.ServerFacade;
import ui.EscapeSequences.*;

public class Repl {
    private final StartingClient sClient;
    private final LoggedInClient lClient;
    private final InGameClient gClient;

    public Repl(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        sClient = new StartingClient(server,serverUrl);
        lClient = new LoggedInClient(server,serverUrl);
        gClient = new InGameClient(server,serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}
