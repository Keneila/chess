package ui;

import server.ServerFacade;

public class StartingClient {
    private final ServerFacade server;
    private final String serverUrl;
    private LoginState state = LoginState.LOGGED_OUT;

    public StartingClient(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }
}
