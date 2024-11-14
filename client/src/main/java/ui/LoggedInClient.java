package ui;

import server.ServerFacade;

public class LoggedInClient {
    private final ServerFacade server;
    private final String serverUrl;
    private GameplayState gState = GameplayState.NOTPLAYING;
    private LoginState lState = LoginState.LOGGED_OUT;

    public LoggedInClient(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }
}
