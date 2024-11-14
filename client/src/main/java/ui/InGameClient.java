package ui;

import server.ServerFacade;

public class InGameClient {
    private final ServerFacade server;
    private final String serverUrl;
    private GameplayState state = GameplayState.NOTPLAYING;

    public InGameClient(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }
}
