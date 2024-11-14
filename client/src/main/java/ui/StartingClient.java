package ui;

import server.ServerFacade;

public class StartingClient implements Client{
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGED_OUT;

    public StartingClient(ServerFacade server, String serverUrl, State state) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
    }

    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
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
