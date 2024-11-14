package ui;

import model.AuthData;
import model.CreateGameRequest;
import server.ServerFacade;
import service.ErrorMessage;

import java.util.Arrays;

public class LoggedInClient implements Client{
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGED_OUT;
    private AuthData auth = null;
    public LoggedInClient(ServerFacade server, String serverUrl, State state) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "observe" -> observe(params);
                case "join" -> join(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ErrorMessage ex) {
            return ex.getMessage();
        }
    }

    private String join(String[] params) throws ErrorMessage{
        return "";
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
    private String create(String... params) throws ErrorMessage{
        server.createGame(new CreateGameRequest(params[0],auth.authToken()));
        return "Game Created.";
    }
    private String list() throws ErrorMessage{
        return server.listGames(auth.authToken()).toString();

    }
    private String observe(String... params) throws ErrorMessage{
        server.joinGame(auth.authToken(), null,Integer.parseInt(params[0]));
        return "Joined Game as Observer";
    }
    private String logout() throws ErrorMessage{
        server.logout(auth.authToken());
        return "Logged out.";
    }
    private String quit() throws ErrorMessage{
        //server.logout(auth.authToken());
        return "quit";
    }
    @Override
    public void updateState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setAuth(AuthData auth) {
        this.auth = auth;
    }
}

