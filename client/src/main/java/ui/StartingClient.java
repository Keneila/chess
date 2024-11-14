package ui;

import model.AuthData;
import model.LoginRequest;
import model.UserData;
import server.ServerFacade;
import service.ErrorMessage;

import java.util.Arrays;

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

    private String register(String... params) throws ErrorMessage{
        if (params.length >= 3) {
            AuthData auth = server.register(new UserData(params[0], params[1],params[2]));
            state = State.LOGGED_IN;
            return "Register successful";
        }
        throw new ErrorMessage(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }
    private String login(String... params) throws ErrorMessage{
        if (params.length >= 2) {
            AuthData auth = server.login(new LoginRequest(params[0], params[1]));
            state = State.LOGGED_IN;
            return "Login successful.";
        }
        throw new ErrorMessage(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ErrorMessage ex) {
            return ex.getMessage();
        }
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
