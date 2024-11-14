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
        String result = "";
        if (line.equals("help")){
            return help();
        } else if (line.equals("quit")){
            return line;
        }
        return result;
    }

    public String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
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

