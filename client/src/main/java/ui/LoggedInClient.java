package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.CreateGameRequest;
import model.GameData;
import ui.server.ServerFacade;

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
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String join(String[] params) throws Exception{
        if (params.length == 2){
            try {
                int id = Integer.parseInt(params[0]);
                String playerColor = params[1];
                if("black".equalsIgnoreCase(playerColor) || "white".equalsIgnoreCase(playerColor)) {
                    GameData game = find(id);
                    int gameID = 0;
                    ChessGame g = null;
                    if(game != null){
                        g = game.game();
                        gameID = game.gameID();
                    }
                    server.joinGame(auth.authToken(), playerColor, gameID);
                    state = State.PLAYING;
                    gameClient.join(gameID, g, playerColor);
                    assert g != null;
                    return "";
                } else {
                    return "Not an Valid Spot in A Game Right Now. Please pick something else.";
                }
            } catch (Exception e) {
                return "Not an Valid Spot in A Game Right Now. Please pick something else.";
            }
        } else {
            return "Expected: join <ID> [WHITE|BLACK]";
        }
    }
    public GameData find(int id) throws Exception {
        int num = 0;
        ChessGame g = null;
        for (GameData game : server.listGames(auth.authToken())) {
            num++;
            if (num == id) {
                return game;
            }
        }
        return null;
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
    private String create(String... params) throws Exception{
        if (params.length > 0) {
            server.createGame(params[0], auth.authToken());
            return "Game Created.";
        } else {
            return "Please include <Name> when creating a game.";
        }
    }
    private String list() throws Exception{
        try {
            StringBuilder s = new StringBuilder();
            int num = 0;
            for (GameData game : server.listGames(auth.authToken())){
                num++;
                s.append(num + ") " + game.gameName() + " \nPlayers-> white: " +
                        game.whiteUsername() + "  black: " + game.blackUsername() + "\n");
            }
            if (num == 0){
                return "No Created Games.";
            }
            return s.toString();
        } catch (Exception e) {
            return "Invalid Authtoken. Please restart Server and Login Again.";
        }
    }

    private String observe(String... params) throws Exception{
        if (params.length == 1) {
            try {
                int num = 0;
                for (GameData game : server.listGames(auth.authToken())) {
                    num++;
                    if (num == Integer.parseInt(params[0])) {
                        server.joinGame(auth.authToken(), "obs", game.gameID());
                        state = State.WATCHING;
                        gameClient.join(game.gameID(), game.game(), null);
                        return "";
                    }
                }
            } catch (Exception e){
                return "Please input a valid Game ID when attempting to observe.";
            }
        }
        return "Needs a Valid Game ID.";
    }
    private String logout() throws Exception{
        try{
        server.logout(auth.authToken());
        state = State.LOGGED_OUT;
        return "Logged out.";
        } catch (Exception e){
            return "Something went wrong. Please restart.";
        }
    }
    private String quit() throws Exception{
        server.logout(auth.authToken());
        state = State.LOGGED_OUT;
        return "quit";
    }

    private String delete() throws Exception{
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

