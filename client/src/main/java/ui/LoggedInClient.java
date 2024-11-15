package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.CreateGameRequest;
import model.GameData;
import ui.server.ServerFacade;
import service.ErrorMessage;

import java.util.Arrays;

public class LoggedInClient implements Client{
    private final ServerFacade server;
    private final String serverUrl;
    private final InGameClient gameClient;
    private State state = State.LOGGED_OUT;
    private AuthData auth = null;
    public LoggedInClient(ServerFacade server, String serverUrl, State state, InGameClient gameClient) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
        this.gameClient = gameClient;
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "delete" -> delete();
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
        int gameID = Integer.parseInt(params[0]);
        String playerColor = params[1];
        server.joinGame(auth.authToken(), playerColor, gameID);
        return gameClient.printBoard(new ChessGame().getBoard(), "white") + gameClient.printBoard(new ChessGame().getBoard(), "black");
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
        if (params.length > 0) {
            server.createGame(params[0], auth.authToken());
            return "Game Created.";
        } else {
            return "Please include <Name> when creating a game.";
        }
    }
    private String list() throws ErrorMessage{
        return server.listGames(auth.authToken()).toString();
    }

    private String observe(String... params) throws ErrorMessage{
        if (params.length == 1) {
            try {
                for (GameData game : server.listGames(auth.authToken())) {
                    if (game.gameID() == Integer.parseInt(params[0])) {
                        return gameClient.printBoard(game.game().getBoard(), "white");
                    }
                }
            } catch (Exception e){
                return "Please input a valid Game ID when attempting to observe.";
            }
        }
        return "Not a Valid Game ID.";
    }
    private String logout() throws ErrorMessage{
        server.logout(auth.authToken());
        state = State.LOGGED_OUT;
        return "Logged out.";
    }
    private String quit() throws ErrorMessage{
        server.logout(auth.authToken());
        state = State.LOGGED_OUT;
        return "quit";
    }

    private String delete() throws ErrorMessage{
        server.deleteDB();
        return "Deleted Database";
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

