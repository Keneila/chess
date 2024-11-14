package ui;

import server.ServerFacade;

public class LoggedInClient implements Client{
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGED_OUT;

    public LoggedInClient(ServerFacade server, String serverUrl, State state) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
    }

    public String eval(String line) {
        return "";
    }

    @Override
    public void updateState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }
}

